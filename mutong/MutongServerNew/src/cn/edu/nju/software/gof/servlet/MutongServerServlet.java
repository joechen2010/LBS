package cn.edu.nju.software.gof.servlet;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class MutongServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// EntityManager em = EMF.getInstance().createEntityManager();
		// StringBuilder sb = new StringBuilder();
		// try {
		// Query query = em.createQuery("SELECT A FROM Account AS A");
		// Account account = (Account) query.getSingleResult();
		// List<Account> friends = account.getProfile().getFriends();
		// for(Account friend : friends) {
		// sb.append(friend.getProfile().getRealName());
		// sb.append("\n");
		// }
		// resp.setContentType("plain/text");
		// resp.getWriter().println(sb.toString());
		// } finally {
		// em.close();
		// }
	}
}
