package com.fccs.es_qwzn.index;



import java.io.IOException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;


public class IndexTest {

	/*
	 * 创建一个空index，没有任何type，可以在这基础上设置mapping
	 * GET /fccs_newhouser/_mapping
	 * 		返回的mapping没有数据
	 */
	@Test
	public void prepareCreateIndex() {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareCreate("missxu2").execute().actionGet();
		client.close();
	}
	
	/*
	 * 删除一个index
	 */
	@Test
	public void deleteIndex() {
		Client client = ClientTemplate.getClient();
		client.admin().indices().prepareDelete("missxu").execute().actionGet();
		client.close();
	}
	
	
	/*
	 * 创建一个具体的索引(index,type,field)
	 * 如果预先设置好了mapping，就必须按照规定的数据类型添加数据，参见MappingTest
	 * 但是，可以添加新的字段
	 */
	@Test
	public void createOne() throws ElasticsearchException, IOException {
		Client client = ClientTemplate.getClient();
		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
											.startObject()
												.field("name", "echo")
												.field("high", 166.5)
												.field("gengder", true)
												.field("age", 27)
												.field("birthday", "1988-05-06")
												.field("description", "2115-09-01 join fccs")
												.field("more", "2115-09-01 join fccs") //这个字段mapping中没有
											.endObject();
        client.prepareIndex("missxu", "user", "002").setSource(xContentBuilder).execute().actionGet();
		client.close();
	}
	
	/*
	 * 删除某条具体索引(index,type,field)
	 * 即使没有数据，该操作也不会删除field，比如上面的more
	 */
	@Test
	public void deleteOne() {
		Client client = ClientTemplate.getClient();
		client.prepareDelete("_river", "rivertest3", "_meta").execute().actionGet();
		client.close();
	}
	
	
	
}
