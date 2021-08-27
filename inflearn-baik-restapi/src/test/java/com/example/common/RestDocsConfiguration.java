package com.example.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;

import com.fasterxml.jackson.core.PrettyPrinter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocsConfiguration {

	@Bean
	public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
		
		return configurer -> configurer.operationPreprocessors()
				.withRequestDefaults(prettyPrint())
				.withResponseDefaults(prettyPrint())
				;
//		return new RestDocsMockMvcConfigurationCustomizer() {
//			
//			@Override
//			public void customize(MockMvcRestDocumentationConfigurer configurer) {
//				
//				configurer.operationPreprocessors()
//					.withRequestDefaults(prettyPrint())
//					.withResponseDefaults(prettyPrint())
//				;
//			}
//		};
	}
	
}
