/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.credentials.UsernamePasswordCredentials;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.CredentialType;
import org.teapotech.credentials.service.CredentialsService;
import org.teapotech.credentials.web.CredentialsRequest;
import org.teapotech.credentials.web.SimpleCredentialsResponse;
import org.teapotech.taskforce.web.RestResponse;
import org.teapotech.util.JsonHelper;

/**
 * @author lessdev
 *
 */
@RestController
public class CredentialsController extends LogonUserController {

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	JsonHelper jsonHelper;

	@PostMapping("/credentials")
	@ResponseBody
	public RestResponse<SimpleCredentialsResponse> createCredentials(@RequestBody CredentialsRequest request,
			HttpServletRequest httpRequest) throws Exception {

		Credentials cred = credentialsService.findByName(request.getName());
		if (cred != null) {
			throw new IllegalArgumentException("Credentials already exists.");
		}
		if (request.getType() == CredentialType.USERNAME_PASSWORD) {
			UsernamePasswordCredentials co = jsonHelper.getObject(request.getConfiguration(),
					UsernamePasswordCredentials.class);

			String conf = jsonHelper.getJSON(co);

			cred = new Credentials();
			cred.setCredentials(conf);
			cred.setName(request.getName());
			cred.setType(request.getType());
			cred.setUpdatedBy(getLogonUser(httpRequest).getName());
			cred.setLastUpdatedTime(new Date());
			cred = credentialsService.saveCredentials(cred);
			return new RestResponse<SimpleCredentialsResponse>(new SimpleCredentialsResponse(cred));
		}

		throw new IllegalArgumentException("Invalid request type [" + request.getType() + "]");
	}
}
