package com.wxm.jimureport.conttroller;

import org.jeecg.modules.jmreport.desreport.a.a;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 * <p></p>
 *
 * @author 王森明
 * @date 2022/1/24 13:58
 * @since 1.0.0
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String getMsg(){


        return "welcome";
    }
}
