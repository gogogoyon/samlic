package com.samlic.rpc.arch.codec;

public interface BinarySerializer<T> extends Serializer<T> {
	byte[] serialize(T data);
	T deserialize(byte[] data);
}
