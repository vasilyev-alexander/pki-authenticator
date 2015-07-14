package org.wso2.carbon.identity.authenticator.pki.internal;

import java.util.Hashtable;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.services.authentication.CarbonServerAuthenticator;
import org.wso2.carbon.identity.authenticator.pki.PKIAuthenticator;

/**
 * @scr.component name="pki.authenticator" immediate="true"
 */
public class PKIServiceComponent {

	protected void activate(ComponentContext ctxt) {

		PKIAuthenticator authenticator = new PKIAuthenticator();
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(CarbonConstants.AUTHENTICATOR_TYPE,
				authenticator.getAuthenticatorName());
		ctxt.getBundleContext()
				.registerService(CarbonServerAuthenticator.class.getName(),
						authenticator, props);
		System.out.println("PKI Authenticator Bundle activated successfully..");
	}

}
