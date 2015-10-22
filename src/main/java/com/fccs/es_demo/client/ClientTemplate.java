package com.fccs.es_demo.client;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ClientTemplate {

	private ClientTemplate() {
	}
	
	@SuppressWarnings("resource")
	public static  Client getLocalInstance() {
		return new TransportClient()
        .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
	}
	
	@SuppressWarnings("resource")
	public static Client getInstance(String host, int port) {
		if (StringUtils.isBlank(host) || port == 0)
			throw new IllegalArgumentException();
		return new TransportClient()
		        .addTransportAddress(new InetSocketTransportAddress(host, port));
	}
	
	public static void close(Client client) {
		if (client != null)
			client.close();
	}
	
}
