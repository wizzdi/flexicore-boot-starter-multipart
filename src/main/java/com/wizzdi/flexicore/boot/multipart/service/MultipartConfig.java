package com.wizzdi.flexicore.boot.multipart.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig  implements WebMvcConfigurer {

	private final MultipartProperties multipartProperties;

	public MultipartConfig(MultipartProperties multipartProperties) {
		this.multipartProperties = multipartProperties;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver(MultipartConfigElement multipartConfigElement) {
 		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		resolver.setMaxUploadSize(multipartConfigElement.getMaxFileSize());
		return resolver;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return multipartProperties.createMultipartConfig();
	}
}
