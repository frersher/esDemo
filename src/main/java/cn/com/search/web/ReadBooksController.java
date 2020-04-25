package cn.com.search.web;

import org.springframework.stereotype.Controller;
import cn.com.search.core.Result;
import cn.com.search.core.ResultGenerator;
import cn.com.search.model.ReadBooks;
import cn.com.search.service.ReadBooksService;
import lombok.extern.slf4j.Slf4j;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/12/16.
*/
@Controller
@RequestMapping("/read/books")
@Slf4j
public class ReadBooksController {
    @Resource
    private ReadBooksService readBooksService;

    @PostMapping
    public Result add(@RequestBody ReadBooks readBooks) {
        readBooksService.save(readBooks);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        readBooksService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody ReadBooks readBooks) {
        readBooksService.update(readBooks);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        ReadBooks readBooks = readBooksService.findById(id);
        return ResultGenerator.genSuccessResult(readBooks);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,@RequestParam String keyWords) {
        PageHelper.startPage(page, size);
        Long start = System.currentTimeMillis();
        List<ReadBooks> list = readBooksService.search(keyWords);
        log.info("查询key={}，所需时间={}ms,记录={}",keyWords,(System.currentTimeMillis() - start),list.size());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
