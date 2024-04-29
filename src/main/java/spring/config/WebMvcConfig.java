package spring.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.ProxyProperties;
import com.github.novicezk.midjourney.support.ApiAuthorizeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private ApiAuthorizeInterceptor apiAuthorizeInterceptor;
    @Resource
    private ProxyProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:doc.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (CharSequenceUtil.isNotBlank(this.properties.getApiSecret())) {
            registry.addInterceptor(this.apiAuthorizeInterceptor)
                    .addPathPatterns("/submit/**", "/task/**", "/account/**");
        }
    }

    // 添加CORS配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径开启CORS
                .allowedOrigins("*") // 允许所有源
                .allowedMethods("*") // 允许所有方法
                .allowedHeaders("*") // 允许所有头信息
                .allowCredentials(true); // 允许凭证
    }
}
