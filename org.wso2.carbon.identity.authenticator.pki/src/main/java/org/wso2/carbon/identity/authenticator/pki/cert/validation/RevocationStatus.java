package org.wso2.carbon.identity.authenticator.pki.cert.validation;

public enum RevocationStatus {
	GOOD("Good"), UNKNOWN("Unknown"), REVOKED("Revoked");
	private String message;

	private RevocationStatus(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
