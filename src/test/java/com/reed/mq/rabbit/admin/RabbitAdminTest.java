/** 
  * RabbitAdminTest.java
  * 
  * Copyright (c) 2013 by lashou.com.
  */
package com.reed.mq.rabbit.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.reed.mq.rabbit.admin.RabbitAdmin;

/**
 * @author Brody Cai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:adminTest.xml" })
public class RabbitAdminTest extends AbstractJUnit4SpringContextTests {

	/** RabbitAdmin */
	@Autowired
	private RabbitAdmin rabbitAdmin;
	/** 测试用的交换器名称 */
	private static final String TEST_DIRECT_EXCHANGE = "test.directExchange";
	/** 测试用的交换器名称 */
	public final static String EXCHANGE_NAME = "test.topic";
	/** 测试用的队列名称 */
	final static String QUEUE_NAME = "testQueue";
	/** 测试绑定用的routingKey */
	final static String BINDING_KEY = "#.testQueue.#";
	/** 测试路由交换用的routingKey */
	final static String ROUTING_KEY = "testQueue.anotherQueue";
	
	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#declareExchange(java.lang.String, java.lang.String, boolean, boolean, java.util.Map)}.
	 */
	@Test
	public void testDeclareExchangeStringStringBooleanBooleanMapOfStringObject() {
		Assert.notNull(rabbitAdmin, "injected failed");
		rabbitAdmin.declareExchange(EXCHANGE_NAME, ExchangeTypes.TOPIC, true, false, null);
		Assert.isTrue(rabbitAdmin.hasExchange(EXCHANGE_NAME), "交换器创建后应该已存在");
		rabbitAdmin.deleteExchange(EXCHANGE_NAME);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#declareExchange(org.springframework.amqp.core.AbstractExchange)}.
	 */
	@Test
	public void testDeclareExchangeAbstractExchange() {
		rabbitAdmin.declareExchange(new DirectExchange(TEST_DIRECT_EXCHANGE));
		Assert.isTrue(rabbitAdmin.hasExchange(TEST_DIRECT_EXCHANGE), "交换器创建后应该已存在");
		rabbitAdmin.deleteExchange(TEST_DIRECT_EXCHANGE);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#hasExchange(java.lang.String)}.
	 */
	@Test
	public void testHasExchange() {
		Assert.isTrue(rabbitAdmin.hasExchange("amq.direct"), "默认交换器amq.direct应该存在");
		Assert.isTrue(rabbitAdmin.hasExchange("amq.fanout"), "默认交换器amq.fanout应该存在");
		Assert.isTrue(rabbitAdmin.hasExchange("amq.topic"), "默认交换器amq.topic应该存在");
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#deleteExchange(java.lang.String, boolean)}.
	 */
	@Test
	public void testDeleteExchangeStringBoolean() {
		rabbitAdmin.declareExchange(EXCHANGE_NAME, ExchangeTypes.TOPIC, false, false, null);
		Assert.isTrue(rabbitAdmin.hasExchange(EXCHANGE_NAME), "要删除的交换器在删除前应该已存在");
		rabbitAdmin.deleteExchange(EXCHANGE_NAME, true);
		Assert.isTrue(!rabbitAdmin.hasExchange(EXCHANGE_NAME), "交换器创删除后应该已不存在");
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#deleteExchange(java.lang.String)}.
	 */
	@Test
	public void testDeleteExchangeString() {
		rabbitAdmin.declareExchange(EXCHANGE_NAME, ExchangeTypes.TOPIC, false, false, null);
		Assert.isTrue(rabbitAdmin.hasExchange(EXCHANGE_NAME), "要删除的交换器在删除前应该已存在");
		rabbitAdmin.deleteExchange(EXCHANGE_NAME);
		Assert.isTrue(!rabbitAdmin.hasExchange(EXCHANGE_NAME), "交换器创删除后应该已不存在");
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#declareQueue(java.lang.String, boolean, boolean, boolean, java.util.Map)}.
	 */
	@Test
	public void testDeclareQueueStringBooleanBooleanBooleanMapOfStringObject() {
		rabbitAdmin.declareQueue(QUEUE_NAME, true, false, false, null);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "队列创建后应该已存在");
		rabbitAdmin.deleteQueue(QUEUE_NAME);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#declareQueue(java.lang.String)}.
	 */
	@Test
	public void testDeclareQueueString() {
		rabbitAdmin.declareQueue(QUEUE_NAME);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "队列创建后应该已存在");
		rabbitAdmin.deleteQueue(QUEUE_NAME);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#hasQueue(java.lang.String)}.
	 */
	@Test
	public void testHasQueue() {
		rabbitAdmin.declareQueue(QUEUE_NAME);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "队列创建后应该已存在");
		rabbitAdmin.deleteQueue(QUEUE_NAME);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#deleteQueue(java.lang.String, boolean, boolean)}.
	 */
	@Test
	public void testDeleteQueueStringBooleanBoolean() {
		rabbitAdmin.declareQueue(QUEUE_NAME);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "队列创建后应该已存在");
		rabbitAdmin.deleteQueue(QUEUE_NAME, true, true);
		Assert.isTrue(!rabbitAdmin.hasQueue(QUEUE_NAME), "要删除的队列在删除后应该已不复存在");
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#deleteQueue(java.lang.String)}.
	 */
	@Test
	public void testDeleteQueueString() {
		rabbitAdmin.declareQueue(QUEUE_NAME);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "要删除的队列在删除前应该已存在");
		rabbitAdmin.deleteQueue(QUEUE_NAME);
		Assert.isTrue(!rabbitAdmin.hasQueue(QUEUE_NAME), "要删除的队列在删除后应该已不复存在");
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#bindQueue(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testBindQueue() {
		rabbitAdmin.declareQueue(QUEUE_NAME);
		rabbitAdmin.declareExchange(EXCHANGE_NAME, ExchangeTypes.TOPIC, true, false, null);
		Assert.isTrue(rabbitAdmin.hasQueue(QUEUE_NAME), "要绑定的队列在绑定前必须已存在");
		Assert.isTrue(rabbitAdmin.hasExchange(EXCHANGE_NAME), "要绑定的交换器在绑定前必须已存在");
		rabbitAdmin.bindQueue(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);
		rabbitAdmin.unbindQueue(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);
	}

	/**
	 * Test method for {@link com.reed.mq.rabbit.admin.RabbitAdmin#unbindQueue(java.lang.String, java.lang.String, java.lang.String)}.
	 * @see #testBindQueue()
	 */
	@Test
	public void testUnbindQueue() {
		// nothing todo 
	}

}
