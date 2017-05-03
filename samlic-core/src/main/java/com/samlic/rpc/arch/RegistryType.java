package com.samlic.rpc.arch;

/**
 * Enumeration fo registry type
 * @author yuanpeng
 * @since 2017-03-05
 */
public enum RegistryType {
	LOCAL, 
	
	ZOOKEEPER;

	public ServiceRegistry getServiceRegistry() {
		return ObjectHolder.<ServiceRegistry>getObject("ServiceRegistry." + this.name());
	}
}
