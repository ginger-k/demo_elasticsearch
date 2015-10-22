package com.fccs.es_qwzn.index;

import java.io.IOException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;

public class RiverSycIndexTest {

	/*
	 * 映射为简单类型，es的字段和sql的字段一一对应， 可以只更新部分字段
	 */
	@Test
	public void riverUpdateSimpleType() throws IOException {
		TransportClient client = ClientTemplate.getClient();
		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
				.startObject()
					.field("type", "jdbc")
					.startObject("jdbc")
						.field("url", "jdbc:mysql://localhost:3306/wabacusdemodb")
						.field("user", "root")
						.field("password", "password")
						.field("sql", "SELECT td.no AS _id, ename AS name, salary AS high,marriage AS gender, joinindate AS birthday,photo AS description FROM tbl_detailinfo td")
						.field("index","missxu")  
                        .field("type","user")
                        .field("bulk_size", 100)
                        .field("max_bulk_requests", 30)
                        .field("bulk_timeout", "10s")
                        .field("flush_interval", "5s")
                        .field("schedule", "0 0-59 0-23 * * ?")
					.endObject()
				.endObject();
		client.prepareIndex("_river", "river_missxu", "_meta").setSource(xContentBuilder).execute().actionGet();
		client.close();
	}
	
	
	/*
	 * 映射为复杂类型，只更新部分字段
	 */
	@Test
	public void riverUpdateObjectType() throws IOException {
		TransportClient client = ClientTemplate.getClient();
		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
				.startObject()
					.field("type", "jdbc")
					.startObject("jdbc")
						.field("url", "jdbc:mysql://localhost:3306/wabacusdemodb")
						.field("user", "root")
						.field("password", "password")
						.field("sql", "SELECT td.no AS _id,td.province AS 'address.province', td.city AS 'address.city' FROM tbl_detailinfo td")
						.field("index","missxu2")  
                        .field("type","user2")
                        .field("bulk_size", 100)
                        .field("max_bulk_requests", 30)
                        .field("bulk_timeout", "10s")
                        .field("schedule", "0 0-59 0-23 * * ?")
					.endObject()
				.endObject();
		client.prepareIndex("_river", "rivertest2", "_meta").setSource(xContentBuilder).execute().actionGet();
		client.close();
	}
	
	/*
	 * 数组类型还不知道怎么同步
	 */
	@Test
	public void riverUpdateArrayType() throws IOException {

	}
	
}
