package com.samlic.rpc.arch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.samlic.rpc.arch.codec.SerializerFactory;
import com.samlic.rpc.arch.codec.JavaObjectSerializerFactory;

public class ObjectHolder<T> {
	private T object;
	private ObjectHolder(T object) {
		this.object = object;
	}
	
	public T get() {
		return this.object;
	}	
	
	private static ConcurrentMap<String, ObjectHolder<?>> objectMapper = new ConcurrentHashMap<String, ObjectHolder<?>>();
	
	static {
		objectMapper.putIfAbsent("SerializerFactory.Java", new ObjectHolder<SerializerFactory>(new JavaObjectSerializerFactory()));
		ServiceRegistry serviceRegistry = new LocalServiceRegistry();
		objectMapper.putIfAbsent("ServiceRegistry.LOCAL", new ObjectHolder<ServiceRegistry>(serviceRegistry));
		objectMapper.putIfAbsent("ServiceInvoker.LOCAL", new ObjectHolder<ServiceInvoker>(new LocalInvoker(serviceRegistry)));
	}
	
	public static void putObject(String key, String className) {
		if(objectMapper.containsKey(key)) {
			throw new SamlicException("Duplicate key.");
		}		
			
		try {
			Class<?> classz = Class.forName(className);
			objectMapper.putIfAbsent(key, new ObjectHolder<Object>(classz.newInstance()));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new SamlicException("Failed to initiate class.", e);
		}
	}
	
	public static void putObject(String key, Object object) {
		if(objectMapper.containsKey(key)) {
			throw new SamlicException("Duplicate key.");
		}		
		
		objectMapper.putIfAbsent(key, new ObjectHolder<Object>(object));		
	}
	
	@SuppressWarnings("unchecked")
	public static <I> I getObject(String key) {
		return (I)objectMapper.get(key).get();
	}
}
