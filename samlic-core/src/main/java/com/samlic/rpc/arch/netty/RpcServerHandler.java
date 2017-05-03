package com.samlic.rpc.arch.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samlic.rpc.arch.Invoke;
import com.samlic.rpc.arch.InvokeResult;
import com.samlic.rpc.arch.LocalInvoker;
import com.samlic.rpc.arch.ObjectHolder;
import com.samlic.rpc.arch.ServiceInvoker;
import com.samlic.rpc.arch.ServiceRegistry;
import com.samlic.rpc.arch.codec.SerializerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private SerializerFactory factory;	
	private ServiceInvoker invoker;	
	private ServiceRegistry registry;
	private int maxFrameLength;
	private int port = 80;
	
	public void setPort(int port) {
		this.port = port;
	}

	public void setMaxFrameLength(int maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}
	
	public void setRegistry(ServiceRegistry registry) {
		this.registry = registry;
		this.invoker = new LocalInvoker(registry);
	}
	
	public ServiceRegistry getRegistry() {
		return this.registry;
	}

	public void setFactory(SerializerFactory factory) {
		this.factory = factory;
	}
	
	public RpcServerHandler() {
		this.factory = ObjectHolder.<SerializerFactory>getObject("SerializerFactory.Java");
		this.registry = ObjectHolder.<ServiceRegistry>getObject("ServiceRegistry.LOCAL");
		this.invoker = ObjectHolder.<ServiceInvoker>getObject("ServiceInvoker.LOCAL");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(msg instanceof Invoke) {
			InvokeResult result = invoker.invoke((Invoke)msg);
			ctx.writeAndFlush(result);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	public void start() throws InterruptedException {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new RpcMessageDecoder<Invoke>(maxFrameLength, factory.<Invoke>getSerializer()))
							.addLast(new RpcResponseEncoder(new RpcMessageEncoder<InvokeResult>(factory.<InvokeResult>getSerializer())))	
							.addLast(RpcServerHandler.this);
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); 
			
			logger.info("Samlic server started at port " + port);
			
			f.channel().closeFuture().sync();			
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public void end() {
		if(workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
		if(workerGroup != null) {
			bossGroup.shutdownGracefully();
		}
	}
}
