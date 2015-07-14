package org.wso2.carbon.identity.authenticator.pki;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

import org.apache.axiom.om.util.Base64;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.core.security.AuthenticatorsConfiguration;
import org.wso2.carbon.core.services.authentication.CarbonServerAuthenticator;
import org.wso2.carbon.core.services.util.CarbonAuthenticationUtil;
import org.wso2.carbon.identity.authenticator.pki.cert.CertificateValidator;
import org.wso2.carbon.identity.authenticator.pki.dto.VerifiedUser;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

public class PKIAuthenticator extends AbstractAdmin implements CarbonServerAuthenticator {

	public static final String AUTHENTICATOR_NAME = "PKIAuthenticator";
	private static final String CERT_VALIDATOR = "CertValidator";

	private CertificateValidator validator = null;

	public PKIAuthenticator() {
		super();
		init();
	}

	public PKIAuthenticator(AxisConfiguration axisConfig) throws Exception {
		super(axisConfig);
		init();
	}

	private void init() {
		AuthenticatorsConfiguration authenticatorsConfiguration =
		                                                          AuthenticatorsConfiguration.getInstance();
		AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
		                                                                      authenticatorsConfiguration.getAuthenticatorConfig(AUTHENTICATOR_NAME);
		String certValidator = authenticatorConfig.getParameters().get(CERT_VALIDATOR);
		try {
			validator = (CertificateValidator) Class.forName(certValidator).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getPriority() {
		AuthenticatorsConfiguration authenticatorsConfiguration =
		                                                          AuthenticatorsConfiguration.getInstance();
		AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
		                                                                      authenticatorsConfiguration.getAuthenticatorConfig(AUTHENTICATOR_NAME);
		if (authenticatorConfig != null && authenticatorConfig.getPriority() > 0) {
			return authenticatorConfig.getPriority();
		}
		return 0;

	}

	@Override
	public boolean isDisabled() {
		AuthenticatorsConfiguration authenticatorsConfiguration =
		                                                          AuthenticatorsConfiguration.getInstance();
		AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
		                                                                      authenticatorsConfiguration.getAuthenticatorConfig(AUTHENTICATOR_NAME);
		if (authenticatorConfig != null && authenticatorConfig.getPriority() > 0) {
			return authenticatorConfig.isDisabled();
		}
		return false;

	}

	@Override
	public boolean authenticateWithRememberMe(MessageContext arg0) {
		return false;
	}

	@Override
	public String getAuthenticatorName() {
		return AUTHENTICATOR_NAME;
	}

	@Override
	public boolean isAuthenticated(MessageContext arg0) {
		return false;
	}

	@Override
	public boolean isHandle(MessageContext arg0) {
		return true;
	}

	public VerifiedUser login(String certContent) {

		CertificateFactory cf;
		VerifiedUser user = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			Collection<?> c =
			                  cf.generateCertificates(new ByteArrayInputStream(
			                                                                   Base64.decode(certContent)));
			Iterator<?> i = c.iterator();
			while (i.hasNext()) {
				Certificate cert = (Certificate) i.next();
				user = validator.validate(cert);

				int tenantId = MultitenantUtils.getTenantId(configurationContext);
				CarbonAuthenticationUtil.onSuccessAdminLogin(getHttpSession(),
				                                             user.getSubject(),
				                                             tenantId,
				                                             MultitenantUtils.getTenantDomain(user.getSubject()),
				                                             "127.0.0.1");

				return user;
			}

		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
