package com.fccs.es_demo.client;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

public class TransportClientTest {

	@Test
	public void getClient() {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
		System.out.println(client);
		client.close();
	}
	
	
}
