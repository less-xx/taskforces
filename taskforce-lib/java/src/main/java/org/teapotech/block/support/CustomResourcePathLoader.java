package org.teapotech.block.support;

import java.util.Collection;

import org.teapotech.taskforce.entity.FileSystemPath;

public interface CustomResourcePathLoader {

	Collection<FileSystemPath> getAllFileSystemPaths();

	FileSystemPath getFileSystemPathById(String id);
}
