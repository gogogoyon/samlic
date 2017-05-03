package com.samlic.rpc.arch.netty;

import java.nio.charset.Charset;
import java.util.List;

import com.samlic.rpc.arch.codec.BinarySerializer;
import com.samlic.rpc.arch.codec.Serializer;
import com.samlic.rpc.arch.codec.TextSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Rpc message decoder. Data struct as follows:
 * 
 *  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32
 *  ---------------------------------------------------------------------------------------
 * |                fixed six bytes mark: s a m l i c 									 
 *  ---------------------------------------------------------------------------------------
 *                                        |       version         |                                             +
 *  ---------------------------------------------------------------------------------------
 *            data length                 |        data
 *  ---------------------------------------------------------------------------------------
 * @author yuanpeng
 *
 * @param <T>
 */
public class RpcMessageDecoder<T> extends ByteToMessageDecoder {
	private int step = 0;
	private int length = 0;
	
	protected static final byte[] MARK = new byte[] {'s', 'a', 'm', 'l', 'i', 'c'};
	protected static final byte VERSION = (byte)1;
	
	protected Serializer<T> serializer;
	
	protected final int maxFrameLength;
	
	public RpcMessageDecoder(int maxFrameLength, Serializer<T> serializer) {
		this.maxFrameLength = maxFrameLength <= 0 ? Integer.MAX_VALUE : maxFrameLength;
		this.serializer = serializer;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
	
		if(step == 0 && !markCheck(in)) return;
		
		if(step == 1 && !versionCheck(in)) return;
		
		if(step == 2) {
			if(in.readableBytes() < 3) return;
			
			length = in.readMedium();
			if(length > maxFrameLength) {
				step = 0;
				return;
			}
			
			++step;
		}
		
		if(step == 3) {
			if(in.readableBytes() < length) return;
			byte[] data = new byte[length];
			in.readBytes(data);
			
			length = 0;
			step = 0;		
			
			if(serializer instanceof BinarySerializer) {
				BinarySerializer<T> binSerializer = (BinarySerializer<T>)serializer;
				out.add(binSerializer.deserialize(data));
			} else if(serializer instanceof TextSerializer) {
				TextSerializer<T> txtSerializer = (TextSerializer<T>)serializer;
				out.add(txtSerializer.deserialize(new String(data, Charset.forName("utf-8"))));
			}		
		}
	}
	
	private boolean markCheck(ByteBuf in) {
		int i = 0;
		int len = MARK.length;
		
		if(in.readableBytes() < MARK.length) {
			step = 0;
			return false;
		}
		
		while(i < len) {
			if(in.readByte() != MARK[i]) {
				step = 0;
				return false;
			}
			
			++i;
		}
		
		++step;
		return true;
	}
	
	private boolean versionCheck(ByteBuf in) {
		if(in.readableBytes() < 1) return false;
		
		if(in.readByte() == VERSION) {
			++step;
			return true;
		}
		else {
			step = 0;
			return false;
		}
	}
}
