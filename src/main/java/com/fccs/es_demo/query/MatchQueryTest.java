package com.fccs.es_demo.query;

import java.text.ParseException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class MatchQueryTest {

	@Test
	public void matchQueryTest() throws ElasticsearchException, ParseException {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		SearchResponse response = client.prepareSearch("missxu")
			.setTypes("user")
			.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery(QueryBuilders.matchQuery("name", "xin").type(MatchQueryBuilder.Type.PHRASE_PREFIX))
			.setFrom(0).setSize(9).setExplain(true).get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
	}
	
}
