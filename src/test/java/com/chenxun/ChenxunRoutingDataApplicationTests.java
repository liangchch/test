package com.chenxun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.chenxun.demo.service.HelloService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChenxunRoutingDataApplicationTests {
	
	@Autowired
	private HelloService helloService;
	
	@Test
	public void testSay(){
		for(int i=0;i<1000;i++){
			helloService.say();
		}
	}
	


}
