package com.samlic.rpc.arch.codec;

public interface TextSerializer<T> extends Serializer<T> {
	String serialize(T data);
	T deserialize(String data);
}
