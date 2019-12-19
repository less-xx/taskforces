/**
 * 
 */
package org.teapotech.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.base.controller.LogonUserController;
import org.teapotech.base.web.RestResponse;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.service.CredentialsService;
import org.teapotech.resource.entity.ResourceConfigEntity;
import org.teapotech.resource.entity.ResourceConfigEntity.Type;
import org.teapotech.resource.exception.CreateResourceException;
import org.teapotech.resource.service.ResourceService;
import org.teapotech.resource.web.ResourceConfigRequest;
import org.teapotech.resource.web.ResourceConfigResponse;

/**
 * @author lessdev
 *
 */
@RestController
public class ResourceController extends LogonUserController {

	@Autowired
	ResourceService resourceService;

	@Autowired
	CredentialsService credentialService;

	@PostMapping("/resources")
	@ResponseBody
	public RestResponse<ResourceConfigResponse> createResource(@RequestBody ResourceConfigRequest request,
			HttpServletRequest httpRequest) throws CreateResourceException {
		ResourceConfigEntity rce = resourceService.findByNameAndType(request.getName(), request.getType());
		if (rce != null) {
			throw new IllegalArgumentException("Resource with the same name and type is already existed.");
		}

		if (request.getType() == Type.SQL_QUERY) {
			Credentials dbCred = credentialService.getCredentialsById(request.getCredentialsId());
			if (dbCred == null) {
				throw new IllegalArgumentException("Invalid DB credentials id.");
			}

		}

		return null;
	}

}
