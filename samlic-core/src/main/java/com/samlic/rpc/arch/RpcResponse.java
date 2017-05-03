package com.samlic.rpc.arch;

import java.io.Serializable;

public class RpcResponse implements InvokeResult, Serializable {

	private static final long serialVersionUID = -5411953164521130170L;
	
	private Object returnValue;
	private Throwable throwable;

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public Object getReturnValue() {
		// TODO Auto-generated method stub
		return returnValue;
	}

	@Override
	public Throwable getThrowable() {
		// TODO Auto-generated method stub
		return throwable;
	}

}
