package com.mobispaces.findabilitycomponent.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mobispaces.findabilitycomponent.configuration.VirtuosoProperties;
import com.mobispaces.findabilitycomponent.domain.Dataset;
import com.mobispaces.findabilitycomponent.domain.SKOSConcept;
import com.mobispaces.findabilitycomponent.util.DatasetUtils;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;




@Component
public class DatasetRepository {
		 
	private static final Logger LOG = LoggerFactory.getLogger(DatasetRepository.class);
	private static int cacheSize=200;
	private static Map <String, List<SKOSConcept>> cache = new LinkedHashMap<String, List<SKOSConcept>>(cacheSize + 1, .75F, true) {
		@Override
        public boolean removeEldestEntry(Map.Entry<String, List<SKOSConcept>> eldest) {
            return size() > cacheSize;
        }
    };
	  
	@Autowired
	VirtuosoProperties vp;
	
	public Dataset insertDataset(Dataset dataset) {
	  LOG.info("Inserting dataset:"+dataset);
	  if(dataset.getUri()==null) {
		  if(dataset.getIdentifier()!=null) {
			  dataset.setUri(DatasetUtils.uriFromID("dataset", dataset.getIdentifier()));
		  }else {
			  dataset.setUri(DatasetUtils.randomURI("dataset"));
		  }		  
	  }
	  String sparql = "INSERT DATA{ GRAPH <"+vp.getDataGraph()+">{"
	  		+ "<"+dataset.getUri()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/dcat#Dataset>.";
	  if(dataset.getLabel()!=null && !dataset.getLabel().equals("")) {
		  sparql+= "<"+dataset.getUri()+"> <http://purl.org/dc/terms/title> \"" +dataset.getLabel()+"\".";
	  }
	  if(dataset.getLanguage()!=null && !dataset.getLanguage().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/language> <" +dataset.getLanguage()+">.";
	  }
	  if(dataset.getIssued()!=null && !dataset.getIssued().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/issued> \"" +dataset.getIssued()+"\"^^<http://www.w3.org/2001/XMLSchema#date>.";;
	  }
	  if(dataset.getModified()!=null && !dataset.getModified().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/modified> \"" +dataset.getModified()+"\"^^<http://www.w3.org/2001/XMLSchema#date>.";
		  						
	  }
	  if(dataset.getAccrualPeriodicity()!=null) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/accrualPeriodicity> <" +dataset.getAccrualPeriodicity()+">.";
	  }
	  if(dataset.getTemporalStart()!=null && dataset.getTemporalEnd()!=null && !dataset.getTemporalStart().equals("") && !dataset.getTemporalEnd().equals("")) {
		  URI temporalRandom=DatasetUtils.randomURI("temporal");
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/temporal> <" +temporalRandom+">."
				+ "<" +temporalRandom+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/dc/terms/PeriodOfTime>." 
		  		+ "<"+temporalRandom+"> <http://www.w3.org/ns/dcat#startDate> \"" + dataset.getTemporalStart()+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>."
		  		+ "<"+temporalRandom+"> <http://www.w3.org/ns/dcat#endDate> \""+dataset.getTemporalEnd()+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>.";
	  }
	  if(dataset.getTemporalResolution()!=null && !dataset.getTemporalResolution().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://www.w3.org/ns/dcat#temporalResolution> \"" +dataset.getTemporalResolution()+"\".";
	  }
	  if(dataset.getSpatial()!=null && !dataset.getSpatial().equals("")) {
		  if(dataset.getSpatial().contains("POLYGON(")) {
			  URI spatialRandom=DatasetUtils.randomURI("spatial");
			  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/spatial> <" +spatialRandom+">."					  
					  +"<"+spatialRandom+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/dc/terms/Location>."
					  +"<"+spatialRandom+"> <http://www.w3.org/ns/locn#geometry> "+dataset.getSpatial()+"^^<http://www.opengis.net/ont/geosparql#asWKT>.";
			  }else {
				  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/spatial> <" +dataset.getSpatial()+">.";
			  }		  
	  }
	  
	  if(dataset.getSpatialResolution()!=null && !dataset.getSpatialResolution().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://www.w3.org/ns/dcat#spatialResolutionInMeters> " +dataset.getSpatialResolution()+" .";
	  }
	  if(dataset.getConformsTo()!=null && !dataset.getConformsTo().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/conformsTo> <" +dataset.getConformsTo()+">.";
	  }
	  if(dataset.getLandingPage()!=null && !dataset.getLandingPage().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://www.w3.org/ns/dcat#landingPage> <" +dataset.getLandingPage()+">.";
	  }
	  if(dataset.getPublisher()!=null && !dataset.getPublisher().equals("")) {
		  sparql+="<"+dataset.getUri()+"> <http://purl.org/dc/terms/publisher> <" +dataset.getPublisher()+">.";
	  }
	  
