package com.samlic.rpc.arch.codec;

public interface SerializerFactory {
	<T> Serializer<T> getSerializer();
}
