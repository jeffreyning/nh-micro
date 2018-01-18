package com.nh.micro.springcloud.demo;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class DemoServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoServiceApplication.class).web(true).run(args);
	}

}
