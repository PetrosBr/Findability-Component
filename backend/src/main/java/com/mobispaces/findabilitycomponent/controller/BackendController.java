package com.mobispaces.findabilitycomponent.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mobispaces.findabilitycomponent.service.RdfParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mobispaces.findabilitycomponent.domain.Dataset;
import com.mobispaces.findabilitycomponent.domain.SKOSConcept;
import com.mobispaces.findabilitycomponent.exception.CodelistNotFoundException;
import com.mobispaces.findabilitycomponent.repository.DatasetRepository;
import com.mobispaces.findabilitycomponent.util.DatasetUtils;
import com.mobispaces.findabilitycomponent.service.RdfParserService;
import java.util.Set;
import java.util.Map;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController()
@RequestMapping("/api")
public class BackendController {
		
	@Autowired
	DatasetRepository dtr;
	
    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);
	
	@Autowired
    private RdfParserService rdfParserService;
	
	
	
	@PostMapping("/upload-ttl")
	public ResponseEntity<String> uploadTTLFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("No file was uploaded.");
    }

		try {
			String directory = rdfParserService.getTtlFilesDirectory();
        // Ensure directory ends with a file separator
			if (!directory.endsWith(File.separator)) {
				directory += File.separator;
        }
			Path path = Paths.get(directory + file.getOriginalFilename());

        // Logging for debugging
			LOG.info("Attempting to save file to: {}", path);

			Files.write(path, file.getBytes());

			LOG.info("File uploaded successfully: {}", path);
			return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
    } catch (IOException e) {
        LOG.error("Could not store the file. Error: ", e);
        return ResponseEntity.status(500).body("Could not store the file. Error: " + e.getMessage());
    }
}

            
    @PostMapping(path = "/dataset")
    @ResponseStatus(HttpStatus.CREATED)
    public URI addNewDataset (@RequestBody Dataset dataset) {    	   	
   		if(dataset.getIdentifier()!=null) {
   			  dataset.setUri(DatasetUtils.uriFromID("dataset", dataset.getIdentifier()));
   		}else {
   			  dataset.setUri(DatasetUtils.randomURI("dataset"));   		
   		}    	    	
    	Dataset insertedDataset= dtr.insertDataset(dataset);
	    LOG.info(insertedDataset.getUri() + " successfully saved into DB");
	    return insertedDataset.getUri();
    }

    @GetMapping(path = "/codelist/{id}")
    public List<SKOSConcept> getCodelist(@PathVariable("id") String id) {
        List<SKOSConcept> list=dtr.getCodelistContent(id);
        if (list.size()>0) {
        	return list;
        }else {
        	throw new CodelistNotFoundException("The codelist with the id " + id + " couldn't be found.");
        }
    }
	
	@GetMapping("/datasets")
    public ResponseEntity<Set<String>> getDatasets() {
        return ResponseEntity.ok(rdfParserService.parseAllTTLFiles());
    }




	@GetMapping("/metadata")
	public ResponseEntity<?> getMetadataForDOI(@RequestParam String doi) {
		LOG.info("Received request for DOI: " + doi); // Debugging log
		Map<String, Object> metadata = rdfParserService.findAndParseTTLByDOI(doi);
		if (metadata == null || metadata.isEmpty()) {
			return ResponseEntity.notFound().build();
    } 	else {
			return ResponseEntity.ok(metadata);
    }
}





    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
    public String redirectApi() {
        LOG.info("URL entered directly into the Browser, so we need to redirect...");
        return "forward:/";
    }

}