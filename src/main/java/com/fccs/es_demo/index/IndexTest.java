package com.fccs.es_demo.index;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fccs.es_demo.bean.User;
import com.fccs.es_demo.client.ClientTemplate;

public class IndexTest {

	//注意：字段的类型为第一次插入数据的类型，之后插入类型必须一致，否则会报错
	@Test
	public void createIndexTest1() throws Exception {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("姓名：" + i);
            user.setAge(i % 10);
            user.setBirthday(new Date(System.currentTimeMillis() + 365*24*60*60*1000*i));
            String json = mapper.writeValueAsString(user);
            System.out.println(json);
            IndexResponse response = client.prepareIndex("missxu", "user", ""+(i+1)).setSource(json).get();
            System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " created: "+response.isCreated());
        }
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
	@Test
	public void createIndexTest2() throws Exception {
		long start = System.currentTimeMillis();
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		for (int i = 10; i < 20; i++) {
            IndexResponse response = client.prepareIndex("missxu", "user", ""+(i+101))
            		.setSource(XContentFactory.jsonBuilder()
            				.startObject()
            				.field("name", "xingming：" + i)
            				.field("age", i % 10)
            				.field("birthday1", new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 365*24*60*60*1000*i)))
            				.endObject())
                    .execute().actionGet();
            System.out.println("index: "+response.getIndex()+" type: "+response.getType()+" id: "+response.getId() + " created: "+response.isCreated());
        }
		ClientTemplate.close(client);
		long end = System.currentTimeMillis();
		System.out.println("创建索引花费时间：" + (end-start) +"ms");
	}
	
}
