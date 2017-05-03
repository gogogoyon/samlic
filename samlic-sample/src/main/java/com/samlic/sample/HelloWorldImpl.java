package com.samlic.sample;

public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHello(String name) {
		return "Hello " + name + "!";
	}

}
