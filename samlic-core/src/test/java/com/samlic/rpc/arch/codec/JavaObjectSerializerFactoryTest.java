package com.samlic.rpc.arch.codec;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.samlic.rpc.arch.Invoke;
import com.samlic.rpc.arch.RpcRequest;


public class JavaObjectSerializerFactoryTest {
	@Test
	public void testSerializer() {
		JavaObjectSerializerFactory factory = new JavaObjectSerializerFactory();
		BinarySerializer<Invoke>  serializer = (BinarySerializer<Invoke>)factory.<Invoke>getSerializer();
		RpcRequest request = new RpcRequest();
		request.setInterfaceName("com.samlic.example.HelloWorld");
		request.setMethodName("sayHello");
		request.setArguments(Arrays.asList("yp"));
		
		byte[] data = serializer.serialize(request);
		Assert.assertTrue(data.length > 0);
		
		Invoke invoke = serializer.deserialize(data);
		Assert.assertTrue(request.equals(invoke));
	}
}
