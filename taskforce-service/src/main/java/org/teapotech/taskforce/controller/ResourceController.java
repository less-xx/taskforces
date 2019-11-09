/**
 * 
 */
package org.teapotech.taskforce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.resource.exception.CreateResourceException;
import org.teapotech.resource.service.ResourceService;
import org.teapotech.resource.web.ResourceConfigRequest;
import org.teapotech.resource.web.ResourceConfigResponse;
import org.teapotech.taskforce.web.RestResponse;

/**
 * @author lessdev
 *
 */
@RestController
public class ResourceController extends LogonUserController {

	@Autowired
	ResourceService resourceService;

	@PostMapping("/resources")
	@ResponseBody
	public RestResponse<ResourceConfigResponse> createResource(@RequestBody ResourceConfigRequest request,
			HttpServletRequest httpRequest) throws CreateResourceException {
		return null;
	}
}
