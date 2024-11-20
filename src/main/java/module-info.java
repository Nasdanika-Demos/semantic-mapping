module org.nasdanika.demos.diagrams.mapping {
		
	requires transitive org.nasdanika.emf;
	requires transitive org.nasdanika.models.architecture;
	requires transitive org.nasdanika.models.education;
	requires transitive org.nasdanika.models.family;
	requires transitive org.nasdanika.models.nature;
	
	exports org.nasdanika.demos.diagrams.mapping;
	
	opens org.nasdanika.demos.diagrams.mapping to org.nasdanika.common;
	
}
