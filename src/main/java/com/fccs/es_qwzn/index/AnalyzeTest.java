package com.fccs.es_qwzn.index;

import java.util.List;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.client.Client;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class AnalyzeTest {

	/*
	 * 给某个字段进行分词测试
	 * description字段设置了不分词
	 */
	@Test
	public void analyzeTest() {
		Client client = ClientTemplate.getClient();
		AnalyzeResponse response = client.admin().indices().prepareAnalyze("missxu", "2015-02-02").setField("description").execute().actionGet();
		List<AnalyzeToken> tokens = response.getTokens();
		for (AnalyzeToken token : tokens) {
			System.out.println(token.getTerm());
		}
		client.close();
	}
	
	
	/*
	 * 查询中指定分词器
	 * 通过MatchQueryBuilder等的analyzer(String s)方法指定
	 */
	@Test
	public void analyzer() {
		
		
	}
	
}
