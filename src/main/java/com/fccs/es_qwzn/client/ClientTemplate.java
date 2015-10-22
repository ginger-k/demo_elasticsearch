package com.fccs.es_qwzn.client;


import java.lang.reflect.Constructor;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


/*
 * java client 会创建一个连接池，所以用完后不要关闭，下次可以直接复用。采用单例模式，可以保证应用只产生一个实例。
 * 采用java反射的方法生成一个client ，效率明显高于由于new 一个客户端。
 */
public class ClientTemplate {

	private static final String host = "127.0.0.1";
	private static final int port = 9300;
	private static TransportClient client;
	private static Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true).build();
	
	
	static {
		try {
			Class<?> clazz = Class.forName(TransportClient.class.getName());
			Constructor<?> constructor = clazz.getDeclaredConstructor(Settings.class);
			constructor.setAccessible(true);
			client = (TransportClient) constructor.newInstance(settings);
			client.addTransportAddress(new InetSocketTransportAddress(host, port));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private ClientTemplate() {
	}
	
	
	public static synchronized TransportClient getClient() {
		return client;
	}
	
	
}
