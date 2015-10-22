package com.fccs.es_demo.mapping;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class MappingTest {

	@Test
	public void mappingTest() throws IOException {
		Client client = ClientTemplate.getLocalInstance();
		client.admin().indices().prepareCreate("mapping_test_index").execute().actionGet();
		XContentBuilder mapping = XContentFactory.jsonBuilder()
			.startObject()  
	        	.startObject("mappingTestType")  
		        	.startObject("properties")         
			          .startObject("title").field("type", "string").field("store", "yes").endObject()    
			          .startObject("description").field("type", "string").field("index", "not_analyzed").endObject()  
			          .startObject("price").field("type", "double").endObject()  
			          .startObject("onSale").field("type", "boolean").endObject()  
			          .startObject("type").field("type", "integer").endObject()  
			          .startObject("createDate").field("type", "date").endObject()                 
			        .endObject()  
	       .endObject()  
	     .endObject();
		PutMappingRequest mappingRequest = Requests.putMappingRequest("mapping_test_index").type("mappingTestType").source(mapping);
		client.admin().indices().putMapping(mappingRequest).actionGet();
		
	}
	
	
}
