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
		System.out.println(mapping.string());
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
		System.out.println(mapping.string());
		client.admin().indices().preparePutMapping("missxu2").setType("user2").setSource(mapping).execute().actionGet();
		client.close();
	}
	
	
	/*
	 * 复合类型(array)的映射
	 * 		参考org.elasticsearch.index.mapper.Mapper
	 * 		并没有ArrayFieldMapper
	 * 但是通过 GET /missxu/_mapping 来看，object和array的json是一样的
	 * 所以，只要在初始化数据的时候，使用startArry 就能是array类型了
	 */
	@Test
	public void arrayMapping() throws IOException {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareCreate("floor").execute().actionGet();
		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()  
	        		.startObject("position")  //type
		        		.startObject("properties")         
			        		.startObject("name").field("type", "string").field("store", "yes").field("indexAnalyzer", "standard").field("searchAnalyzer", "english").endObject()    
			        		.startObject("location").field("type", "integer").endObject()
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
		System.out.println(mapping.string());
		client.admin().indices().preparePutMapping("floor").setType("position").setSource(mapping).execute().actionGet();
		client.close();
	}
	
	/*
	 * 删除index的mapping设置信息
	 */
	@Test
	public void deleteMapping() throws IOException {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareDeleteMapping("missxu3").setType("user3").execute().actionGet();
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
	
	//数组数据 添加
	// {"name":"名称99","location":[99,1],"address":[{"province":"浙江省","city":"嘉兴市"},{"province":"江苏省","city":"苏州市"}]}
	@Test
	public void initIndex() throws Exception {
		Client client = ClientTemplate.getClient();
		for (int i = 1; i < 100; i++) {
			XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
					.startObject()
						.field("name", "名称" + i)
						.startArray("location")
							.value(i).value(100-i)
						.endArray()
						.startArray("address")
							.startObject().field("province", "浙江省").field("city", "嘉兴市").endObject()
							.startObject().field("province", "江苏省").field("city", "苏州市").endObject()
						.endArray()
					.endObject();
			System.out.println(xContentBuilder.string());
			client.prepareIndex("floor", "position", i + "").setSource(xContentBuilder).execute().actionGet();
		}
		client.close();
	}
	
	
}
