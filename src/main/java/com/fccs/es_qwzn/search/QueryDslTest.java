package com.fccs.es_qwzn.search;

import java.text.ParseException;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;


/*
 * 有很多query，要了解每个query的使用场景
 */
public class QueryDslTest {
	
	
	@Test
	public void dateRangeSearchTest() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.rangeQuery("joinDate").gte("2014-07-10").lte("2014-08-01"))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}

	/*
	 * 简单搜索，姓氏中含有Smith的员工
	 */
	@Test
	public void simpleSearchTest1() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("lastName", "Smith"))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	/*
	 * 稍微复杂的搜索，姓氏中含有Smith的员工，并且年龄大于30岁
	 */
	@Test
	public void simpleSearchTest2() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("lastName", "Smith"))
			.setPostFilter(FilterBuilders.rangeFilter("age").gt(30))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	
	/*
	 * 全文搜索，喜欢"rock climbing"的员工
	 * matchQuery默认的Type 为 MatchQueryBuilder.Type.BOOLEAN
	 * 默认情况下，根据结果相关性评分来对结果集进行排序，所谓的相关性评分，就是文档与查询条件的匹配程度。
	 */
	@Test
	public void documentSearchTest() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("about", "rock climbing"))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(ReflectionToStringBuilder.toString(hits.getHits()[i], ToStringStyle.JSON_STYLE));
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	/*
	 * 短语搜索，查询同时包含"rock"和"climbing"(并且是相邻的)的员工记录
	 * 只要将match查询变更为match_phrase查询即可
	 * matchQuery的Type 设置为 MatchQueryBuilder.Type.PHRASE
	 */
	@Test
	public void phraseSearchTest() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("about", "rock climbing").type(MatchQueryBuilder.Type.PHRASE))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(ReflectionToStringBuilder.toString(hits.getHits()[i], ToStringStyle.JSON_STYLE));
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	/*
	 * 短语前缀搜索，查询某个单词以"lo"开头
	 * 只要将match查询变更为match_phrase_prefix查询即可
	 * matchQuery的Type 设置为 MatchQueryBuilder.Type.PHRASE_PREFIX
	 */
	@Test
	public void phrasePrefixSearchTest() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("about", "lo").type(MatchQueryBuilder.Type.PHRASE_PREFIX))
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println("score:"+hits.getHits()[i].getScore()+" "+hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
	/*
	 * 高亮搜索
	 */
	@Test
	public void highlightSearchTest() throws ElasticsearchException, ParseException {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		SearchResponse response = client.prepareSearch("fccs")
			.setTypes("employee")
			.setQuery(QueryBuilders.matchQuery("about", "rock climbing").type(MatchQueryBuilder.Type.PHRASE))
			.addHighlightedField("about")
			.get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getHighlightFields().get("about").getFragments()[0].toString());
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		client.close();
	}
	
}
