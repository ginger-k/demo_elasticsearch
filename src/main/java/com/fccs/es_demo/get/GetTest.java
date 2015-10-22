package com.fccs.es_demo.get;


import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class GetTest {

	@Test
	public void getOneTest1() {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
        GetResponse response = client.prepareGet("missxu", "user", "16").get();
        System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " exists: "+response.isExists());
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
	@Test
	public void getOneTest2() {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		GetRequest request = new GetRequest("missxu", "user", "16");
		GetResponse response = client.get(request).actionGet();
        System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " exists: "+response.isExists());
        ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
}


