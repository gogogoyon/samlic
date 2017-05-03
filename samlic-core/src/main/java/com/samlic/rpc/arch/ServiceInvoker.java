package com.samlic.rpc.arch;

public interface ServiceInvoker {
	InvokeResult invoke(Invoke invoke);
	boolean isLocal();
}
