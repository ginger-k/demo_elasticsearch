package com.fccs.es_demo.aggregation;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class AggregationTest {

	@Test
	public void minMaxAvgSumCountStateTest() {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		SearchResponse response = client.prepareSearch("missxu")
		        .setQuery(QueryBuilders.matchPhraseQuery("name", "姓名"))
		        .addAggregation(AggregationBuilders.min("min_age").field("age"))
		        .addAggregation(AggregationBuilders.max("max_age").field("age"))
		        .addAggregation(AggregationBuilders.avg("avg_age").field("age"))
		        .addAggregation(AggregationBuilders.sum("sum_age").field("age"))
		        .addAggregation(AggregationBuilders.count("count_age").field("age"))
		        .addAggregation(AggregationBuilders.stats("stats_age").field("age"))
		        .addAggregation(AggregationBuilders.extendedStats("extendedStats_age").field("age"))
		        .setFrom(0).setSize(5).get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
		Min min = response.getAggregations().get("min_age");
		Max max = response.getAggregations().get("max_age");
		Avg avg = response.getAggregations().get("avg_age");
		Sum sum = response.getAggregations().get("sum_age");
		ValueCount count = response.getAggregations().get("count_age");
		Stats stats = response.getAggregations().get("stats_age");
		ExtendedStats extendedStats = response.getAggregations().get("extendedStats_age");
		System.out.println("min_age: "+min.getValue());
		System.out.println("max_age: "+max.getValue());
		System.out.println("avg_age: "+avg.getValue());
		System.out.println("sum_age: "+sum.getValue());
		System.out.println("count_age: "+count.getValue());
		System.out.println(ReflectionToStringBuilder.toString(stats, ToStringStyle.JSON_STYLE));
		System.out.println(ReflectionToStringBuilder.toString(extendedStats, ToStringStyle.JSON_STYLE));
	}
	
	
	
}


