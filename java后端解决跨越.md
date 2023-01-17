# 后端解决跨域
### 五种方法：
> 1. 自定义CorsFilter 继承 Filter  进行配置
> 2. 使用 org.springframework.web.filter.CorsFilter 配置
> 3. 这里可以通过实现 WebMvcConfigurer 接口中的 addCorsMappings() 方法来实现跨域。
> 4. 在Spring Boot 2.X应用程序中可以使用注解@CrossOrigin
> 5. Nginx 配置解决跨域问题

### 具体实现
1. 自定义CorsFilter 继承 Filter  进行配置
```java
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//采用@WebFilter 注解  Filter 方法，还需要在启动类加上@ServletComponentScan注解
//@WebFilter(urlPatterns = "/*", filterName="cosFilter")
// 采用@Component 或者 配置里配置@bean 可以不用@ServletComponentScan注解
@Component
/** 控制过滤器的级别最高 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CosFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest reqs = (HttpServletRequest) req;
        // 可以使用* 代表所有
        // 设置允许Cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 设置允许跨域请求的方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        // 允许跨域请求头包含 x-requested-with,Access-Control-Allow-Origin,content-type,token  其他字符
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Access-Control-Allow-Origin,content-type,token");
        // 允许http://www.xxx.com域（自行设置，这里只做示例）发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", reqs.getHeader("Origin"));
        // 设置在3600秒不需要再发送预校验请求
        response.setHeader("Access-Control-Max-Age", "3600");
        filterChain.doFilter(req,res);
    }
    @Override
    public void init(FilterConfig filterConfig) { }
    @Override
    public void destroy() { }
}

```

2. 使用 org.springframework.web.filter.CorsFilter 配置
```java
@Configuration
public class CrosBeanConfig {
    /**
     * 注册bean
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /**
         * 报错：When allowCredentials is true,
         * allowedOrigins cannot contain the special value "*" since that cannot be
         * set on the "Access-Control-Allow-Origin" response header.
         * To allow credentials to a set of origins, list them explicitly or
         * consider using "allowedOriginPatterns" instead.
         */
        // corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("x-requested-with,Access-Control-Allow-Origin,content-type");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",buildConfiguration());
        return new CorsFilter(source);
    }
 
}
```

3. 这里可以通过实现 WebMvcConfigurer 接口中的 addCorsMappings() 方法来实现跨域。
```java
/**
 * 直接继承WebMvcConfigurer 实现addCorsMappings  方式
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }

}

/**
 * 再配置文件中注册@Bean  WebMvcConfigurer 方式
 */
@Configuration
public class CrosBeanConfig {
    /**
     * WebMvcConfigurer  实现跨域处理
     * springMvc中也可以用
     * @return
     */
    @Bean
    public WebMvcConfigurer crosconfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}

```

4. 在Spring Boot 2.X应用程序中可以使用注解@CrossOrigin
```java
// 放在controller类上 标识类中所有接口都支持跨域访问
@CrossOrigin(origins ={"http://localhost:8080","http://127.0.0.1:8080"})


// 在方法上标识，方法支持跨域访问
@CrossOrigin(origins ={"http://localhost:8080","http://127.0.0.1:8080"})
```

5. Nginx 配置解决跨域问题
```shell
server {
	listen       8090;
	server_name  127.0.0.1 localhost;
 
        #charset koi8-r;
 
        #access_log  logs/host.access.log  main;
 
	location / {
			root   E:/nginx-1.15.0/ssm;
			index  index.html index.htm;
			try_files $uri $uri/ /index.html;
        }
 
	location ~/ssm{
			#Proxy Settings 跨域配置
			add_header Access-Control-Allow-Origin $http_origin;
			add_header Access-Control-Allow-Headers *;
			add_header Access-Control-Allow-Methods *;
 
			proxy_redirect     off;
			proxy_set_header   Host             $host;
			proxy_set_header   X-Real-IP        $remote_addr;
			proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
			proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
			proxy_max_temp_file_size 0;
			proxy_connect_timeout      90;
			proxy_send_timeout         90;
			proxy_read_timeout         90;
			
			proxy_pass http://127.0.0.1:8700;
 
        }
 
        #error_page  404              /404.html;
 
        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
```