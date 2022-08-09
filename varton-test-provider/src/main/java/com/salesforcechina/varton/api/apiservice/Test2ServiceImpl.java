package com.salesforcechina.varton.api.apiservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
//@Service
@DubboService
public class Test2ServiceImpl implements Test2Service {
    @Value("${service.tag}")
    private String serviceTag;

    @Override
    public String test2(String orgId) {
        return String.format("test2-provider[%s]", serviceTag);
    }
}
