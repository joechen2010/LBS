package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.RichManUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class BuyExistedPlaceProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String placeID = request.getParameter(ServletParam.RequestParam.PlaceID);

		boolean success = RichManUtilities.buyExistedPlace(sessionID, placeID);

		ResponseUtilities.writeMessage(response, success ? 1 : 0,
				ResponseUtilities.TEXT);

	}

}
