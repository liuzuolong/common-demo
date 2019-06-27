package com.bosssoft.gp.demo.web.controller;

import com.bosssoft.gp.framework.base.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author smile7up
 * @createDate 2019-06-27
 * @Description
 */
@RestController
@RequestMapping("/rest/v1")
public class TestController extends BaseController {

    @RequestMapping("test")
    public Map<String,String> test(){
        Map<String,String> infoMap = new HashMap<>();
        success(infoMap);
        return infoMap;
    }
}
