package com.fccs.es_demo.update;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class UpdateTest {

	/*
	 * 如果不存在，会报错
	 */
	@Test
	public void updateOneTest1() throws IOException, InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		UpdateRequest response = new UpdateRequest();
		response.index("missxu");
		response.type("user");
		response.id("2");
		response.doc(XContentFactory.jsonBuilder().startObject().field("name", "姓名：改过2").endObject());
		UpdateResponse response2 = client.update(response).actionGet();
		System.out.println("index: "+response2.getIndex()+" type: "+response2.getType()+" id: "+response2.getId() + " created: "+response2.isCreated());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
	@Test
	public void updateOneTest2() throws ElasticsearchException, IOException {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		UpdateResponse response = client.prepareUpdate("missxu", "user", "28")
			.setDoc(XContentFactory.jsonBuilder().startObject().field("name", "姓名：又改又改").endObject())
			.get();
		System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " created: "+response.isCreated());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
	
	/*
	 * 不存在，就创建，之后不会更新，response.isCreated()为true
	 * 存在，直接更新，response.isCreated()为false
	 */
	@Test
	public void upertTest() throws ElasticsearchException, IOException, InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		IndexRequest indexRequest = new IndexRequest("missxu", "user", "16")
		        .source(XContentFactory.jsonBuilder()
		            .startObject()
		                .field("name", "姓名：没有就新增")//不存在，就新增，不会再更新
		                .field("age", 111)
		            .endObject());
		UpdateRequest updateRequest = new UpdateRequest("missxu", "user", "16")
		        .doc(XContentFactory.jsonBuilder()
		            .startObject()
		            	.field("name", "姓名：有就更新")//存在，就更新
		                .field("age", 122)
		            .endObject())
		        .upsert(indexRequest);              
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " created: "+response.isCreated());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
}
