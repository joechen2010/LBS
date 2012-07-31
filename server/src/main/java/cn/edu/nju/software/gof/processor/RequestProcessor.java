package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestProcessor {

	public void process(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
}
