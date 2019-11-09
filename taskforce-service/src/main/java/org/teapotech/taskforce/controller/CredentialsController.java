/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.credentials.CredentialsObject;
import org.teapotech.credentials.DBConnectionCredentials;
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

	@GetMapping("/credentials")
	@ResponseBody
	public RestResponse<Page<SimpleCredentialsResponse>> queryCredentials(@RequestParam(required = false) String id,
			@RequestParam(required = false) CredentialType type,
			@RequestParam(name = "enabled", required = false) Boolean enabled,
			@RequestParam(name = "start_time", required = false) Date startTime,
			@RequestParam(name = "updatedBy", required = false) String updatedBy, Pageable pageable) throws Exception {

		Page<Credentials> creds = credentialsService.query(id, type, enabled, startTime, updatedBy, pageable);
		List<SimpleCredentialsResponse> result = creds.getContent().stream().map(c -> new SimpleCredentialsResponse(c))
				.collect(Collectors.toList());
		Page<SimpleCredentialsResponse> body = new PageImpl<>(result, pageable, creds.getTotalElements());
		return new RestResponse<Page<SimpleCredentialsResponse>>(body);
	}

	@GetMapping("/credentials/{id}")
	@ResponseBody
	public RestResponse<CredentialsObject> queryCredentialsById(@PathVariable String id) throws Exception {

		Credentials cred = credentialsService.getCredentialsById(id);
		if (cred == null) {
			throw new EntityNotFoundException("Cannot find the credentials.");
		}
		CredentialsObject cobj = credentialsService.createCredentialsObject(cred);
		cobj = credentialsService.maskPassword(cobj);
		return new RestResponse<CredentialsObject>(cobj);
	}

	@PostMapping("/credentials")
	@ResponseBody
	public RestResponse<SimpleCredentialsResponse> createCredentials(@RequestBody CredentialsRequest request,
			HttpServletRequest httpRequest) throws Exception {

		Credentials cred = credentialsService.findByName(request.getName());
		if (cred != null) {
			throw new IllegalArgumentException("Credentials already exists.");
		}
		String config = null;
		if (request.getType() == CredentialType.USERNAME_PASSWORD) {
			UsernamePasswordCredentials co = jsonHelper.getObject(request.getConfiguration(),
					UsernamePasswordCredentials.class);
			config = jsonHelper.getJSON(co);
		} else if (request.getType() == CredentialType.DB_CONNECTION) {
			DBConnectionCredentials co = jsonHelper.getObject(request.getConfiguration(),
					DBConnectionCredentials.class);
			config = jsonHelper.getJSON(co);
		} else {
			throw new IllegalArgumentException("Invalid request type [" + request.getType() + "]");
		}

		cred = new Credentials();
		cred.setCredentials(config);
		cred.setName(request.getName());
		cred.setType(request.getType());
		cred.setUpdatedBy(getLogonUser(httpRequest).getName());
		cred.setLastUpdatedTime(new Date());
		cred = credentialsService.saveCredentials(cred);
		return new RestResponse<SimpleCredentialsResponse>(new SimpleCredentialsResponse(cred));
	}
}
