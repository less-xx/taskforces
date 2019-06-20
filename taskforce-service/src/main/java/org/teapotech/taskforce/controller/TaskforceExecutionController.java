/**
 * 
 */
package org.teapotech.taskforce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.taskforce.exception.TaskforceDataStoreException;
import org.teapotech.taskforce.service.TaskforceExecutionService;
import org.teapotech.taskforce.web.RestResponse;
import org.teapotech.taskforce.web.SimpleTaskforceEntity;
import org.teapotech.taskforce.web.TaskforceRequest;

/**
 * @author jiangl
 *
 */
@RestController
@CrossOrigin("*")
public class TaskforceExecutionController extends LogonUserController {

	@Autowired
	TaskforceExecutionService taskforceExecutionService;

	@PostMapping("/taskforce-execution")
	public RestResponse<SimpleTaskforceEntity> runTaskforce(@PathVariable("id") String taskforceId,
			@RequestBody TaskforceRequest request, HttpServletRequest httpRequest)
			throws TaskforceDataStoreException {

	}

}
