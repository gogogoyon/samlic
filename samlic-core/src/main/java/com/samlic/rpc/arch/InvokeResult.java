package com.samlic.rpc.arch;

public interface InvokeResult {	
	/**
	 * Return value of a request
	 * @return
	 */
	Object getReturnValue();
	
	/**
	 * Return a throwable object if exists
	 * @return
	 */
	Throwable getThrowable();
}
