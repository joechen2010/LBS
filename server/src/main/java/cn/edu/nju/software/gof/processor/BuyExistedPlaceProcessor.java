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

		RichManUtilities richManUtilities = new RichManUtilities();
		boolean success = richManUtilities.buyExistedPlace(sessionID, Long.valueOf(placeID));

		ResponseUtilities.writeMessage(response, success ? 1 : 0,
				ResponseUtilities.TEXT);

	}

}
