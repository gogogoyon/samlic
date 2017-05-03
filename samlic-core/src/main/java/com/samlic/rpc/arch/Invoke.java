package com.samlic.rpc.arch;

import java.util.List;

/**
 * Structure of remote procedure invocation.
 * @author yuanpeng
 * @since 2017-03-05
 */
public interface Invoke {
	/**
	 * Get name of interface
	 * @return
	 */
	String getInterfaceName();
	/**
	 * Get name of method
	 * @return
	 */
	String getMethodName();
	
	/**
	 * Get arguments of this request
	 * @return
	 */
	List<Object> getArguments();
}
