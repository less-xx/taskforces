package org.teapotech.block.support;

import java.util.List;

import org.teapotech.taskforce.entity.FileSystemPath;

public interface CustomResourcePathLoader {

	List<FileSystemPath> getAllFileSystemPaths();

	FileSystemPath getFileSystemPathById(String id);
}
