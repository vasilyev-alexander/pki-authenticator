package org.wso2.carbon.identity.authenticator.pki.cert.validation;

public class CertificateVerificationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CertificateVerificationException(String message) {
		super(message);
	}

	public CertificateVerificationException(Throwable throwable) {
		super(throwable);
	}

	public CertificateVerificationException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
