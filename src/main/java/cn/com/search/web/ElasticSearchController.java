package cn.com.search.web;

import java.util.List;

import com.cb.springbootjdbc.service.HelloService;
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
	@Autowired
	private HelloService helloService;

	@RequestMapping("/hellotest")
	public Result index(){
		String sayHello = helloService.sayHello();
		System.out.println(sayHello);
		return  ResultGenerator.genSuccessResult();
	}


	@GetMapping("/hello")
	public Result hello() {
		return ResultGenerator.genSuccessResult();
	}


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


	public static void main(String[] args) {
		int[] array1 = new int[]{5};
		System.out.println(bsearch(array1,0,array1.length-1,5));
		System.out.println(search(array1,5));

	}


	private static int bsearch(int[] a, int low,int hight, int value) {
		if (low <= hight) {
			int middle = low + ((hight - low) >> 1);
			if(a[middle] == value){
				return middle;
			}else if(a[middle] < value){
				return bsearch(a, middle + 1, hight, value);
			}else if(a[middle] > value){
				return bsearch(a, low,middle-1, value);
			}
		}
		return -1;
	}

	public static int search(int[] nums, int target) {
		int low = 0;
		int hight = nums.length - 1;

		while (low <= hight) {
			int mid = (low + hight) >> 1;
			if (nums[mid] < target) {
				low = mid + 1;
			} else if (nums[mid] > target) {
				hight = mid - 1;
			} else {
				return mid;
			}

		}
		return -1;
	}



}
