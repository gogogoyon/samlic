package com.samlic.rpc.arch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Factory which can create instance of remote interface.
 * @author yuanpeng
 * @since 2017-03-05
 */
public class SamlicFactory implements InvocationHandler {
	private UIL uil;
	
	private SamlicFactory(String uilString) {		
		this.uil = new UIL(uilString);
	}
	
	private SamlicFactory(UIL uil) {		
		this.uil = uil;		
	}

	/**
	 * Create instance of remote interface.
	 * @param interfaceClass
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T  createInstance(Class<?> interfaceClass, String uilString) {		
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaceClass}, new SamlicFactory(uilString));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if(method.getDeclaringClass() == Object.class) {
			return method.invoke(proxy, args);
		}
		
		ServiceInvoker invoker = RegistryType.valueOf(uil.getScheme()).getServiceRegistry().findInvoker(uil);
		
		RpcRequest invoke = new RpcRequest();
		invoke.setInterfaceName(uil.getInterfaceName());
		invoke.setMethodName(method.getName());
		invoke.setArguments(Arrays.asList(args));
		InvokeResult result = invoker.invoke(invoke);
		if(result == null) {
			throw new SamlicException("Invoke error.");
		}
		if(result.getThrowable() != null) {
			throw result.getThrowable();
		}
		
		return result.getReturnValue();
	}
}
