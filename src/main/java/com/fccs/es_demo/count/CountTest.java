package com.fccs.es_demo.count;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class CountTest {

	@Test
	public void countTest() {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		CountResponse response = client.prepareCount("missxu")
					.setQuery(QueryBuilders.matchQuery("name", "姓名"))
					.get();
		System.out.println("count："+response.getCount());
	}
	
}
