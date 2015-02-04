/** 
 * RabbitMessageServiceTest.java
 * 
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.mq.rabbit;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.reed.mq.domain.Message;

/**
 * 
 */
@ContextConfiguration(locations = { "classpath:publisherTest.xml" })
public class RabbitPublisherServiceTest extends
		AbstractJUnit4SpringContextTests {
	/**  */
	@Autowired
	private RabbitPublisherService rabbitPublisherService;

	/**
	 * Test method for
	 * {@link com.reed.mq.rabbit.RabbitPublisherService#send(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testSend() {
		Date d = new Date();
		Message<String> message = new Message<String>();
		message.setCreate_time(d.getTime());
		message.setData(new String("test"));
		message.setId(String.valueOf(d.getTime()));
		message.setMethod("test");
		rabbitPublisherService.send("test", message);
	}

}
