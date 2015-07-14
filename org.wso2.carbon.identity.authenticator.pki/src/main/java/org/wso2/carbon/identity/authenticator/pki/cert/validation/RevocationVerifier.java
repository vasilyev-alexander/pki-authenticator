package org.wso2.carbon.identity.authenticator.pki.cert.validation;

import java.security.cert.X509Certificate;


public interface RevocationVerifier {
    public RevocationStatus checkRevocationStatus(X509Certificate peerCert, X509Certificate issuerCert)
            throws CertificateVerificationException;

}
