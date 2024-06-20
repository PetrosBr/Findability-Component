package com.mobispaces.findabilitycomponent.repository;

import java.util.List;
import java.util.Map;

import com.github.jsonldjava.shaded.com.google.common.collect.ImmutableMap;

public final class SPARQL_URIs {
		
	 public static final String ACCRUAL_PERIODICITY="ACCRUAL_PERIODICITY";
	 public static final String LANGUAGE="LANGUAGE";
	 public static final String COUNTRY="COUNTRY";
	 public static final String AGENT="AGENT";
	 public static final String LICENSE="LICENSE";
	 public static final String MEDIA_TYPE="MEDIA_TYPE";
	
	 public static final Map<String, String> CODELISTS = ImmutableMap.of(
			  ACCRUAL_PERIODICITY, "<http://purl.org/cld/terms/Frequency>",
		      LANGUAGE, "<http://publications.europa.eu/resource/authority/language>",
		      COUNTRY, "<http://publications.europa.eu/resource/authority/country>",
		      LICENSE, "<http://publications.europa.eu/resource/authority/licence>",
 		      MEDIA_TYPE,"<http://publications.europa.eu/resource/authority/file-type>");
	 
	 public static final List<String> MEASURE_COMPONENTS = List.of(
			 
			 );
}