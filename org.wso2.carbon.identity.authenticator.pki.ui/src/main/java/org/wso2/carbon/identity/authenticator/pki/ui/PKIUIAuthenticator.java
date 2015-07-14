package org.wso2.carbon.identity.authenticator.pki.ui;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axiom.om.util.Base64;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.core.common.AuthenticationException;
import org.wso2.carbon.identity.authenticator.pki.stub.PKIAuthenticatorStub;
import org.wso2.carbon.identity.authenticator.pki.stub.dto.VerifiedUser;
import org.wso2.carbon.ui.BasicAuthUIAuthenticator;
import org.wso2.carbon.ui.CarbonUIUtil;

public class PKIUIAuthenticator extends BasicAuthUIAuthenticator {

	private static final String AUTHENTICATOR_NAME = "PKIAuthenticator";

	@Override
	public boolean canHandle(HttpServletRequest req) {
		String contentType = req.getContentType();
		return contentType != null && contentType.indexOf("multipart/form-data") > -1;
	}

	@Override
	public void authenticate(HttpServletRequest request) throws AuthenticationException {
		handleSecurity(null, false, request);
	}

	@Override
	public String doAuthentication(Object arg0, boolean arg1, ServiceClient arg2,
	                               HttpServletRequest request) throws AuthenticationException {
		HttpSession session = request.getSession();
		ServletContext servletContext = request.getSession().getServletContext();
		ConfigurationContext configContext =
		                                     (ConfigurationContext) servletContext.getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
		String backEndServerURL = request.getParameter("backendURL");
		if (backEndServerURL == null) {
			backEndServerURL = CarbonUIUtil.getServerURL(servletContext, session);
		}
		session.setAttribute(CarbonConstants.SERVER_URL, backEndServerURL);

		byte[] content = null;
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					@SuppressWarnings("unchecked")
					List<FileItem> multiparts =
					                            new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

					for (FileItem item : multiparts) {
						if (!item.isFormField()) {
							InputStream is = item.getInputStream();
							content = new byte[is.available()];
							is.read(content);
							break;
						}
					}

					// File uploaded successfully
					request.setAttribute("message", "File Uploaded Successfully");
				} catch (Exception ex) {
					request.setAttribute("message", "File Upload Failed due to " + ex);
				}

			}

			PKIAuthenticatorStub stub =
			                            new PKIAuthenticatorStub(configContext, backEndServerURL +
			                                                                    "PKIAuthenticator");
			if (content != null) {
				VerifiedUser user = stub.login(Base64.encode(content));
				if (user != null) {
					return user.getSubject();
				} else {
					throw new AuthenticationException("Authentication faliure for the user ");
				}
			}

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAuthenticatorName() {
		return AUTHENTICATOR_NAME;
	}

	@Override
	public void handleRememberMe(Map transportHeaders, HttpServletRequest httpServletRequest)
	                                                                                         throws AuthenticationException {
		super.handleRememberMe(transportHeaders, httpServletRequest);
	}

	@Override
	public void unauthenticate(Object arg0) throws Exception {
		super.unauthenticate(arg0);
	}

}
