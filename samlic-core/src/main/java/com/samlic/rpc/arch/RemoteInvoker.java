package com.samlic.rpc.arch;

import com.samlic.rpc.arch.codec.SerializerFactory;
import com.samlic.rpc.arch.netty.RpcMessageDecoder;
import com.samlic.rpc.arch.netty.RpcMessageEncoder;
import com.samlic.rpc.arch.netty.RpcRequestEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Remote invoker
 * @author yuanpeng
 *
 */
public class RemoteInvoker  extends ChannelInboundHandlerAdapter implements ServiceInvoker {
	private String ip;
	private int port;
	
	private Invoke invoke;
	private InvokeResult result;
	
	public RemoteInvoker(String ip, int port, SerializerFactory factory) {
		this.ip = ip;
		this.port = port;
		this.factory = factory;
	}
	
	/**
	 * Serializer factory.
	 */
	private SerializerFactory factory;

	public void setFactory(SerializerFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Max length of data frame.
	 */
	private int maxFrameLength;
	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}

	@Override
	public InvokeResult invoke(Invoke invoke) {
		this.invoke = invoke;
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap(); 
			b.group(workerGroup); 
			b.channel(NioSocketChannel.class); 
			b.option(ChannelOption.SO_KEEPALIVE, true); 
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new RpcMessageDecoder<InvokeResult>(maxFrameLength, factory.<InvokeResult>getSerializer()))
					.addLast(new RpcRequestEncoder(new RpcMessageEncoder<Invoke>(factory.<Invoke>getSerializer())))
					.addLast(RemoteInvoker.this);
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(ip, port).sync(); 
			
			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new SamlicException("Failed to invoke remote.", e);
		} finally {
			workerGroup.shutdownGracefully();
		}
		
		return result;
	}
	
	@Override
	public boolean isLocal() {		
		return false;
	}
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		final ChannelFuture f = ctx.writeAndFlush(invoke);
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert f == future;
				//ctx.close();				
			}
		}); 
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {		
		if(msg instanceof InvokeResult) {
			this.result = (InvokeResult)msg;
			ctx.close();			
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
