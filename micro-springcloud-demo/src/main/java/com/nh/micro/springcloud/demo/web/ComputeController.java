package com.nh.micro.springcloud.demo.web;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.nh.micro.service.InjectGroovy;

@InjectGroovy(name="ComputeGroovy")
@RestController
public interface ComputeController {

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    @ResponseBody
    public Integer add(@RequestParam(value="a") Integer a, @RequestParam(value="b") Integer b);

}