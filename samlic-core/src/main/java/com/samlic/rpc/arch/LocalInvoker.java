package com.samlic.rpc.arch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocalInvoker implements ServiceInvoker {
	
	private ServiceRegistry registry;
	
	public LocalInvoker(ServiceRegistry registry) {
		this.registry = registry;
	}

	public void setRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}

	@Override
	public InvokeResult invoke(Invoke invoke) {
		Object instance = registry.getServiceInstance(invoke.getInterfaceName());
		List<Class<?>> argClassLists = new ArrayList<Class<?>>();
		for(Object arg : invoke.getArguments()) {
			argClassLists.add(arg.getClass());
		}
		
		RpcResponse result = new RpcResponse();
		try {
			Method method = instance.getClass().getMethod(invoke.getMethodName(), 
					argClassLists.toArray(new Class<?>[0]));
			Object data = method.invoke(instance, invoke.getArguments().toArray());
			result.setReturnValue(data);
		} catch (Exception e) {
			result.setThrowable(e);
		} 
		
		return result;
	}

	@Override
	public boolean isLocal() {		
		return true;
	}
}
