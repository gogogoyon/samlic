package com.samlic.rpc.arch;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.Map.Entry;

public class UIL implements Serializable {

	private static final long serialVersionUID = -8924315098175727788L;
	
	private String scheme;
	
	private String host;
	
	private int port;
	
	private String interfaceName;
	
	private String interfaceVersion;
	
	private EnumMap<ParameterType, String> paramMap;

    public UIL(String uilString) {
    	try {
			URI uri = new URI(uilString);
			this.scheme = uri.getScheme();
			this.host = uri.getHost();
			this.port = uri.getPort();
			this.interfaceName = uri.getPath().substring(1);
			this.interfaceVersion = uri.getFragment();
			this.paramMap = new EnumMap<ParameterType, String>(ParameterType.class);
			parseQueryStr(uri.getQuery());
		} catch (URISyntaxException e) {
			throw new SamlicException("Failed to parse uilString.", e);
		}
    }
   
	public UIL(String scheme, String host, int port, String interfaceName, String interfaceVersion) {
		super();
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.interfaceName = interfaceName;
		this.interfaceVersion = interfaceVersion;
	}
	
	private void parseQueryStr(String queryStr) {
		if(queryStr != null && !queryStr.isEmpty()) {
			String[] pairs = queryStr.split("&");
			for(String pair : pairs) {
				int index = pair.indexOf("=");
				String keyStr = pair.substring(0, index);
				String value = pair.substring(index + 1);
				this.paramMap.put(ParameterType.valueOf(keyStr), value);
			}
		}
	}
	
	public String getParam(ParameterType type) {
		return paramMap.get(type);
	}
	
	public String getObjectKey(ParameterType type) {
		return type.getObjectKey(paramMap.get(type));
	}

	public String getScheme() {
		return scheme;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((interfaceName == null) ? 0 : interfaceName.hashCode());
		result = prime * result + ((interfaceVersion == null) ? 0 : interfaceVersion.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIL other = (UIL) obj;
		if (interfaceName == null) {
			if (other.interfaceName != null)
				return false;
		} else if (!interfaceName.equals(other.interfaceName))
			return false;
		if (interfaceVersion == null) {
			if (other.interfaceVersion != null)
				return false;
		} else if (!interfaceVersion.equals(other.interfaceVersion))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(scheme + "://" + host + ":" + port + "/" + interfaceName);
		if(!paramMap.isEmpty()) {
			sb.append("?");
			for(Entry<ParameterType, String> entry : paramMap.entrySet()) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("#" + interfaceVersion);
		return 	sb.toString();
	}
}
