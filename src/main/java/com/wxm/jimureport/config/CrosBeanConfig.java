package com.wxm.jimureport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p></p>
 * <p></p>
 *
 * @author 王森明
 * @date 2022/1/27 13:46
 * @since 1.0.0
 */
@Configuration
public class CrosBeanConfig {

    /**
     * 设置跨域请求参数
     * @return
     */
    private CorsConfiguration buildConfiguration(){
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
        return corsConfiguration;
    }

    /**
     * 设置跨域地址
     * @return
     */
    private UrlBasedCorsConfigurationSource buildSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",buildConfiguration());
        return source;
    }

    /**
     * 注册bean
     * @return
     */
   // @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter(buildSource());
    }



    /**
     * WebMvcConfigurer  实现跨域处理
     * springMvc中也可以用
     * @return
     */
    //@Bean
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
