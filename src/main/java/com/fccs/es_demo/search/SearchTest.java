package com.fccs.es_demo.search;


import java.text.ParseException;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class SearchTest {
	
	/*
	 * 搜索所有，不设from和size的话，默认为0-10
	 */
	@Test
	public void smallestSearchTest() {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		try {
			ListenableActionFuture<SearchResponse> listenableActionFuture = client.prepareSearch().execute();
			System.out.println(ReflectionToStringBuilder.toString(listenableActionFuture, ToStringStyle.JSON_STYLE));
		} catch (Exception e) {
			client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		}
		SearchResponse response = client.prepareSearch().setFrom(0).setSize(20).setExplain(true).get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
	}

	/* 搜索，多个排序，多个过滤，分页
	 * 注意：string类型的日期
	      "2015-9-23"和"2015-09-23"能查到相同的结果，说明不是简单的字符串比较
	      	注释部分为 long类型的日期
	 */
	@Test
	public void searchTest() throws ElasticsearchException, ParseException {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		SearchResponse response = client.prepareSearch("missxu")
			.setTypes("user")
			.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery(QueryBuilders.fuzzyQuery("name", "xingming"))
			.addSort(SortBuilders.fieldSort("age").order(SortOrder.ASC))
			.addSort(SortBuilders.fieldSort("birthday1").order(SortOrder.DESC))
			.setPostFilter(FilterBuilders.andFilter(FilterBuilders.rangeFilter("age").from(6).to(8),
					FilterBuilders.rangeFilter("birthday1").from("2015-09-12").to("2015-9-23")))
			/*.setPostFilter(FilterBuilders.andFilter(FilterBuilders.rangeFilter("age").from(2).to(5),
					FilterBuilders.rangeFilter("birthday").from(System.currentTimeMillis()+365*24*60*60*1000*2).to(System.currentTimeMillis()+365*24*60*60*1000*5)))*/
			.setFrom(0).setSize(60).setExplain(true).get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
	}
	
	@Test
	public void scrollSearchTest() {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		QueryBuilder qb = QueryBuilders.matchPhraseQuery("name", "xingming");
		SearchResponse scrollResp = client.prepareSearch("missxu")
		        .setSearchType(SearchType.SCAN)
		        .setScroll(new TimeValue(60000))
		        .setQuery(qb)
		        .setSize(100).get(); //100 hits per shard will be returned for each scroll
		//Scroll until no hits are returned
		while (true) {
		    for (SearchHit hit : scrollResp.getHits().getHits()) {
		    	System.out.println("index:"+hit.getIndex()+" type:"+hit.getType()+" id:"+hit.getId()+" "+hit.getSourceAsString());
		    }
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).get();
		    //Break condition: No hits are returned
		    if (scrollResp.getHits().getHits().length == 0) {
		        break;
		    }
		}
		ClientTemplate.close(client);
	}
		
		
		/*
		 * 注意：该程序对于某些Query会抛空指针异常
		 */
		@Test
		public void multiSearchTest() {
			Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
			SearchRequestBuilder srb1 = client.prepareSearch()
					.setQuery(QueryBuilders.matchPhraseQuery("name", "姓名")).setFrom(0).setSize(9);
			SearchRequestBuilder srb2 = client.prepareSearch()
					.setQuery(QueryBuilders.matchPhraseQuery("name", "xingming")).setFrom(0).setSize(9);
			MultiSearchResponse sr = client.prepareMultiSearch()
			        .add(srb1)
			        .add(srb2)
			        .get();
			// You will get all individual responses from MultiSearchResponse#getResponses()
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse response = item.getResponse();
			    SearchHit[] hits = response.getHits().getHits();
			    for (SearchHit hit : hits) {
					System.out.println(hit.getSourceAsString());
				}
			    nbHits += response.getHits().getTotalHits();
			}
			System.out.println("总数："+nbHits);
			ClientTemplate.close(client);
		}
	
}
