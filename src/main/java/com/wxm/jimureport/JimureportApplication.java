package com.wxm.jimureport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@ServletComponentScan   @WebFilter 等注解时，启动类需要添加该注解
@SpringBootApplication(scanBasePackages = {"com.wxm","org.jeecg.modules.jmreport"})
public class JimureportApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimureportApplication.class, args);
    }

}
