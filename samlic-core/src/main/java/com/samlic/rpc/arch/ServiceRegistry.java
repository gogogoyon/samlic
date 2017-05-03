package com.samlic.rpc.arch;

public interface ServiceRegistry extends ServiceRouter {
	/**
	 * Register a servcie with class.
	 * @param classz
	 */
	void registerService(Class<?> classz);
	/**
	 * Register a servcie with interface name and class.
	 * @param interfaceName
	 * @param classz
	 */
	void registerService(String interfaceName, Class<?> classz);
	
	/**
	 * Ge instance of a service.
	 * @param interfaceName
	 * @return
	 */
	Object getServiceInstance(String interfaceName);	
}  
