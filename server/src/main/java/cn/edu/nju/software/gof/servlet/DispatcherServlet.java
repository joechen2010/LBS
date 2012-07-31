package cn.edu.nju.software.gof.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import cn.edu.nju.software.gof.processor.RequestProcessor;
import cn.edu.nju.software.gof.processor.RequestProcessorFactory;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPostAndGet(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPostAndGet(req, resp);
	}

	private void doPostAndGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		String processorName = null;
		if (isMultipart) {
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();
			// Parse the request
			FileItemIterator iter;
			try {
				iter = upload.getItemIterator(req);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						String value = Streams.asString(stream, "UTF-8");
						if (name.equals(ServletParam.ProcessorName)) {
							processorName = value;
						} else {
							req.setAttribute(name, value);
						}
					} else {
						req.setAttribute(name, getBytesFromStream(stream));
					}
				}
			} catch (FileUploadException e) {
			}
		} else {
			processorName = req.getParameter(ServletParam.ProcessorName);
		}
		if (processorName != null) {
			RequestProcessor processor = RequestProcessorFactory
					.getProcessor(processorName);
			if (processor != null) {
				processor.process(req, resp);
			} else {
				resp.setContentType("text/plain");
				resp.getWriter().println("Processor Name is invalid!");
			}
		} else {
			resp.setContentType("text/plain");
			resp.getWriter().println("Processor Name is not provided!");
		}
	}

	private byte[] getBytesFromStream(InputStream in) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int b = 0;
		try {
			while ((b = in.read()) != -1) {
				buffer.write(b);
			}
			return buffer.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

}
