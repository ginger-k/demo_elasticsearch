package com.fccs.es_qwzn.client;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Test;

public class NodeTest {
	
	@Test
	public void getClientTest() {
		Node node = NodeBuilder.nodeBuilder().node();
		Client client = node.client();
		System.out.println(ReflectionToStringBuilder.toString(client));
		node.close();
	}
}