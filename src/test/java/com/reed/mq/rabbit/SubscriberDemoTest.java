/** 
 * DemoTest.java
 * 
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.mq.rabbit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
@Configuration
public class SubscriberDemoTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// no Transaction
		// new ClassPathXmlApplicationContext("subscriberTest.xml");
		new ClassPathXmlApplicationContext("handlerTest.xml");
	}
}
