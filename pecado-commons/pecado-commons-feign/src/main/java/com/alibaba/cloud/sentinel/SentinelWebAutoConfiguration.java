/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.alibaba.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.feign.sentinel.SentinelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Optional;

/**
 * @author lengleng
 * @date 2020-02-12
 * <p>
 * sentinel 配置
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(SentinelConfiguration.SentinelWebConfiguration.class)
@Slf4j
public class SentinelWebAutoConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private SentinelProperties properties;

    @Autowired
    private Optional<SentinelWebInterceptor> sentinelWebInterceptorOptional;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!sentinelWebInterceptorOptional.isPresent()) {
            return;
        }
        SentinelProperties.Filter filterConfig = properties.getFilter();
        registry.addInterceptor(sentinelWebInterceptorOptional.get())
                .order(filterConfig.getOrder())
                .addPathPatterns(filterConfig.getUrlPatterns());
        log.info(
                "[Sentinel Starter] register SentinelWebInterceptor with urlPatterns: {}.",
                filterConfig.getUrlPatterns());
    }
}
