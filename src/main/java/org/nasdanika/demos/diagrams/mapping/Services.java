package org.nasdanika.demos.diagrams.mapping;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EObject;
import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.common.Invocable.Parameter;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.drawio.Element;
import org.nasdanika.mapping.ContentProvider;
import org.nasdanika.models.architecture.ArchitectureDescriptionElement;
import org.nasdanika.models.architecture.ArchitectureFactory;

/**
 * Static methods invoked from mapping.  
 */
public class Services {
	
	public static ArchitectureDescriptionElement nodeInitializer(
			CapabilityFactory.Loader loader, 
			ProgressMonitor loadingProgressMonitor, 
			byte[] binding,
			String fragment,
			@Parameter(name = "contentProvider") ContentProvider<Element> contentProvider,
			@Parameter(name = "registry") Consumer<BiConsumer<Map<EObject, EObject>,ProgressMonitor>> registry,
			@Parameter(name = "progressMonitor") ProgressMonitor mappingProgressMonitor,
			Element source) {
		ArchitectureDescriptionElement architectureDescriptionElement = ArchitectureFactory.eINSTANCE.createArchitectureDescriptionElement();
		architectureDescriptionElement.setDescription("I was created by a Java initializer");
		return architectureDescriptionElement;				
	}

}
