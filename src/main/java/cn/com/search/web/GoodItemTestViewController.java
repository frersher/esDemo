package cn.com.search.web;

import org.springframework.stereotype.Controller;
import cn.com.search.core.Result;
import cn.com.search.core.ResultGenerator;
import cn.com.search.model.GoodItemTestView;
import cn.com.search.service.GoodItemTestViewService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/01/04.
*/
@Controller
@RequestMapping("/good/item/test/view")
public class GoodItemTestViewController {
    @Resource
    private GoodItemTestViewService goodItemTestViewService;

    @PostMapping
    public Result add(@RequestBody GoodItemTestView goodItemTestView) {
        goodItemTestViewService.save(goodItemTestView);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        goodItemTestViewService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody GoodItemTestView goodItemTestView) {
        goodItemTestViewService.update(goodItemTestView);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        GoodItemTestView goodItemTestView = goodItemTestViewService.findById(id);
        return ResultGenerator.genSuccessResult(goodItemTestView);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<GoodItemTestView> list = goodItemTestViewService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
