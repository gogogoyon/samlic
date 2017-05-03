package com.samlic.rpc.arch;

public class SamlicException extends RuntimeException {

	private static final long serialVersionUID = 6513914276673153836L;

	private String errCode;
	
	public SamlicException(String message) {
		super(message);
	}
	
	public SamlicException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
	}
	
	public SamlicException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}
	
	public SamlicException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getErrorCode() {
		return this.errCode;
	}
}
