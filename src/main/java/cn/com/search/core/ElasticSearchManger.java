package cn.com.search.core;

import java.net.InetAddress;
 
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchManger implements InitializingBean{
	
    public TransportClient client;
    public Settings settings;
    public void init() {
        settings = Settings.settingsBuilder().put("cluster.name", "my-es").put("client.transport.sniff", true)
                .build();
        try {
 
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 8300));
            //这里是数据传输端口 不是9200 8200
        } catch (Exception e) {
 
        }
    }
 
    public static void main(String[] args) {
        ElasticSearchManger elasticSearchManger = new ElasticSearchManger();
        elasticSearchManger.init();
        SearchResponse response = elasticSearchManger.client.prepareSearch("bbg_goods").setTypes("item_loc")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.queryStringQuery("item_desc:格力"))
                .setFrom(0).setSize(10).setExplain(false)
                .get();
        System.out.println(response);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		 init();
	}
}