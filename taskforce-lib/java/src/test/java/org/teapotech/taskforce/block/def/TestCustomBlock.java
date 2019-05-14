package org.teapotech.taskforce.block.def;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teapotech.block.def.file.FilePathBlock;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.taskforce.entity.CustomResourcePath;

@ExtendWith(MockitoExtension.class)
public class TestCustomBlock {

	@Mock
	CustomResourcePathLoader customResourcePathLoader;

	@BeforeEach
	void init() {
		when(customResourcePathLoader.getAllCustomResourcePaths())
				.thenReturn(Arrays.asList(new CustomResourcePath("id0", "Temp file system"),
						new CustomResourcePath("id1", "File storage 1"),
						new CustomResourcePath("id2", "Shared folder 2")));
	}

	@Test
	public void testResourcePathBlock() {
		FilePathBlock b = new FilePathBlock();
		b.setCustomResourcePathLoader(customResourcePathLoader);
		String def = b.getBlockDefinition();
		System.out.println(def);
		assertNotNull(def);
	}

	@Test
	public void testCopyFileBlock() {

	}
}
