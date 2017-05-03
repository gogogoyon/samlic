package com.samlic.rpc.arch.netty;

import com.samlic.rpc.arch.InvokeResult;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcResponseEncoder extends MessageToByteEncoder<InvokeResult> {
	
	private RpcMessageEncoder<InvokeResult> encoder;
	
	public RpcResponseEncoder(RpcMessageEncoder<InvokeResult> encoder) {
		this.encoder = encoder;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, InvokeResult msg, ByteBuf out) throws Exception {
		encoder.encode(ctx, msg, out);		
	}
}