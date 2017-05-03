package com.samlic.sample;

import com.samlic.rpc.arch.netty.RpcServerHandler;


public class Main {
	
	public static void main(String[] args) {
		RpcServerHandler server = new RpcServerHandler();
		server.getRegistry().registerService("com.samlic.sample.HelloWorld", HelloWorldImpl.class);
		try {
			server.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
