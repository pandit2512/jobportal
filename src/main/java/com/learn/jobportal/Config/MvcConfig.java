package com.learn.jobportal.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//this config class will map requests for /photos to serve files from a directory on our file System
public class MvcConfig implements WebMvcConfigurer {

	private static final String UPLOAD_DIR = "photos";

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
	exposeDirectory(UPLOAD_DIR,registry);
}

	private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
		
		Path path=Paths.get(uploadDir);
		registry.addResourceHandler( "/"+ uploadDir + "/**").addResourceLocations("file:"+path.toAbsolutePath()+"/");
	}
}


