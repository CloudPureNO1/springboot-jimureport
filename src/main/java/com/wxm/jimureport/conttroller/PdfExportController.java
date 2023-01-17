package com.wxm.jimureport.conttroller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 * <p></p>
 *
 * @author 王森明
 * @date 2022/1/24 13:45
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/spi")

// 放在类上 标识所有接口都支持跨域访问
//@CrossOrigin(origins ={"http://localhost:8080","http://127.0.0.1:8080"})
public class PdfExportController {
    private final RestTemplate restTemplate;

    public PdfExportController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 在方法上标识，方法支持跨域访问
    @CrossOrigin(origins ={"http://localhost:8080","http://127.0.0.1:8080"})
    @ResponseBody
    @RequestMapping("/download/{type}")
    public void download(@PathVariable String type,@RequestBody JSONObject json,HttpServletRequest request, HttpServletResponse response) {
        //创建url路径
        String url = "http://127.0.0.1:8888/wxm/jmreport/exportPdf";
        Map<String, Object> map = new HashMap<>();
        JSONObject object = new JSONObject();
        //此处为示例，需要传递api和sql的正确参数值
        object.put("name", "张三");
        //queryParam中有个特殊属性 dpi每英寸点数,windows是96，可不用传
        object.put("dpi", "96");
        map.put("excelConfigId", "582015784389632000");
        map.put("queryParam", object);
        HttpHeaders headers = new HttpHeaders();
        //如果有字典需要传token
        headers.add("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjY2NjAzNzcsInVzZXJuYW1lIjoiYWRtaW4ifQ.6VdYrH2UChHeJLLKLiiuOu1Mos40CIN3zbigyI2O978");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        try {
            String apiResult = restTemplate.postForObject(url, httpEntity, String.class);
            JSONObject jsonObject = JSONObject.parseObject(apiResult);
            Object code = jsonObject.get("code");
            Object message = jsonObject.get("message");
            if (null != code && !"".equals(String.valueOf(code)) && "200".equals(String.valueOf(code))) {
                JSONObject result = jsonObject.getJSONObject("result");
                //文件byte64字符串
                String file = result.getString("file");
                //文件名称
                String name = result.getString("name");
                //转换成byte
                byte[] buffer = Base64Utils.decodeFromString(file);
                name = URLEncoder.encode(name, "UTF-8");
                response.setContentType("application/vnd.ms-excel;charset=GBK");
                response.setHeader("Content-Disposition", "attachment;filename=" + name);
                response.getOutputStream().write(buffer);
                response.getOutputStream().flush();
            } else {
                log.info("调用失败:" + String.valueOf(message));
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // 在方法上标识，方法支持跨域访问
    @CrossOrigin(origins ={"http://localhost:8080","http://127.0.0.1:8080"})
    @ResponseBody
    @RequestMapping("/downloadPdf")
    public JSONObject download(@RequestBody JSONObject json,HttpServletRequest request, HttpServletResponse response) {
        //创建url路径
        String url = "http://127.0.0.1:8888/wxm/jmreport/exportPdf";
        Map<String, Object> map = new HashMap<>();
        JSONObject object = new JSONObject();
        //此处为示例，需要传递api和sql的正确参数值
        object.put("name", "张三");
        //queryParam中有个特殊属性 dpi每英寸点数,windows是96，可不用传
        object.put("dpi", "96");
        map.put("excelConfigId", "1347459370216198144");
        map.put("queryParam", object);
        HttpHeaders headers = new HttpHeaders();
        //如果有字典需要传token
        headers.add("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjY2NjAzNzcsInVzZXJuYW1lIjoiYWRtaW4ifQ.6VdYrH2UChHeJLLKLiiuOu1Mos40CIN3zbigyI2O978");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        try {
            String apiResult = restTemplate.postForObject(url, httpEntity, String.class);
            JSONObject jsonObject = JSONObject.parseObject(apiResult);
            Object code = jsonObject.get("code");
            Object message = jsonObject.get("message");
            if (null != code && !"".equals(String.valueOf(code)) && "200".equals(String.valueOf(code))) {
                JSONObject result = jsonObject.getJSONObject("result");
                return result;
            } else {
                log.info("调用失败:" + String.valueOf(message));
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    // 在方法上标识，方法支持跨域访问
    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @PostMapping("/msg")
    @ResponseBody
    public String getMsg(){
        return "hello";
    }

}
