package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.business.RichManUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class GetRichManInfoProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		RichManUtilities richManUtilities = new RichManUtilities();
		RichManInfo info = richManUtilities.getRichManInfo(sessionID);

		if (info != null) {
			ResponseUtilities.writeMessage(response, info.toJSONString(),
					ResponseUtilities.JSON);
		}

	}

}
