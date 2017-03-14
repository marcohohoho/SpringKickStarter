package com.kickstarter._environment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.kickstarter")
public class RootEnvironment extends WebMvcConfigurerAdapter{

	@Bean
	public PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer(){

		// set path for configuration property values
		Resource[] resources = {
				new ClassPathResource("/configs/kickstarter.config.properties")
				//, new ClassPathResource("/configs/db_schema.properties")
		};
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setLocations(resources);
		propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		
		return propertySourcesPlaceholderConfigurer;
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver getMultipartResolver(){
		CommonsMultipartResolver mResolver = new CommonsMultipartResolver();
		mResolver.setMaxUploadSize(5000000);
		return mResolver; 
	}

	/*
	 * DEV NOTE:
	 * 		This has been chosen over the method below it so 
	 * 		we can resolve views automatically to be redirected to default page
	 * 		when a 404 exception occurs
	 * 
	 * 		-Randolf 
	 * */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("staticResources/**").addResourceLocations("/staticResources/")
		.setCachePeriod(0);
		registry.addResourceHandler("bootstrap/**").addResourceLocations("/bootstrap/");
		registry.addResourceHandler("pics/**").addResourceLocations("/pics/");
		registry.addResourceHandler("dashboard/**").addResourceLocations("/dashboard/");
		registry.addResourceHandler("/**");
		
	}
	
}
