package org.teapotech.block.support;

import java.util.List;

import org.teapotech.taskforce.entity.CustomResourcePath;

public interface CustomResourcePathLoader {

	List<CustomResourcePath> getAllCustomResourcePaths();

	CustomResourcePath getCustomResourcePathById(String id);
}
