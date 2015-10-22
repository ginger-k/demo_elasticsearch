package com.fccs.es_demo.deletebyquery;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import com.fccs.es_demo.client.ClientTemplate;

public class DeleteByQueryTest {

	@Test
	public void deleteByQueryTest() {
		Client client = ClientTemplate.getInstance("127.0.0.1", 9300);
		QueryBuilder qb = QueryBuilders.matchPhraseQuery("name", "姓名");
		SearchResponse scrollResp = client.prepareSearch("missxu")
		        .setSearchType(SearchType.SCAN)
		        .setScroll(new TimeValue(60000))
		        .setQuery(qb)
		        .setSize(100).get(); //100 hits per shard will be returned for each scroll
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//Scroll until no hits are returned
		while (true) {
		    for (SearchHit hit : scrollResp.getHits().getHits()) {
		    	System.out.println(hit.getSourceAsString());
		    	bulkRequest.add(new DeleteRequest(hit.getIndex(), hit.getType(), hit.getId()));
		    }
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).get();
		    //Break condition: No hits are returned
		    if (scrollResp.getHits().getHits().length == 0) {
		        break;
		    }
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			System.out.println("------有错误--------");
		}
		ClientTemplate.close(client);
	}
	
}
