package com.fccs.es_qwzn.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class FilterTest {

	
	
	@Test
	public void filt() {
		TransportClient client = ClientTemplate.getClient();
		RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter("birthday").gte("1988-01-31").lt("1989-01-01");
		SearchResponse searchResponse = client.prepareSearch("missxu").setTypes("user")
												.setPostFilter(rangeFilter)
												.execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	
	/*
	 * 多个过滤，可以通过FilterBuilders的andFilter(FilterBuilder... filters)
	 * 			orFilter(FilterBuilder... filters)
	 * 			notFilter(FilterBuilder filter)等
	 * 		构建
	 */
	@Test
	public void mulfilt() {
		TransportClient client = ClientTemplate.getClient();
		RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter("birthday").gte("1988-01-31").lt("1989-01-01");
		TermsFilterBuilder termsFilter = FilterBuilders.termsFilter("name", new String[]{"kuang", "echo"});
		SearchResponse searchResponse = client.prepareSearch("missxu").setTypes("user")
												.setPostFilter(FilterBuilders.andFilter(rangeFilter, termsFilter))
												.execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	
}