	  //If there are info related to the dcat:Distribution
	  if((dataset.getLicense()!=null && !dataset.getLicense().equals("")) ||
		  (dataset.getMediaType()!=null && !dataset.getMediaType().equals("")) ||
	      (dataset.getByteSize()!=null && !dataset.getByteSize().equals(""))||
	      (dataset.getTableName()!=null && !dataset.getTableName().equals(""))){
		  
		  URI distributionURI=DatasetUtils.randomURI("distribution");
		  
		  sparql+="<"+dataset.getUri()+"> <http://www.w3.org/ns/dcat#distribution> <"+distributionURI+">."
		  		+ "<"+distributionURI+"> a <http://www.w3.org/ns/dcat#Distribution>.";
		  
		  if(dataset.getLicense()!=null && !dataset.getLicense().equals("")) {
			  sparql+="<"+distributionURI+"> <http://purl.org/dc/terms/license> <" +dataset.getLicense()+">.";
		  } 
			  
		  if(dataset.getMediaType()!=null && !dataset.getMediaType().equals("")) {
			  sparql+="<"+distributionURI+"> <http://www.w3.org/ns/dcat#mediaType> <" +dataset.getMediaType()+">.";
		  }
				  
		  if(dataset.getByteSize()!=null && !dataset.getByteSize().equals("")) {
			  sparql+="<"+distributionURI+"> <http://www.w3.org/ns/dcat#byteSize> \"" +dataset.getByteSize()+"\".";
		  }	
		  
		  //If there are info about the DB
		  if(dataset.getTableName()!=null && !dataset.getTableName().equals("")) {
			  String tableURI="https://w3id.org/mobispaces/table/"+dataset.getTableName();
			  sparql+= "<"+distributionURI+"> <https://w3id.org/mobispaces/model#accessDatabase> <"+vp.getdbURI()+">."
			  		+ "<"+vp.getdbURI()+"> <https://w3id.org/mobispaces/model#accessTable> <"+tableURI+">."
			  		+ "<"+tableURI+"> a <https://w3id.org/mobispaces/model#Table> ."
			  		+ "<"+tableURI+"> <https://w3id.org/mobispaces/model#tableName> \""+dataset.getTableName()+"\"."
			  		+ "<"+tableURI+"> <http://purl.org/dc/terms/subject> <"+dataset.getUri()+">.";		    
		  }	
		  
		  //if there info about the data structure
		  if(dataset.getComponents()!=null && !dataset.getComponents().isEmpty()) {
			  URI dsdURI=DatasetUtils.randomURI("dsd");
			  sparql+= "<"+dsdURI+"> a <http://purl.org/linked-data/cube#DataStructureDefinition>."
			  		+ "<"+dataset.getUri()+"> <http://purl.org/linked-data/cube#structure> <"+dsdURI+">.";
			  for(String component:dataset.getComponents()) {
				  URI componentSpecificationURI=DatasetUtils.randomURI("componentSpecification");  
				  sparql+= "<"+componentSpecificationURI+"> a <http://purl.org/linked-data/cube#ComponentSpecification>."
				  		+ "<"+dsdURI+"> <http://purl.org/linked-data/cube#component> <"+componentSpecificationURI+">.";
				  if(SPARQL_URIs.MEASURE_COMPONENTS.contains(component)) { //It is a measure
					  sparql+="<"+componentSpecificationURI+"> <http://purl.org/linked-data/cube#measure> "
								+ "<https://w3id.org/mobispaces/component/id/"+component+">.";
				  }else {   //It is a dimension
					  sparql+="<"+componentSpecificationURI+"> <http://purl.org/linked-data/cube#dimension> "
								+ "<https://w3id.org/mobispaces/component/id/"+component+">.";
				  }
				  		
			  }
		  }
	  }
	  
