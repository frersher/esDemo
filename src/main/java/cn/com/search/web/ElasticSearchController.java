package cn.com.search.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.search.core.Result;
import cn.com.search.core.ResultGenerator;
import cn.com.search.model.ReadBooks;
import cn.com.search.service.ElasticSearchService;
import cn.com.search.service.ReadBooksService;
import cn.com.search.vo.BookSearchParam;
import cn.com.search.vo.BookSearchResultVo;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/es")
@Slf4j
public class ElasticSearchController {

	@Autowired
	ReadBooksService booksService;
	@Autowired
	ElasticSearchService esSeachService;

	@GetMapping("/creatIndex")
	public Result creatIndex() {
		booksService.creatIndex();		//做一次全量数据插入 做一次就行了。
		return ResultGenerator.genSuccessResult();
	}

	@GetMapping("/search")
	public Result search(@RequestParam(required = false) String keyWord) {

		BookSearchParam bookSearchParam = new BookSearchParam();
		bookSearchParam.setDesc("童话故事");
		List<BookSearchResultVo> books = esSeachService.queryDocumentByParam("test-es", "test-type", bookSearchParam);
		return ResultGenerator.genSuccessResult(books);
	}
}
