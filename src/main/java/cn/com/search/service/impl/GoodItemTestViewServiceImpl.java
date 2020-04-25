package cn.com.search.service.impl;

import cn.com.search.dao.GoodItemTestViewMapper;
import cn.com.search.model.GoodItemTestView;
import cn.com.search.service.GoodItemTestViewService;
import cn.com.search.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/04.
 */
@Service
@Transactional
public class GoodItemTestViewServiceImpl extends AbstractService<GoodItemTestView> implements GoodItemTestViewService {
    @Resource
    private GoodItemTestViewMapper bbgDtpMomItemMapper;

}
