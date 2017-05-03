package com.samlic.sample;

import com.samlic.rpc.arch.SamlicFactory;

public class SamlicClientTest {
	public static void main(String[] args) {
		HelloWorld service = SamlicFactory.createInstance(HelloWorld.class, "LOCAL://127.0.0.1:80/com.samlic.sample.HelloWorld#0.0.1");
		System.out.println(service.sayHello("yp"));
	}
}
