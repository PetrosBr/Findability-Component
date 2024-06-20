package com.mobispaces.findabilitycomponent.repository;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobispaces.findabilitycomponent.domain.Dataset;
import com.mobispaces.findabilitycomponent.util.DatasetUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DatasetRepositoryTest {	
	  
	   	@Autowired
	    private TestEntityManager entityManager;
	
	    @Autowired
	    private DatasetRepositoryJPA datasetRepo;
	    
	    private Dataset d1=new Dataset();
	    private Dataset d2= new Dataset();
	    private String label1="dataset1";
	    private String label2="dataset2";
	    
	    private String lang1="english";
	    private String lang2="greek";
	    	    		    
	    @Before
	    public void fillSomeDataIntoOurDb() {
	        // Add new Datasets to Database
	    	d1.setUri(DatasetUtils.randomURI("dataset"));
	    	d1.setLabel(label1);
	    	d1.setLanguage(lang1);
	    	
	    	d2.setUri(DatasetUtils.randomURI("dataset"));
	    	d2.setLabel(label2);
	    	d2.setLanguage(lang2);
	    	
	    	entityManager.persist(d1);
		    entityManager.persist(d2);	    	
	    }
	    
	    
	    @Test
	    public void testFindLabel() throws Exception {
	        // Search for specific Dataset in Database according to label
	        List<Dataset> datasetsWithLabeldataset1 = datasetRepo.findByLabel(label1);
	        assertThat(datasetsWithLabeldataset1, contains(d1));
	    }


	    @Test
	    public void testFindByLanguage() throws Exception {
	        // Search for specific Dataset in Database according to language
	    	 List<Dataset> datasetsWithLanguagegreek = datasetRepo.findByLanguage(lang2);
		     assertThat(datasetsWithLanguagegreek, contains(d2));	    	
	    }
}