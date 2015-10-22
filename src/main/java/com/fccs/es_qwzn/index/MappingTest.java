package com.fccs.es_qwzn.index;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class MappingTest {

	/*
	 * 创建index，同时设置映射和分析，没有配置分词的字段为es标准分词器
	 * GET /missxu/_mapping
	 */
	@Test
	public void simpleMapping() throws IOException {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareCreate("missxu").execute().actionGet();
		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()  
	        		.startObject("user")  //type
		        		.startObject("properties")         
			        		.startObject("name").field("type", "string").field("store", "yes").field("indexAnalyzer", "standard").field("searchAnalyzer", "english").endObject()    
			        		.startObject("high").field("type", "double").endObject()  
			        		.startObject("gender").field("type", "boolean").endObject()  
			        		.startObject("age").field("type", "integer").endObject()  
			        		.startObject("birthday").field("type", "date").endObject()                 
			        		.startObject("description").field("type", "string").field("index", "not_analyzed").endObject()  
		        		.endObject()  
		        	.endObject()  
		        .endObject();
		client.admin().indices().preparePutMapping("missxu").setType("user").setSource(mapping).execute().actionGet();
		client.close();
	}
	
	/*
	 * 复合类型(object)的映射
	 */
	@Test
	public void objectMapping() throws IOException {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareCreate("missxu2").execute().actionGet();
		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()  
	        		.startObject("user2")  //type
		        		.startObject("properties")         
			        		.startObject("name").field("type", "string").field("store", "yes").field("indexAnalyzer", "standard").field("searchAnalyzer", "english").endObject()    
			        		.startObject("high").field("type", "double").endObject()  
			        		.startObject("gender").field("type", "boolean").endObject()  
			        		.startObject("age").field("type", "integer").endObject()  
			        		.startObject("birthday").field("type", "date").endObject()                 
			        		.startObject("description").field("type", "string").field("index", "not_analyzed").endObject()  
			        		.startObject("address")
			        			.field("type", "object")
			        			.startObject("properties")
			        				.startObject("province").field("type", "string").endObject()
			        				.startObject("city").field("type", "string").endObject()
			        			.endObject()
			        		.endObject()
		        		.endObject()  
		        	.endObject()  
		        .endObject();
		client.admin().indices().preparePutMapping("missxu2").setType("user2").setSource(mapping).execute().actionGet();
		client.close();
	}
	
	
	/*
	 * 复合类型(array)的映射
	 */
	@Test
	public void arrayMapping() throws IOException {

	}
	
	/*
	 * 删除index的mapping设置信息
	 */
	@Test
	public void deleteMapping() throws IOException {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareDeleteMapping("missxu").setType("user").execute().actionGet();
		client.close();
	}
	
	/*
	 * 获取missxu的user类型的mapping信息
	 * 用river更新数据后，就会报异常
	 */
	@Test
	public void getMapping() {
		Client client = ClientTemplate.getClient();
		ImmutableOpenMap<String,MappingMetaData> mappings = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices().get("missxu").getMappings();
		MappingMetaData mappingMetaData = mappings.get("user");
		System.out.println(mappingMetaData.source());
		client.close();
	}
	
	
}
