package org.nasdanika.demos.diagrams.mapping;

import org.nasdanika.drawio.Node;
import org.nasdanika.html.bootstrap.Color;
import org.nasdanika.mapping.ReflectiveContributor.Configurator;
import org.nasdanika.mapping.ReflectiveContributor.Initializer;
import org.nasdanika.models.bootstrap.Appearance;
import org.nasdanika.models.bootstrap.BootstrapFactory;
import org.nasdanika.models.bootstrap.Container;
import org.nasdanika.models.bootstrap.Page;

/**
 * Contains configurator methods for semantic mapping of diagrams
 */
public class Configurators {
	
	@Configurator("id == 'body-root-container'")
	public void configure(Node source, Container target) {		
		Appearance appearance = BootstrapFactory.eINSTANCE.createAppearance();		
		target.setAppearance(appearance);
		appearance.setBackground(Color.PRIMARY);
		
		System.out.println("~~~ " + source + " -> " + target);		
	}
		
	@Initializer(value ="id == 'page'", priority = -10)
	public void initialize(Node source, Page target) {				
		System.out.println("=== " + source + " -> " + target);		
	}
	
}
