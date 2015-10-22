package com.fccs.es_qwzn.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class SortTest {

	
	/*
	 * 多个排序，可以继续通过addSort(SortBuilder sb)添加
	 */
	@Test
	public void sort() {
		TransportClient client = ClientTemplate.getClient();
		FieldSortBuilder sortBuilder = SortBuilders.fieldSort("birthday").order(SortOrder.DESC);
		SearchResponse searchResponse = client.prepareSearch("missxu").setTypes("user")
												.addSort(sortBuilder)
												.execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	
}
