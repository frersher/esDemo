package cn.com.search.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.com.search.core.ElasticSearchManger;
import cn.com.search.model.ReadBooks;
import cn.com.search.service.ElasticSearchService;
import cn.com.search.vo.BookSearchParam;
import cn.com.search.vo.BookSearchResultVo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

	@Autowired
	ElasticSearchManger elasticSearchManger;

	// 单个数据添加
	public Boolean add(Map<String, Object> doc) throws Exception {
		if (doc != null) {
			try {
				XContentBuilder json = XContentFactory.jsonBuilder().startObject();
				Iterator<String> iterator = doc.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					Object object = doc.get(key);
					if (object instanceof Integer)
						json.field(key, Integer.valueOf(object.toString()));
					else if (object instanceof Long)
						json.field(key, Long.valueOf(object.toString()));
					else if (object instanceof String)
						json.field(key, object.toString());
					else
						json.field(key, object);
				}
				json.endObject();
				elasticSearchManger.client.prepareIndex("bbg_goods", "jdbc").setSource(json).get();
				return true;
			} catch (Exception e) {
				throw e;
			}
		}
		return false;
	}

	public Boolean adds(List<Map<String, Object>> docs) {
		return true;
	}

	public Boolean searchBulkIn(List<Map<String, Object>> datas, String index, String type) throws Exception {
		try {
			BulkRequestBuilder bulkRequest = elasticSearchManger.client.prepareBulk();
			for (Map<String, Object> data : datas) {
				String content = JSONObject.toJSONString(data);		//zai es里用的是json格式。
				System.out.println(content);
				bulkRequest.add(elasticSearchManger.client.prepareIndex(index, type, data.get("id").toString())
						.setSource(data));
			}
			bulkRequest.execute().actionGet();		//真实写数据
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean delete() {
		return true;
	}

	public SearchResponse query() {
		return null;
	}

	public  List<BookSearchResultVo> queryDocumentByParam(String indices, String type, BookSearchParam param) {
		SearchRequestBuilder builder = buildRequest(indices, type);
		builder.setQuery(convertParam(param));
		//if
		builder.setFrom(param.getPageIndex()).setSize(param.getPageSize());	//分页 注意es其实是内存分页 
		//比如你分页10条 查第10页 会把钱10也全部拿出来放到内存中 给你取你要的后面的数据. 不要查的太多 防止被人参数注入 内存爆炸 最多不要超过1000页.
		//builder.addSort("price", SortOrder.ASC);	//结合权重排序 需要自己先离线计算好分值。然后在使用es里面的sum加权函数就可以了
		if(param.getHighlightedField() != null) {
			builder.addHighlightedField(param.getHighlightedField());
			builder.setHighlighterPreTags("<h1 style='red'>");
			builder.setHighlighterPostTags("</h1>");
		}
		log.info("对index={}，type={}进行检索，query={}",indices,type,param.toString());
		SearchResponse resp = builder.get();
		log.info("对index={}，type={}进行检索，query={},检索到结果数为{}条",indices,type,param.toString(),resp.getHits().getTotalHits());
		//convertSugg(resp,"facet");
		return convertResponse(resp,param);
	}

	public SearchRequestBuilder buildRequest(String indices, String type) {
		return elasticSearchManger.client.prepareSearch(indices).setTypes(type);
	}

	private BoolQueryBuilder convertParam(BookSearchParam param) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.hasText(param.getBookName())) {
			boolQueryBuilder.must(QueryBuilders.termQuery("name", param.getBookName()));
		}
		if (param.getPrice() != null) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(param.getPrice()));		//和前面的and
		}
		if (StringUtils.hasText(param.getDesc())) {
			boolQueryBuilder.must(QueryBuilders.queryStringQuery("discription:"+param.getDesc()));
		}
		return boolQueryBuilder;
	}
	
	public List<BookSearchResultVo> convertResponse(SearchResponse response,BookSearchParam searchParam) {
		List<BookSearchResultVo> list = Lists.newArrayList();
		if (response != null && response.getHits() != null) {
			String result = org.apache.commons.lang3.StringUtils.EMPTY;
			BookSearchResultVo e = new BookSearchResultVo();
			for (SearchHit hit : response.getHits()) {
				result = hit.getSourceAsString();
				Map<String, HighlightField> map = hit.getHighlightFields();
				if (StringUtils.hasText(result)) {
					e = JSONObject.parseObject(result, BookSearchResultVo.class);
				}
				if (e != null) {
					if(map != null) {
						HighlightField field = map.get(searchParam.getHighlightedField());
						String str = "";
						for(Text text : field.fragments()) {
							str += text.string();
						}
						Map<String, String> highlightMap = new HashMap<>();
						System.out.println(field.getName() + ":" + str);
						highlightMap.put(field.getName(), str);
					}
					list.add(e);
				}
			}
		}
		return list;
	}
}
