package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtilities {

	public static String JSON = "application/json";
	public static String TEXT = "text/plain";
	public static String IMAGE = "image/*";

	public static void writeMessage(HttpServletResponse response,
			Object message, String type) {

		try {
			PrintWriter writer = response.getWriter();
			try {
				response.setContentType(type);
				writer.print(message);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void writeImage(HttpServletResponse response, byte[] images) {
		try {
			OutputStream out = response.getOutputStream();
			try {
				response.setContentType(IMAGE);
				out.write(images);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
