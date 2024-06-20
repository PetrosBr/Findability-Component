package com.mobispaces.findabilitycomponent.domain;

import java.net.URI;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dataset{

	@Id
    private URI uri;
	
	private String identifier;
   
	private String label;
    private String conformsTo;
    private String language;
    private String publisher;
    private String modified;
    private String issued;
    private String accrualPeriodicity;
    
	private String landingPage;
    
    private String spatial;
    private String spatialResolution;
    
    private String temporalStart;
    private String temporalEnd;
    private String temporalResolution;
    
    private String license;
    private String mediaType;
    private String byteSize;
    
    //Database properties
    private String tableName;
    
    //Structure properties
    @ElementCollection
    private List<String> components;
    
  	public Dataset() {
		super();
	}
               
    public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getIdentifier() {
			return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public String getAccrualPeriodicity() {
		return accrualPeriodicity;
	}

	public void setAccrualPeriodicity(String accrualPeriodicity) {
		this.accrualPeriodicity = accrualPeriodicity;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getSpatial() {
		return spatial;
	}

	public void setSpatial(String spatial) {
		this.spatial = spatial;
	}

	public String getSpatialResolution() {
		return spatialResolution;
	}

	public void setSpatialResolution(String spatialResolution) {
		this.spatialResolution = spatialResolution;
	}

	public String getTemporalStart() {
		return temporalStart;
	}

	public void setTemporalStart(String temporalStart) {
		this.temporalStart = temporalStart;
	}
	
	public String getTemporalEnd() {
		return temporalEnd;
	}

	public void setTemporalEnd(String temporalEnd) {
		this.temporalEnd = temporalEnd;
	}

	public String getTemporalResolution() {
		return temporalResolution;
	}

	public void setTemporalResolution(String temporalResolution) {
		this.temporalResolution = temporalResolution;
	}
	
	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getByteSize() {
		return byteSize;
	}

	public void setByteSize(String byteSize) {
		this.byteSize = byteSize;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public List<String> getComponents() {
		return components;
	}

	public void setComponents(List<String> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "Dataset [uri=" + uri + ", label=" + label + ", conformsTo=" + conformsTo + ", language=" + language
				+ ", publisher=" + publisher + ", modified=" + modified + ", issued=" + issued + ", accrualPeriodicity="
				+ accrualPeriodicity + ", landingPage=" + landingPage + ", spatial=" + spatial
				+ ", spatialResolutionInMeters=" + spatialResolution + ", temporalStart=" + temporalStart
				+ ", temporalEnd=" + temporalEnd+ ", temporalResolution=" + temporalResolution 
				+ ", license=" + license +", mediaType=" + mediaType + ", byteSize=" + byteSize
				+ ", tableName=" + tableName+ ", components=" + components+ ", identifier="+identifier+"]";
	}  
}
