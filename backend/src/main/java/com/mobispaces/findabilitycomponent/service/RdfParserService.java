package com.mobispaces.findabilitycomponent.service;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class RdfParserService {
	@Value("${findability.ttlFilesDirectory}")
	private String ttlFilesDirectory;
	private Set<String> extractedDOIs = new HashSet<>();
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    // Updated to exclude seconds
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public String getTtlFilesDirectory() {
		return this.ttlFilesDirectory;
	}	

	public Set<String> parseAllTTLFiles() {
        Set<String> allDOIs = new HashSet<>();
        File folder = new File(ttlFilesDirectory);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".ttl"));

        for (File file : listOfFiles) {
            if (file.isFile()) {
                Map<String, Object> metadata = parseTTL(file.getPath());
                String doi = (String) metadata.get("doi");
                if (doi != null && !"DOI not found".equals(doi)) {
                    allDOIs.add(doi);
                }
            }
        }
        return allDOIs;
    }

	private String cleanDateTime(String dateTime) {
		if (dateTime != null) {
        // Attempt to fix dateTime issues
			dateTime = dateTime.trim().replace(" ", "T");
			if (!dateTime.endsWith("Z")) {
				dateTime += "Z"; // Append 'Z' to indicate UTC if missing
        }
    }
    return dateTime;
}

	private String cleanDuration(String duration) {
		if (duration != null) {
        // Attempt to fix duration issues
			if (!duration.startsWith("P")) {
               duration = "PT" + duration.replace("T", "").replace("S", "S");
        }
    }
		return duration;
}



	private String adjustDurationFormat(String duration) {
		if (duration != null && !duration.startsWith("P")) {
			return "PT" + duration.replace("T", "");
    }
		return duration;
}


	public Map<String, Object> findAndParseTTLByDOI(String targetDOI) {
		File folder = new File(ttlFilesDirectory);
		File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".ttl"));

		for (File file : listOfFiles) {
			if (file.isFile()) {
				String fileNameWithoutExtension = file.getName().replace(".ttl", "");
				Model model = FileManager.get().loadModel(file.getPath(), "TTL");
				String doi = findDOI(model, file.getName()); // Pass file name to findDOI method
				if (targetDOI.equals(doi) || targetDOI.equals(fileNameWithoutExtension)) {
					return parseTTL(file.getPath()); 
            }
        }
    }
    return new HashMap<>(); 
}


    public  Map<String, Object> parseTTL(String filePath) {
        Map<String, Object> metadata = new HashMap<>();
        Model model = FileManager.get().loadModel(filePath, "TTL");
		File file = new File(filePath);
		String fileName = file.getName();
        
        Property startDateProperty = model.createProperty("http://www.w3.org/ns/dcat#startDate");
        Property endDateProperty = model.createProperty("http://www.w3.org/ns/dcat#endDate");
        Property spatialResolutionInMetersProperty = model.createProperty("http://www.w3.org/ns/dcat#spatialResolutionInMeters");
        Property spatialCoverageProperty = model.createProperty("http://www.opengis.net/ont/geosparql#asWKT");
        Property temporalResolutionProperty = model.createProperty("http://www.w3.org/ns/dcat#temporalResolution");

		

        // Temporal resolution formatted
		String temporalResolution = cleanDuration(findProperty(model, temporalResolutionProperty));
		metadata.put("temporalResolution", temporalResolution);
        

        // Parse and format start and end dates without seconds
		String startDate = cleanDateTime(findProperty(model, startDateProperty));

		String endDate = cleanDateTime(findProperty(model, endDateProperty));
		metadata.put("startDate", formatDate(startDate));
		metadata.put("endDate", formatDate(endDate));
        

        // Spatial resolution
        metadata.put("spatialResolutionInMeters", findProperty(model, spatialResolutionInMetersProperty));

        // DOI
        metadata.put("doi", findDOI(model, fileName));

        // Spatial coverage
        metadata.put("spatialCoverage", parseSpatialCoverage(findProperty(model, spatialCoverageProperty)));
		
		String doi = findDOI(model, fileName); // Extract DOI
        if (!"DOI not found".equals(doi)) {
            extractedDOIs.add(doi); // Step 2: Collect DOI
        }
		
		metadata.put("doi", doi);

        return metadata;
		
    }
	


    private String findProperty(Model model, Property property) {
        StmtIterator iter = model.listStatements(new SimpleSelector(null, property, (RDFNode) null));
        if (iter.hasNext()) {
            return iter.nextStatement().getObject().toString().split("\\^\\^")[0];
        }
        return "";
    }

    private String findDOI(Model model, String fileName) { // Accept fileName as a parameter
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			RDFNode object = stmt.getObject();
			if (object.isURIResource() && object.toString().contains("doi.org/")) {
				return object.asResource().getURI().replace("https://doi.org/", "");
        }
    }
    // Return the file name without the .ttl extension as the DOI if no DOI is found
    return fileName.replace(".ttl", "");
}




    private String formatDate(String inputDate) {
		try {
			Date date = inputDateFormat.parse(inputDate); // Assuming inputDateFormat matches the input date format from the TTL file
			return outputDateFormat.format(date); // Use the format with seconds
    } 	catch (ParseException e) {
			e.printStackTrace();
			return "";
    }
}


    private String parseSpatialCoverage(String wkt) {
		String[] parts = wkt.replace("POLYGON((", "").replace("))", "").split(", ");
		double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, minLong = Double.MAX_VALUE, maxLong = Double.MIN_VALUE;
		for (String part : parts) {
			String[] coords = part.split(" ");
			double lat = Double.parseDouble(coords[1]);
			double lon = Double.parseDouble(coords[0]);
			if (lat < minLat) minLat = lat;
			if (lat > maxLat) maxLat = lat;
			if (lon < minLong) minLong = lon;
			if (lon > maxLong) maxLong = lon;
    }
    // Format the string as expected by the frontend
		return String.format(Locale.US, "%f,%f,%f,%f", minLat, maxLat, minLong, maxLong);
}


    
}