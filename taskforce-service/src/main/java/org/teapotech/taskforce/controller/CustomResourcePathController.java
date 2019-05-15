/**
 * 
 */
package org.teapotech.taskforce.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.exception.CustomResourcePathException;
import org.teapotech.taskforce.service.CustomResourcePathServiceImpl;
import org.teapotech.taskforce.web.FileSystemPathRequest;
import org.teapotech.taskforce.web.RestResponse;

/**
 * @author lessdev
 *
 */
@RestController
public class CustomResourcePathController extends LogonUserController {

	@Autowired
	CustomResourcePathServiceImpl custResourcePathService;

	@GetMapping("/custom-file-paths")
	@ResponseBody
	public RestResponse<Page<FileSystemPath>> getFileSystemPaths(Pageable pageable, HttpServletRequest httpRequest)
			throws CustomResourcePathException {

		Page<FileSystemPath> result = custResourcePathService.getAllFileSystemPaths(pageable);
		return new RestResponse<Page<FileSystemPath>>(result);
	}

	@PostMapping("/custom-file-paths")
	@ResponseBody
	public RestResponse<FileSystemPath> createFileSystemPaths(@RequestBody FileSystemPathRequest request,
			HttpServletRequest httpRequest) throws CustomResourcePathException {

		FileSystemPath existedPath = custResourcePathService.findFileSystemPathByName(request.getName());
		if (existedPath != null) {
			throw new CustomResourcePathException("The name already exists.");
		}
		FileSystemPath fpath = new FileSystemPath();
		fpath.setName(request.getName());
		fpath.setDescription(request.getDescription());
		fpath.setPath(request.getPath());
		fpath.setUpdatedBy(getLogonUser(httpRequest).getName());
		fpath.setLastUpdatedTime(new Date());
		fpath = custResourcePathService.saveFileSystemPath(fpath);
		return RestResponse.ok(fpath);
	}

	@PutMapping("/custom-file-paths/{id}")
	@ResponseBody
	public RestResponse<FileSystemPath> updateFileSystemPaths(@PathVariable String id,
			@RequestBody FileSystemPathRequest request, HttpServletRequest httpRequest)
			throws CustomResourcePathException {

		FileSystemPath fpath = custResourcePathService.getFileSystemPathById(id);
		if (fpath == null) {
			throw new CustomResourcePathException("Cannot find file system path by id " + id);
		}

		FileSystemPath existedPath = custResourcePathService.findFileSystemPathByName(request.getName());
		if (existedPath != null && !existedPath.getId().equals(id)) {
			throw new CustomResourcePathException("The name already exists.");
		}
		fpath.setName(request.getName());
		fpath.setDescription(request.getDescription());
		fpath.setPath(request.getPath());
		fpath.setUpdatedBy(getLogonUser(httpRequest).getName());
		fpath.setLastUpdatedTime(new Date());
		fpath = custResourcePathService.saveFileSystemPath(fpath);
		return RestResponse.ok(fpath);
	}
}
