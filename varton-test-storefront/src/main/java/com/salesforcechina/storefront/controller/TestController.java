package com.salesforcechina.storefront.controller;

import com.salesforcechina.authentication.p2.service.Test3Service;
import com.salesforcechina.varton.api.apiservice.Test1Service;
import com.salesforcechina.varton.api.apiservice.Test2Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
public class TestController {

    @DubboReference(check = false, lazy = true)
    private Test1Service test1Service;
    @DubboReference(check = false, lazy = true)
    private Test2Service test2Service;
    @DubboReference
    private Test3Service test3Service;

    @Value("${service.tag}")
    private String serviceTag;


    @GetMapping("storefront/test")
    public String test(@RequestHeader("GATEWAY") String prev) {
        String self = String.format("storefront[%s]", serviceTag);
        String next = test1Service.isUserAvailable("110");
        return String.format("%s --> %s --> %s", prev, self, next);
    }

    @GetMapping("storefront/test2")
    public String test2(@RequestHeader("GATEWAY") String prev) {
        String self = String.format("storefront[%s]", serviceTag);
        String next = test1Service.isUserAvailable("110");
        String next2 = test2Service.test2("110");
        return String.format("%s --> %s --> %s --> %s", prev, self, next, next2);
    }

    @GetMapping("storefront/test3")
    public String test3(@RequestHeader("GATEWAY") String prev) {
        String self = String.format("storefront[%s]", serviceTag);
        String next2 = test2Service.test2("110");
        return String.format("%s --> %s --> %s", prev, self, next2);
    }

    // service-discovery
    @GetMapping("storefront/sd")
    public String service(@RequestHeader("GATEWAY") String prev) {
        String self = String.format("storefront[%s]", serviceTag);
        return String.format("%s --> %s", prev, self);
    }

    // a服务调用b服务，a服务参与灰度，b服务不参与灰度。看看调用情况？
    @GetMapping("storefront/test-b")
    public String testB(@RequestHeader("GATEWAY") String prev) {
        String self = String.format("storefront[%s]", serviceTag);
        String next = test3Service.test2("1");
        return String.format("%s --> %s --> %s", prev, self, next);
    }
}
