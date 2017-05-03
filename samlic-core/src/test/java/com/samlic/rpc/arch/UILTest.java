package com.samlic.rpc.arch;

import org.junit.Test;

public class UILTest {
	@Test
	public void testUil() {
		UIL uil = new UIL("LOCAL://127.0.0.1:80/com.samlic.sample.HelloWorld?SerializerFactory=Java#0.0.1");
		System.out.println(uil.toString());
	}
}
