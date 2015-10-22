package com.fccs.es_qwzn.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class SearchTest {

	/*
	 * 根据需求，setQuery()，addSort()，setPostFilter()
	 */
	@Test
	public void simple() {
		TransportClient client = ClientTemplate.getClient();
		SearchResponse response = client.prepareSearch("missxu")
									.setTypes("user")
									.setFrom(0)
									.setSize(20)
									.setExplain(true) //返回信息中解释一些错误
									.execute().actionGet();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
	}
	
}
