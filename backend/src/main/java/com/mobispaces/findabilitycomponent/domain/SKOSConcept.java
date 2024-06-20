package com.mobispaces.findabilitycomponent.domain;

import org.springframework.stereotype.Component;

@Component
public class SKOSConcept {
	

	private String URI;
	
	private String label;
	
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	

}
