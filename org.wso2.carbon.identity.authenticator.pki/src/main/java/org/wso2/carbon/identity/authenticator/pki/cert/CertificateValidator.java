package org.wso2.carbon.identity.authenticator.pki.cert;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;

import org.wso2.carbon.core.security.AuthenticatorsConfiguration;
import org.wso2.carbon.identity.authenticator.pki.PKIAuthenticator;
import org.wso2.carbon.identity.authenticator.pki.dto.VerifiedUser;

public abstract class CertificateValidator {

	protected static final String TRUST_STORE_PATH = "TrustStorePath";
	private static final Object TRUST_STORE_PWD = "TrustStorePwd";

	public abstract boolean validateRevocation(Certificate certificate, Certificate issuer);

	public VerifiedUser validate(Certificate certificate) {

		Set<TrustAnchor> trustAnchors = getTrustStore();
		Iterator<TrustAnchor> it = trustAnchors.iterator();
		while (it.hasNext()) {
			TrustAnchor anchor = it.next();
			X509Certificate trustCert = anchor.getTrustedCert();
			try {
				certificate.verify(trustCert.getPublicKey());
				if (validateRevocation(certificate, trustCert)) {
					return createUser(certificate);
				}
			} catch (InvalidKeyException e) {
				// e.printStackTrace();
			} catch (CertificateException e) {
				// e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// e.printStackTrace();
			} catch (SignatureException e) {
				// e.printStackTrace();
			}
		}
		return null;

	}

	protected abstract VerifiedUser createUser(Certificate certificate);

	protected Set<TrustAnchor> getTrustStore() {

		try {
			AuthenticatorsConfiguration authenticatorsConfiguration =
			                                                          AuthenticatorsConfiguration.getInstance();
			AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
			                                                                      authenticatorsConfiguration.getAuthenticatorConfig(PKIAuthenticator.AUTHENTICATOR_NAME);
			String trustStorePath = authenticatorConfig.getParameters().get(TRUST_STORE_PATH);
			String trustStorePwd = authenticatorConfig.getParameters().get(TRUST_STORE_PWD);

			if (trustStorePath == null) {
				return null;
			}

			FileInputStream is = new FileInputStream(trustStorePath);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

			keystore.load(is, trustStorePwd.toCharArray());

			// This class retrieves the most-trusted CAs from the keystore
			PKIXParameters params = new PKIXParameters(keystore);

			// Get the set of trust anchors, which contain the most-trusted CA
			// certificates
			return params.getTrustAnchors();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		return null;

	}

}