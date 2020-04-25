package cn.com.search.web;

import org.springframework.stereotype.Controller;
import cn.com.search.core.Result;
import cn.com.search.core.ResultGenerator;
import cn.com.search.model.GoodItem;
import cn.com.search.service.GoodItemService;
import lombok.extern.slf4j.Slf4j;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/12/21.
*/
@Controller
@RequestMapping("/good/item")
@Slf4j
public class GoodItemController {
    @Resource
    private GoodItemService goodItemService;
    
    @GetMapping("/index")
    public Result index() {
    	goodItemService.index();
    	return ResultGenerator.genSuccessResult();
    }
    
    @GetMapping("/search")
    public Result listSerch(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false)String name) {
        PageHelper.startPage(page, size);
        Long start = System.currentTimeMillis();
        List<GoodItem> list = goodItemService.search(name);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false)String name) {
        PageHelper.startPage(page, size);
        Long start = System.currentTimeMillis();
        List<GoodItem> list = goodItemService.list(name);
        log.info("like查询所需要的时间为{}ms",(System.currentTimeMillis() - start));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
