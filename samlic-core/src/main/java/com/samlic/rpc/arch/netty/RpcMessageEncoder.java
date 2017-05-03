package com.samlic.rpc.arch.netty;

import java.nio.charset.Charset;

import com.samlic.rpc.arch.codec.BinarySerializer;
import com.samlic.rpc.arch.codec.Serializer;
import com.samlic.rpc.arch.codec.TextSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RpcMessageEncoder<I> {

	private static final byte[] MARK = new byte[] {'s', 'a', 'm', 'l', 'i', 'c'};
	private static final byte VERSION = (byte)1;
	
	private Serializer<I> serializer;
	
	public RpcMessageEncoder(Serializer<I> serializer) {
		this.serializer = serializer;
	}

	protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception {
		out.writeBytes(MARK);
		out.writeByte(VERSION);
		byte[] data = null;
		if(serializer instanceof BinarySerializer) {
			BinarySerializer<I> binSerializer = (BinarySerializer<I>)serializer;
			data = binSerializer.serialize(msg);
		} else if(serializer instanceof TextSerializer) {
			TextSerializer<I> txtSerializer = (TextSerializer<I>)serializer;
			data = txtSerializer.serialize(msg).getBytes(Charset.forName("utf-8"));
		}
		
		if(data != null) {
			out.writeMedium(data.length);
			out.writeBytes(data);
		}		
	}
}
