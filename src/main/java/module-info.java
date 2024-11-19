module org.nasdanika.demos.diagrams.mapping {
		
	requires transitive org.nasdanika.emf;
	requires transitive org.nasdanika.models.architecture;
	requires transitive org.nasdanika.models.education;
	
	exports org.nasdanika.demos.diagrams.mapping;
	
	opens org.nasdanika.demos.diagrams.mapping to org.nasdanika.common;
	
}
