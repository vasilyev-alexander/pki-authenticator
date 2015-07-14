package org.wso2.carbon.identity.authenticator.handler.internal;

import javax.servlet.Servlet;

import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.authenticator.handler.AuthenticationCallbackHandler;
import org.wso2.carbon.identity.authenticator.handler.AuthenticationHandler;
import org.wso2.carbon.ui.tracker.AuthenticatorRegistry;

/**
 * @scr.component name="authenticator.handler.custom" immediate="true"
 * @scr.reference name="osgi.httpservice"
 *                interface="org.osgi.service.http.HttpService"
 *                cardinality="1..1" policy="dynamic" bind="setHttpService"
 *                unbind="unsetHttpService"
 */
public class AuthenticationHandlerServiceComponent {

	private static HttpService httpService;

	protected void activate(ComponentContext ctxt) {

		try {
			String url = "/carbon/custom/auth-handler";
			String callbackUrl = "/carbon/custom/auth-callback";

			Servlet authoServlet = new ContextPathServletAdaptor(new AuthenticationHandler(), url);
			Servlet callbackServlet =
			                          new ContextPathServletAdaptor(
			                                                        new AuthenticationCallbackHandler(),
			                                                        url);
			try {
				httpService.registerServlet(url, authoServlet, null, null);
				httpService.registerServlet(callbackUrl, callbackServlet, null, null);

			} catch (Exception e) {
				e.printStackTrace();
			}

			AuthenticatorRegistry.init(ctxt.getBundleContext());
			System.out.println("Authenticator handler Bundle activated successfully..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setHttpService(HttpService httpService) {
		AuthenticationHandlerServiceComponent.httpService = httpService;
	}

	protected void unsetHttpService(HttpService httpService) {
		AuthenticationHandlerServiceComponent.httpService = null;
	}

}
