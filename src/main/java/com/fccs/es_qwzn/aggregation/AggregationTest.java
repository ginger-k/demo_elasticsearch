package com.fccs.es_qwzn.aggregation;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.junit.Test;

import com.fccs.es_qwzn.client.ClientTemplate;



/*
 * 聚合运行在数据上生成复杂的分析统计，很像SQL中的group by，但功能更强大
 */
public class AggregationTest {

	/*
	 * 找到所有职员中最大的共同点(兴趣爱好)是什么
	 */
	@Test
	public void aggregationTest() {
		Client client = ClientTemplate.getClient();
		SearchResponse response = client.prepareSearch("fccs")
				.setTypes("employee")
		        .addAggregation(AggregationBuilders.terms("by_interests").field("interests"))
		        .get();
		StringTerms terms = response.getAggregations().get("by_interests");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			System.out.println("key:"+bucket.getKey()+" doc_count:"+bucket.getDocCount());
		}
		client.close();
	}
	
	/*
	 * 所有姓氏为"Smith"的员工的最大的共同点(兴趣爱好)是什么
	 * 聚合变成只包含和查询语句想匹配的文档
	 */
	@Test
	public void queryAggregationTest() {
		Client client = ClientTemplate.getClient();
		SearchResponse response = client.prepareSearch("fccs")
				.setTypes("employee")
				.setQuery(QueryBuilders.matchQuery("lastName", "Smith"))
		        .addAggregation(AggregationBuilders.terms("by_interests").field("interests"))
		        .get();
		Terms terms = response.getAggregations().get("by_interests");
		for (Terms.Bucket bucket : terms.getBuckets()) {
			System.out.println("key:"+bucket.getKey()+" doc_count:"+bucket.getDocCount());
		}
		client.close();
	}
	
	/*
	 * 统计每种兴趣下职工的平均年龄
	 * 聚合也允许分级汇总
	 */
	@Test
	public void subAggregationTest() {
		Client client = ClientTemplate.getClient();
		SearchResponse response = client.prepareSearch("fccs")
				.setTypes("employee")
		        .addAggregation(AggregationBuilders.terms("by_interests").field("interests")
		        		.subAggregation(AggregationBuilders.avg("avg_age").field("age")))
		        .get();
		Terms terms = response.getAggregations().get("by_interests");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Avg avg = bucket.getAggregations().get("avg_age");
			System.out.println("key:"+bucket.getKey()+" doc_count:"+bucket.getDocCount()+" avg_age:"+avg.getValue());
		}
		client.close();
	}
	
}
