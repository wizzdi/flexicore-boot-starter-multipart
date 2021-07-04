package com.wizzdi.flexicore.boot.multipart.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@Extension
@RestController
public class TestMultiPart implements  Plugin {



	@PostMapping(value = "/testMultipart",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String testMultipart(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException, ServletException {
		return multipartHttpServletRequest.getParts().size()+"";

	}
}
