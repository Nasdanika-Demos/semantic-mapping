package org.nasdanika.demos.diagrams.mapping.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Test;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.capability.ServiceCapabilityFactory.Requirement;
import org.nasdanika.capability.emf.ResourceSetRequirement;
import org.nasdanika.common.Context;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.common.Transformer;
import org.nasdanika.demos.diagrams.mapping.Configurators;
import org.nasdanika.drawio.ConnectionBase;
import org.nasdanika.drawio.Document;
import org.nasdanika.drawio.Element;
import org.nasdanika.drawio.emf.DrawioContentProvider;
import org.nasdanika.emf.ConfigurationLoadingDrawioFactory;
import org.nasdanika.html.Producer;
import org.nasdanika.html.producer.HtmlGenerator;
import org.nasdanika.mapping.AbstractMappingFactory;
import org.nasdanika.mapping.ContentProvider;
import org.nasdanika.mapping.ReflectiveContributor;
import org.xml.sax.SAXException;

public class TestDiagramMapping {

	@Test
	public void testMapping() throws IOException {
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Requirement<ResourceSetRequirement, ResourceSet> requirement = ServiceCapabilityFactory.createRequirement(ResourceSet.class);		
		ResourceSet resourceSet = capabilityLoader.loadOne(requirement, progressMonitor);
		
		class ContributorAdapter extends AdapterImpl implements AbstractMappingFactory.Contributor<Object, EObject> {

			@Override
			public boolean canHandle(Object source, EObject target) {
				System.out.println("*** " + source);
				return false;
			}
			
		}		
		
		resourceSet.eAdapters().add(new ContributorAdapter());
		
		File diagramFile = new File("diagram.drawio").getCanonicalFile();
		Resource resource = resourceSet.getResource(URI.createFileURI(diagramFile.getAbsolutePath()), true);		
		
		// Saving for manual inspection
		URI xmiURI = URI.createFileURI(new File("target/mapping.xml").getAbsolutePath());
		Resource xmiResource = resourceSet.createResource(xmiURI);
		xmiResource.getContents().addAll(EcoreUtil.copyAll(resource.getContents()));
		xmiResource.save(null);		
		
		// Assertions
		assertEquals(5, resource.getContents().size());		
		
		// Testing saving with 
	}
	
	public static final String MAPPING_PROPERTY = "mapping";
	public static final String MAPPING_REF_PROPERTY = "mapping-ref";
	
	
	@Test
	public void testReflectiveConfigurator() throws IOException, ParserConfigurationException, SAXException {
		ReflectiveContributor<Element, EObject> rc = new ReflectiveContributor<>(List.of(new Configurators()));  
		Document document = Document.load(new File("bootstrap.drawio"));		
		
		document.accept(System.out::println);
		
		ConnectionBase connectionBase = ConnectionBase.SOURCE;
		ContentProvider<Element> contentProvider = new DrawioContentProvider(
				document, 
				Context.BASE_URI_PROPERTY, 
				MAPPING_PROPERTY, 
				MAPPING_REF_PROPERTY, 
				connectionBase);
		
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Requirement<ResourceSetRequirement, ResourceSet> requirement = ServiceCapabilityFactory.createRequirement(ResourceSet.class);		
		ResourceSet resourceSet = capabilityLoader.loadOne(requirement, progressMonitor);
				
		ConfigurationLoadingDrawioFactory<EObject> drawioFactory = new ConfigurationLoadingDrawioFactory<EObject>(
				contentProvider, 
				capabilityLoader, 
				resourceSet, 
				progressMonitor) {

					@Override
					protected EObject getByRefId(Element obj, String refId, int pass, Map<Element, EObject> registry) {
						return null;
					}
			
		};
		
		drawioFactory.getContributors().add(rc);
		
		Transformer<Element,EObject> modelFactory = new Transformer<>(drawioFactory);
		List<Element> documentElements = new ArrayList<>();
		Consumer<Element> visitor = documentElements::add;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Consumer<org.nasdanika.graph.Element> traverser = (Consumer) org.nasdanika.drawio.Util.traverser(visitor, connectionBase);
		document.accept(traverser);
		
		Map<Element, EObject> modelElements = modelFactory.transform(documentElements, false, progressMonitor);
		
		List<EObject> cnt = new ArrayList<>();
		modelElements.values()
			.stream()
			.distinct()
			.filter(modelElement -> modelElement != null && modelElement.eContainer() == null)
			.forEach(cnt::add);
		
		// Assertions
		assertEquals(1, cnt.size());		
		
		// Saving for manual inspection
		URI xmiURI = URI.createFileURI(new File("target/bootstrap.xml").getAbsolutePath());
		Resource xmiResource = resourceSet.createResource(xmiURI);
		xmiResource.getContents().addAll(cnt);
		xmiResource.save(null);
		
		HtmlGenerator htmlGenerator = HtmlGenerator.load(
				Context.EMPTY_CONTEXT, 
				null, 
				progressMonitor);
				
		Producer<Object> processor = htmlGenerator.createProducer(cnt.get(0), progressMonitor);
		Object result = processor.produce(0);
		
		Files.writeString(Path.of("target", "bootstrap.html"), (String) result);
	}
	
		
}
