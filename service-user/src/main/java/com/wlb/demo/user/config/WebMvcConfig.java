package com.wlb.demo.user.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wlb.demo.user.advice.CommonExceptionHandlerAdvice;
import com.wlb.demo.user.advice.LocalDateAdvice;
import com.wlb.demo.user.filter.TrimFilter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Web基础配置
 *
 * @author shiyongbiao
 */
@Configuration
@EnableAsync
@Import(value = {LocalDateAdvice.class, CommonExceptionHandlerAdvice.class})
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setProviderClass(HibernateValidator.class);
        return bean;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        AsyncTaskExecutor executor = getAsyncExecutor();
        configurer.setTaskExecutor(executor);
    }

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        // 核心线程数（默认线程数）
        int corePoolSize = 10;
        // 最大线程数
        int maxPoolSize = 20;
        // 允许线程空闲时间（单位：默认为秒）
        int keepAliveTime = 10;
        // 缓冲队列数
        int queueCapacity = 200;
        // 线程池名前缀
        String threadNamePrefix = "Async-Service-";
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    /**
     * 日志记录过滤器
     *
     * @return 日志记录过滤器
     */
    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setIncludeClientInfo(false);
        filter.setMaxPayloadLength(1024);
        return filter;
    }


    /**
     * 过滤请求参数中的前后空格
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<TrimFilter> trimFilter() {
        FilterRegistrationBean<TrimFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new TrimFilter());
        filter.addUrlPatterns("/*");
        filter.setName("TrimFilter");
        filter.setDispatcherTypes(DispatcherType.REQUEST);
        return filter;
    }

    /**
     * 添加拦截器
     *
     * @param registry 　拦截器注册器
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {

    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return (Jackson2ObjectMapperBuilder builder) -> {

            String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(dateFormatPattern);

            builder.failOnUnknownProperties(false)
                    // java8日期序列化/反序列化
                    .deserializers(new LocalDateTimeDeserializer(dateFormat))
                    .serializers(new LocalDateTimeSerializer(dateFormat))
                    // Date序列化/反序列化
                    .simpleDateFormat(dateFormatPattern)
                    // Long类型序列化为String，解决因Long类型数字导致的Js精度丢失问题
                    // .serializerByType(Long.TYPE, ToStringSerializer.instance)
                    .serializerByType(Long.class, ToStringSerializer.instance)
            ;
        };
    }

}
