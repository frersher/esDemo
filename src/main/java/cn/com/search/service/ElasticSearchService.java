package cn.com.search.service;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;

import cn.com.search.model.ReadBooks;
import cn.com.search.vo.BookSearchParam;
import cn.com.search.vo.BookSearchResultVo;

public interface ElasticSearchService {
	
	//单个添加
	public Boolean add(Map<String, Object> doc) throws Exception;
	
	//多个添加
	public Boolean adds(List<Map<String, Object>> docs);
	
	//bulkIn 往es批量插入数据 效率很高，数据量大的时候用
	public Boolean searchBulkIn(List<Map<String, Object>> datas,String index,String type) throws Exception;
	
	public Boolean delete();
	
	
	public SearchResponse query();

	public List<BookSearchResultVo> queryDocumentByParam(String indices, String type, BookSearchParam param);
	
}
