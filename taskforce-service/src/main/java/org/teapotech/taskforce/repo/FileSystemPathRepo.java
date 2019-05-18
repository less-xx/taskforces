/**
 * 
 */
package org.teapotech.taskforce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.teapotech.taskforce.entity.FileSystemPath;

/**
 * @author jiangl
 *
 */
public interface FileSystemPathRepo
		extends JpaRepository<FileSystemPath, String>, JpaSpecificationExecutor<FileSystemPath> {

	FileSystemPath findByName(String name);

	FileSystemPath findFirstByPath(String path);
}
