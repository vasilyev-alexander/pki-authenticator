package org.wso2.carbon.identity.authenticator.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationCallbackHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	                                                                      IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	                                                                       throws ServletException,
	                                                                       IOException {
		System.out.println("Creating the sessions");

		String loginStatus = req.getParameter("loginStatus");
		System.out.println(loginStatus);
		req.getSession().invalidate();

		String msg = "-1";
		if ("true".equalsIgnoreCase(loginStatus)) {
			// TODO do the custom application specific changes
		}
		String url = String.valueOf(req.getSession().getAttribute("redirect"));
		resp.sendRedirect(url + "?sid=" + msg);

	}

}
