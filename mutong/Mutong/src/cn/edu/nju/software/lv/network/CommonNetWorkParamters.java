package cn.edu.nju.software.lv.network;

public class CommonNetWorkParamters {

	private static String scheme = "http";
	private static String host = "localhost";
	private static String path = "/api";
	private static String encoding = "UTF-8";
	private static int port = 8888;

	public static void setScheme(String scheme) {
		CommonNetWorkParamters.scheme = scheme;
	}

	public static String getScheme() {
		return scheme;
	}
	
	public static void setHost(String host) {
		CommonNetWorkParamters.host = host;
	}
	
	public static String getHost() {
		return host;
	}
	
	public static void setPath(String path) {
		CommonNetWorkParamters.path = path;
	}
	
	public static String getPath() {
		return path;
	}
	
	public static void setEncoding(String encoding) {
		CommonNetWorkParamters.encoding = encoding;
	}
	
	public static String getEncoding() {
		return encoding;
	}
	
	public static void setPort(int port) {
		CommonNetWorkParamters.port = port;
	}
	
	public static int getPort() {
		return port;
	}
}
