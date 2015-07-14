package org.wso2.carbon.identity.authenticator.pki.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class LoginPageFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain arg2)
	                                                                                 throws IOException,
	                                                                                 ServletException {
		((HttpServletResponse) servletResponse).sendRedirect("../pki/login.jsp");

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
