package com.samlic.rpc.arch.netty;

import com.samlic.rpc.arch.Invoke;
//import com.samlic.rpc.arch.RpcRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcRequestEncoder  extends MessageToByteEncoder<Invoke> {
	
	private RpcMessageEncoder<Invoke> encoder;
	
	public RpcRequestEncoder(RpcMessageEncoder<Invoke> encoder) {
		this.encoder = encoder;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Invoke msg, ByteBuf out) throws Exception {
		encoder.encode(ctx, msg, out);		
	}
}
