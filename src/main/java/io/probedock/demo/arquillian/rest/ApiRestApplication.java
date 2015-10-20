package io.probedock.demo.arquillian.rest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.FilterBuilder;

/**
 * Rest application for the API
 * 
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
@ApplicationPath("api")
public class ApiRestApplication extends Application {
	private final Set<Class<?>> classes;

	public ApiRestApplication() {
		classes = new HashSet<>();

		// Scan every packages to get @Path annotated classes
		for (String pckg : new String[] { getClass().getPackage().getName() }) {
			classes.addAll(new Reflections(
				ClasspathHelper.forPackage(pckg), new TypeAnnotationsScanner(), new FilterBuilder().includePackage(pckg)).
				getTypesAnnotatedWith(Path.class));
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
