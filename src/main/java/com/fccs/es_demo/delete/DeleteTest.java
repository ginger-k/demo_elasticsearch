package com.fccs.es_demo.delete;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class DeleteTest {

	/*
	 * 如果没有，不会报错，response.isFound()可以判断
	 */
	@Test
	public void deleteOneTest1() {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
        DeleteResponse response = client.prepareDelete("missxu", "user", "2").get();
        System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " founded: "+response.isFound());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
	@Test
	public void deleteOneTest2() {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		DeleteRequest request = new DeleteRequest("missxu", "user", "23");
		DeleteResponse response = client.delete(request).actionGet();
        System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " founded: "+response.isFound());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
}
