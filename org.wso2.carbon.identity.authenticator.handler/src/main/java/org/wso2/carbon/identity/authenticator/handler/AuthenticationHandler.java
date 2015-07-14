package org.wso2.carbon.identity.authenticator.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationHandler extends HttpServlet {

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

		req.getSession().setAttribute("redirect", req.getParameter("redirect"));
		req.getSession().setAttribute(HandlerConstants.FLOW_IDENTIFIER,
		                              HandlerConstants.FLOW_IDENTIFIER_CUSTOM_AUTH);
		req.getRequestDispatcher("carbon/login_action.jsp?IndexPageURL=/carbon/custom/auth-callback&gsHttpRequest=/carbon/custom/auth-callback")
		   .forward(req, resp);

	}

}
