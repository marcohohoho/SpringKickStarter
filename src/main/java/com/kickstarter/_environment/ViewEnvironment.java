package com.kickstarter._environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class ViewEnvironment {
	@Value("${thymeleaf.templates.location}")
	private String thymeleafTemplatesLocation;
	@Value("${thymeleaf.templates.mode}")
	private String thymeleafTemplatesMode;
	@Value("${thymeleaf.resolver.viewNames}")
	private String thymeleafResolverViewNames;
	
	@Bean(name ="templateResolver")	
	public ServletContextTemplateResolver getServletTemplateResolver() {
//	public TemplateResolver getServletTemplateResolver() {
//		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix(thymeleafTemplatesLocation);
		templateResolver.setSuffix(thymeleafResolverViewNames);
		templateResolver.setTemplateMode(thymeleafTemplatesMode);
		//TODO: recheck this, doesnt work with spring sec
		templateResolver.setCacheable(false);
		
		return templateResolver;
	}
	
	@Bean(name ="templateEngine")	
	public SpringTemplateEngine getTemplateEngine(){
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		springTemplateEngine.setTemplateResolver(getServletTemplateResolver());
		//springTemplateEngine.setTemplateResolver(getTemplateResolver());
		springTemplateEngine.addDialect(getSpringSecurityDialect());
		return springTemplateEngine;
	}
	
	@Bean(name="viewResolver")
	public ViewResolver getViewResolver(){ 
		//String[] viewNames = thymeleafResolverViewNames.split("\\,");
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(getTemplateEngine());
		viewResolver.setOrder(1);
		//viewResolver.setViewNames(viewNames);
		return viewResolver;
	}
	
	@Bean
	public SpringSecurityDialect getSpringSecurityDialect(){
		return new SpringSecurityDialect();
	}
}
