package org.teapotech.taskforce.block.def;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teapotech.block.def.CopyFileBlock;
import org.teapotech.taskforce.entity.CustomStorageConfigEntity;
import org.teapotech.taskforce.service.CustomStorageConfigService;

@ExtendWith(MockitoExtension.class)
public class TestCustomBlock {

	@Mock
	CustomStorageConfigService custStorageConfService;

	@BeforeEach
	void init() {
		when(custStorageConfService.getAllCustomStorageConfigs())
				.thenReturn(Arrays.asList(new CustomStorageConfigEntity("id0", "File storage 0"),
						new CustomStorageConfigEntity("id1", "File storage 1"),
						new CustomStorageConfigEntity("id2", "File storage 2")));
	}

	@Test
	public void testCopyFileBlock() {
		CopyFileBlock cfBlock = new CopyFileBlock();
		cfBlock.setCustomStorageConfigService(custStorageConfService);
		String def = cfBlock.getBlockDefinition();
		System.out.println(def);
		assertNotNull(def);

	}
}
