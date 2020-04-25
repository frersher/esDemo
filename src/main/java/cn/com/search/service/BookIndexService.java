package cn.com.search.service;

import java.util.List;

import cn.com.search.model.ReadBooks;

public interface BookIndexService {

	Integer creatIndexLucene();
	
	List<ReadBooks> search(String keywords,String bookName,String author);
}
