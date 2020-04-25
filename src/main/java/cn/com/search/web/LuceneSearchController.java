package cn.com.search.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.search.core.Result;
import cn.com.search.core.ResultGenerator;
import cn.com.search.model.ReadBooks;
import cn.com.search.service.BookIndexService;

@RestController
@RequestMapping("/search")
public class LuceneSearchController {
	
	@Autowired
	private BookIndexService bookIndexService;
	
	@GetMapping("/book")
	public Result getBook(@RequestParam (required = true)String keywords,@RequestParam (required = false)String bookName,@RequestParam (required = false)String author) {
		List<ReadBooks> books = bookIndexService.search(keywords, bookName, author);
		return ResultGenerator.genSuccessResult(books);
	}
	
	@GetMapping("/creat/book")
	public Result creatBook() {
		bookIndexService.creatIndexLucene();
		return ResultGenerator.genSuccessResult();
	}
	
}
