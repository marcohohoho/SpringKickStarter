package com.kickstarter;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Marco
 *
 */
public class Initializer implements WebApplicationInitializer{
	
	/**
	 * Configures the given ServletContext with any 
	 * servlets, filters, listeners context-params 
	 * and attributes necessary for initializing this 
	 * web application.
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		/**
		 * create root spring application context 
		 * and set location of configuration file(s)
		 */
		final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("com.kickstarter");

		/**
		 * optionals
		 * ContextLoaderListener - start root WebApplicationContext
		 * RequestContextListener - 3rd party servlets
		 */
		ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
		RequestContextListener requestContextListener = new RequestContextListener();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		
		servletContext.addListener(contextLoaderListener);
		servletContext.addListener(requestContextListener);
	
		final FilterRegistration.Dynamic dynamic = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
	
		dynamic.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		dynamic.setInitParameter("encoding", "UTF-8");
		dynamic.setInitParameter("forceEncoding", "true" );
	
		/**
		 * Create Dispatcher Servlet / Front Controller
		 * 
		 * Added this instead of using new DispatcherServlet(context)
		 * to enable more customizations
		 */
		DispatcherServlet customDispatcherServlet = new DispatcherServlet(context);
		customDispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	
		final Dynamic dispatcher = servletContext.addServlet("Kickstarter DispatcherServlet", customDispatcherServlet);
		dispatcher.addMapping("/");
		dispatcher.setLoadOnStartup(1);
		// for multi-threading...
		dispatcher.setAsyncSupported(true);
	
		// LOG.info("SMedia Servlet initializing");
	}

}
