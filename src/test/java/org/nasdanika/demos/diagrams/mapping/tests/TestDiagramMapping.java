package org.nasdanika.demos.diagrams.mapping.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.groovy.groovysh.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.capability.ServiceCapabilityFactory.Requirement;
import org.nasdanika.capability.emf.ResourceSetRequirement;
import org.nasdanika.capability.requirements.DiagramRequirement;
import org.nasdanika.capability.requirements.URIInvocableRequirement;
import org.nasdanika.common.Context;
import org.nasdanika.common.Invocable;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.drawio.Document;
import org.nasdanika.drawio.processor.ElementInvocableFactory;
import org.nasdanika.graph.processor.NodeProcessorInfo;

import groovy.lang.Binding;

public class TestDiagramMapping {

	@Test
	public void testMapping() throws IOException {
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Requirement<ResourceSetRequirement, ResourceSet> requirement = ServiceCapabilityFactory.createRequirement(ResourceSet.class);		
		ResourceSet resourceSet = capabilityLoader.loadOne(requirement, progressMonitor);
		File diagramFile = new File("diagram.drawio").getCanonicalFile();
		Resource resource = resourceSet.getResource(URI.createFileURI(diagramFile.getAbsolutePath()), true);		
		assertEquals(1, resource.getContents().size()); // Single root
		EObject root = resource.getContents().get(0);	
		
		// Saving for manual inspection
		URI xmiURI = URI.createFileURI(new File("target/mapping.xml").getAbsolutePath());
		Resource xmiResource = resourceSet.createResource(xmiURI);
		xmiResource.getContents().add(EcoreUtil.copy(root));
		xmiResource.save(null);		
		
		// Assertions
		
		
	}
		
}
