package com.samlic.rpc.arch;

public enum ParameterType {
	SerializerFactory("Java");
	
	private String defaultValue;
	
	private ParameterType(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getObjectKey(String value) {
		if(value == null || value.isEmpty()) {
			return this.name() + "." + defaultValue;
		} else {
			return this.name() + "." + value;
		}
	}
}
