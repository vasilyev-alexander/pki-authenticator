package org.wso2.carbon.identity.authenticator.pki.ui.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.identity.authenticator.pki.ui.PKIUIAuthenticator;
import org.wso2.carbon.identity.authenticator.pki.ui.filter.LoginPageFilter;
import org.wso2.carbon.ui.CarbonUIAuthenticator;

/**
 * @scr.component name="pki.authenticator.ui.component" immediate="true"
 */
public class PKIUIServiceComponent {
	protected void activate(ComponentContext ctxt) {
		PKIUIAuthenticator authenticator = new PKIUIAuthenticator();
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(CarbonConstants.AUTHENTICATOR_TYPE, authenticator.getAuthenticatorName());
		ctxt.getBundleContext().registerService(CarbonUIAuthenticator.class.getName(),
		                                        authenticator, props);

		if (!authenticator.isDisabled()) {
			if (false) {
				// this is commented since we need to track the invalid login
				// with this url from another component
				HttpServlet loginServlet = new HttpServlet() {
					@Override
					protected void doPost(HttpServletRequest req, HttpServletResponse resp)
					                                                                       throws ServletException,
					                                                                       IOException {

					}
				};

				Filter loginPageFilter = new LoginPageFilter();
				Dictionary loginPageFilterProps = new Hashtable(2);
				Dictionary redirectorParams = new Hashtable(3);

				redirectorParams.put("url-pattern", "/carbon/admin/login.jsp");

				redirectorParams.put("associated-filter", loginPageFilter);
				redirectorParams.put("servlet-attributes", loginPageFilterProps);
				ctxt.getBundleContext().registerService(Servlet.class.getName(), loginServlet,
				                                        redirectorParams);
			}
		}
		System.out.println("PKI UI Authenticator registered successfully");

	}

}
