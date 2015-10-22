package com.fccs.es_demo.bulk;

import java.io.IOException;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class BulkTest {

	/*
	 * 删除不存在，不会报错
	 * 新增的id已存在，会覆盖
	 */
	@Test
	public void bulkTest() throws IOException {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(new DeleteRequest("missxu", "user", "1"));
		bulkRequest.add(new IndexRequest("missxu", "user", "3")
		        .source(XContentFactory.jsonBuilder()
			            .startObject()
			                .field("name", "姓名：新增")//不存在，就新增，不会再更新
			                .field("age", 112)
			            .endObject()));
		bulkRequest.add(new DeleteRequest("missxu", "user", "2"));
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			System.out.println("------有错误--------");
		}
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	} 
	
}
