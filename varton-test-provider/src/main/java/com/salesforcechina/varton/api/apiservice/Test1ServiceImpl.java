package com.salesforcechina.varton.api.apiservice;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
//@Service
@DubboService
public class Test1ServiceImpl implements Test1Service {

    @Value("${service.tag}")
    private String serviceTag;

    @Override
    public String isUserAvailable(String orgId) {
        return String.format("test1-provider[%s]", serviceTag);
    }
}
