package com.wizzdi.flexicore.boot.multipart;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.multipart.app.App;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

public class PluginLoadingTest {

	private static final Logger logger= LoggerFactory.getLogger(PluginLoadingTest.class);
	private static final String pluginsPath;
	private static final String entitiesPath;
	private static final String PLUGIN_ID = "myPlugin";

	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

	@Autowired
	private TestRestTemplate restTemplate;

	static{
		pluginsPath=getPluginsDir("plugins");
		entitiesPath=getPluginsDir("entities");;
		try {
			File pluginsDir = new File(pluginsPath);
			if (!pluginsDir.exists()) {
				if (!pluginsDir.mkdirs()) {
					logger.error("failed creating plugins dir");
				}
			}
			PluginJar pluginZip = new PluginJar.Builder(pluginsDir.toPath().resolve("my-plugin-1.2.3.zip"), PLUGIN_ID)
					.extension("com.wizzdi.flexicore.boot.multipart.pluginA.testMultiPart")
					.pluginVersion("1.2.3")
					.build();
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}

	}

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {

		registry.add("flexicore.plugins", ()->pluginsPath);
		registry.add("flexicore.entities", ()->entitiesPath);

	}

	private static String getPluginsDir(String prefix) {
		try {
			return Files.createTempDirectory(prefix).toFile().getAbsolutePath();

		}
		catch (Exception e){
			logger.error("failed getting "+prefix+" dir",e);
			return null;
		}

	}

	@Test
	public void testMultipart() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
				= new LinkedMultiValueMap<>();
		body.add("test1", "test1");
		body.add("test2", "test2");
		HttpEntity<MultiValueMap<String, Object>> requestEntity
				= new HttpEntity<>(body, headers);
		ResponseEntity<String> test =restTemplate.postForEntity("/testMultipart", requestEntity, String.class);
		Assertions.assertEquals(200,test.getStatusCodeValue());
		String res = test.getBody();
		Assertions.assertNotNull(res);
		logger.info("received "+res+" from multipart");

	}

}
