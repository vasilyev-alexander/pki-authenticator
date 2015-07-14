package org.wso2.carbon.identity.authenticator.pki.cert;

import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.wso2.carbon.identity.authenticator.pki.cert.validation.CertificateVerificationException;
import org.wso2.carbon.identity.authenticator.pki.cert.validation.RevocationStatus;
import org.wso2.carbon.identity.authenticator.pki.cert.validation.RevocationVerifier;
import org.wso2.carbon.identity.authenticator.pki.cert.validation.crl.CRLVerifier;
import org.wso2.carbon.identity.authenticator.pki.cert.validation.ocsp.OCSPVerifier;
import org.wso2.carbon.identity.authenticator.pki.dto.VerifiedUser;

public class CarbonCertifcateValidator extends CertificateValidator {

	@Override
	protected VerifiedUser createUser(Certificate certificate) {
		VerifiedUser user = new VerifiedUser();
		String dn = ((X509Certificate) certificate).getSubjectDN().getName();
		int start = dn.indexOf("CN");
		if (start > -1) {
			int end = dn.indexOf(",", start);
			dn = dn.substring(start, end);
			dn = dn.replaceAll("CN=", "");
		}
		user.setSubject(dn);
		return user;
	}

	@Override
	public boolean validateRevocation(Certificate certificate, Certificate issuer) {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		List<RevocationVerifier> verifiers = new ArrayList<RevocationVerifier>();
		verifiers.add(new OCSPVerifier());
		verifiers.add(new CRLVerifier());

		boolean revStatus = false;
		for (RevocationVerifier verifier : verifiers) {
			try {
				RevocationStatus status =
				                          verifier.checkRevocationStatus((X509Certificate) certificate,
				                                                         (X509Certificate) issuer);
				if (status != null && status.equals(RevocationStatus.GOOD)) {
					revStatus = true;
				}
			} catch (CertificateVerificationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;//revStatus;
	}

}
