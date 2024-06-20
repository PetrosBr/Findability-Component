import axios from 'axios'

const AXIOS = axios.create({
  baseURL: `/sae/api`
});


export default {
    createDatasetMetadata(label_value,lang_value,issued_value,modified_value,periodicity_value, 
    		temporalStart_value, temporalEnd_value, temporalResolution_value, spatial_value, 
    		spatialResolution_value,conformsTo_value,landingPage_value,publisher_value,
    		license_value, mediaType_value, byteSize_value, tableName_value,components_value,id_value) {
    	return AXIOS.post(`/dataset`,{label:label_value, 
    		                          language:lang_value,
    		                          issued:issued_value,
    		                          modified:modified_value,
    		                          accrualPeriodicity:periodicity_value,
    		                          temporalStart:temporalStart_value,
    		                          temporalEnd: temporalEnd_value,
    		                          temporalResolution: temporalResolution_value,
    		                          spatial:spatial_value,
    		                          spatialResolution:spatialResolution_value,
    		                          conformsTo:conformsTo_value,
    		                          landingPage:landingPage_value,
    		                          publisher:publisher_value,
    		                          license:license_value,
    		                          mediaType:mediaType_value,
    		                          byteSize:byteSize_value,
    		                          tableName:tableName_value,
    		                          components:components_value,
    		                          identifier:id_value});
    },
    getCodelistContent(codelistId){
    	return AXIOS.get(`/codelist/` + codelistId);    	
    },
	
	getDatasets() {
        return AXIOS.get(`/datasets`);
    },
	
	getMetadataForDOI(doi) {
		return AXIOS.get(`/metadata?doi=${encodeURIComponent(doi)}`);
}


   
}