	  sparql+="}}";
	  LOG.info(sparql);
	  LOG.info(dataset.getComponents().toString());
	  VirtGraph set = new VirtGraph ("jdbc:virtuoso://"+vp.getEndpoint()+":"+vp.getPort(), vp.getUser(), vp.getPassword());
	  VirtuosoUpdateRequest update = VirtuosoUpdateFactory.create(sparql,set);
	  update.exec();	   
	  return dataset;
  }
  
  public List<SKOSConcept> getCodelistContent(String codelist) {
	  
	  List<SKOSConcept> list=cache.get(codelist);
	  if(list==null) {
		  list=new ArrayList<SKOSConcept>();
		  String codelistURI=SPARQL_URIs.CODELISTS.get(codelist);
		  if(codelistURI!=null || codelist.equals(SPARQL_URIs.AGENT)) {
			  LOG.info("Retrieving codelist:"+codelist);
			  LOG.info("DATA GRAPH:"+vp.getDataGraph());
			  String query="PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
			  		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			  		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			  		+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			  		+ "select ?concept ?label ";
			  if(codelist.equals(SPARQL_URIs.ACCRUAL_PERIODICITY)) {
				  query+= "FROM  <"+vp.getfrequencyCodelistGraph()+">";
			  }else if(codelist.equals(SPARQL_URIs.COUNTRY)) {
				  query+= "FROM  <"+vp.getcountryCodelistGraph()+">";
			  }else if(codelist.equals(SPARQL_URIs.LANGUAGE)) {
				  query+= "FROM  <"+vp.getlanguageCodelistGraph()+">";
			  }else if(codelist.equals(SPARQL_URIs.AGENT)) {
				  query+= "FROM  <"+vp.getagentCodelistGraph()+">";
			  }else if(codelist.equals(SPARQL_URIs.LICENSE)) {
				  query+= "FROM  <"+vp.getlicenseCodelistGraph()+">";
			  }else if(codelist.equals(SPARQL_URIs.MEDIA_TYPE)) {
				  query+= "FROM  <"+vp.getmediaTypeCodelistGraph()+">";
			  }
			  //Agents are not in a skos code list.
			  if(codelist.equals(SPARQL_URIs.AGENT)) {
				  query+= "where{?concept rdf:type foaf:Organization."
					  		+ "?concept rdfs:label ?label.\n" 
					  		+ "FILTER langMatches( lang(?label), \"en\" ) }"
					  		+ "ORDER BY ?label";			  
			  }else {
				  query+= "where{?concept skos:inScheme "+codelistURI+"."
					  		+ "?concept skos:prefLabel ?label.\n" 
					  		+ "FILTER langMatches( lang(?label), \"en\" ) }"
					  		+ "ORDER BY ?label";
			  }
			  VirtGraph set = new VirtGraph ("jdbc:virtuoso://"+vp.getEndpoint()+":"+vp.getPort(), vp.getUser(), vp.getPassword());
		
			  Query sparql = QueryFactory.create(query);
			  VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);  
			  
			  ResultSet results = vqe.execSelect();		 
			  while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
				SKOSConcept c=new SKOSConcept();
				c.setURI(result.get("concept").toString());
				c.setLabel(result.get("label").asLiteral().getString());
				list.add(c);
			  }
			  cache.put(codelist, list);
		  }else {
			  LOG.info("Codelist not found:"+codelist);
		  }
	  }
	  return list; 	
  }
  
  public List<SKOSConcept> getAgents(String codelist) {
  	  
	  List<SKOSConcept> list=new ArrayList<SKOSConcept>();
	  String codelistURI=SPARQL_URIs.CODELISTS.get(codelist);
	  if(codelistURI!=null) {
		  LOG.info("Retrieving codelist:"+codelist);
		  String query="PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
		  		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
		  		+ "select ?concept ?label ";
		  if(codelist.equals(SPARQL_URIs.ACCRUAL_PERIODICITY)) {
			  query+= "FROM  <"+vp.getfrequencyCodelistGraph()+">";
		  }else if(codelist.equals(SPARQL_URIs.COUNTRY)) {
			  query+= "FROM  <"+vp.getcountryCodelistGraph()+">";
		  }else if(codelist.equals(SPARQL_URIs.LANGUAGE)) {
			  query+= "FROM  <"+vp.getlanguageCodelistGraph()+">";
		  }
		  query+= "where{?concept skos:inScheme "+codelistURI+"."
		  		+ "?concept skos:prefLabel ?label.\n" 
		  		+ "FILTER langMatches( lang(?label), \"en\" ) }"
		  		+ "ORDER BY ?label";	  
		  VirtGraph set = new VirtGraph ("jdbc:virtuoso://"+vp.getEndpoint()+":"+vp.getPort(), vp.getUser(), vp.getPassword());
	
		  Query sparql = QueryFactory.create(query);
		  VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);  
		  
		  ResultSet results = vqe.execSelect();		 
		  while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			SKOSConcept c=new SKOSConcept();
			c.setURI(result.get("concept").toString());
			c.setLabel(result.get("label").asLiteral().getString());
			list.add(c);
		  }
	  }else {
		  LOG.info("Codelist not found:"+codelist);
	  }
	  return list; 	
  }
  
}
