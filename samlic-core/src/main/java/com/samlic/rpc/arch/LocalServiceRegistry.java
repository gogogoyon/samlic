package com.samlic.rpc.arch;

import java.util.HashMap;
import java.util.Map;

import com.samlic.rpc.arch.codec.SerializerFactory;

public class LocalServiceRegistry implements ServiceRegistry {
	
	private Map<String, Object> serviceMap = new HashMap<String, Object>();

	@Override
	public void registerService(Class<?> classz) {		
		if(classz.isInterface() ||
				classz.getInterfaces().length == 0) {
			throw new SamlicException("Classz doesn't implement a interface.");
		}
		
		for(Class<?> inter : classz.getInterfaces()) {
			try {
				serviceMap.put(inter.getName(), classz.newInstance());
			} catch (InstantiationException e) {
				throw new SamlicException("Failed to get instance of classz.", e);
			} catch (IllegalAccessException e) {
				throw new SamlicException("Failed to get instance of classz.", e);
			}
		}
	}

	@Override
	public void registerService(String interfaceName, Class<?> classz) {
		try {
			serviceMap.put(interfaceName, classz.newInstance());
		} catch (InstantiationException e) {
			throw new SamlicException("Failed to get instance of classz.", e);
		} catch (IllegalAccessException e) {
			throw new SamlicException("Failed to get instance of classz.", e);
		}
	}

	@Override
	public Object getServiceInstance(String interfaceName) {
		return serviceMap.get(interfaceName);
	}

	@Override
	public ServiceInvoker findInvoker(UIL uil) {
		return new RemoteInvoker(uil.getHost(), uil.getPort(), ObjectHolder.<SerializerFactory>getObject(uil.getObjectKey(ParameterType.SerializerFactory)));
	}
}
