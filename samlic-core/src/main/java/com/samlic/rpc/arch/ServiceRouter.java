package com.samlic.rpc.arch;

public interface ServiceRouter {
	ServiceInvoker findInvoker(UIL uil);
}
