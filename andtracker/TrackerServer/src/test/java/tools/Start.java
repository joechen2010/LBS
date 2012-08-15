package tools;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class Start {

	public static final int PORT = 9000;

	public static final String CONTEXT = "/";

	public static final String DEFAULT_BASE_URL = "http://localhost:9000/";

	public static void main(String[] args) throws Exception {
	       // System.getProperties().setProperty("spring.profiles.active", "dev");
		Server server = new Server(PORT);
		server.setHandler(new WebAppContext("src/main/webapp", CONTEXT));
		server.setStopAtShutdown(true);
		server.start();

		System.out.println("Hit Enter in console to stop server");
		if (System.in.read() != 0) {
			System.out.println("Server stopping");
			server.stop();
			System.out.println("Server stopped");
		}
	}
}
