package com.fccs.es_qwzn.client;

import java.util.List;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

public class TransportClientTest {

	@Test
	public void getClient() {
		@SuppressWarnings("resource")
		Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		System.out.println(client);
		client.close();
	}
	
	
	
	/*
	 * IndicesAdminClient提供了对针对索引的action和operation的管理接口
	 * ClusterAdminClient提供了对针对集群的action和operation的管理接口
	 */
	@Test
	public void adminTest() {
		@SuppressWarnings("resource")
		Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		System.out.println(client);
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		ClusterAdminClient clusterAdminClient = client.admin().cluster();
		System.out.println(indicesAdminClient);
		System.out.println(clusterAdminClient);
		
		AnalyzeResponse response = client.admin().indices().prepareAnalyze("missxu", "2015-02-02").setField("name").execute().actionGet();
		List<AnalyzeToken> tokens = response.getTokens();
		for (AnalyzeToken token : tokens) {
			System.out.println(token.getTerm());
		}
		
		client.close();
	}
	
	
}
