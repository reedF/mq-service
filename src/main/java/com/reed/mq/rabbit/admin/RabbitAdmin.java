/**
 * RabbitAdmin.java
 *
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.mq.rabbit.admin;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * RabbitMQ管理,包括某个vhost下的交换器管理、队列管理和绑定管理
 * 
 * 
 */
public class RabbitAdmin {
	/**
	 * log
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** RabbitTemplate */
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 创建一个交换器
	 * 
	 * @param exchange
	 *            交换器名称
	 * @param type
	 *            交换器类型
	 * @param durable
	 *            是否持久化.持久化后当RabbitMQ重启后交换器依然存在,建议设置为true
	 * @param autoDelete
	 *            是否自动删除.建议设为false
	 * @param arguments
	 *            其它参数
	 * @return state
	 */
	public final AMQP.Exchange.DeclareOk declareExchange(final String exchange,
			final String type, final boolean durable, final boolean autoDelete,
			final Map<String, Object> arguments) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Exchange.DeclareOk>() {
					public AMQP.Exchange.DeclareOk doInRabbit(
							final Channel channel) throws IOException {
						return channel.exchangeDeclare(exchange, type, durable,
								autoDelete, arguments);
					}
				});
	}

	/**
	 * 创建一个交换器
	 * 
	 * @param exchange
	 *            交换器
	 * @return state
	 * @see org.springframework.amqp.core.DirectExchange
	 * @see org.springframework.amqp.core.TopicExchange
	 * @see org.springframework.amqp.core.FanoutExchange
	 */
	public final AMQP.Exchange.DeclareOk declareExchange(
			final AbstractExchange exchange) {
		return declareExchange(exchange.getName(), exchange.getType(),
				exchange.isDurable(), exchange.isAutoDelete(),
				exchange.getArguments());
	}

	/**
	 * 检验交换器是否存在
	 * 
	 * @param exchangeName
	 *            交换器名称
	 * @return true,存在;false,不存在.当网络连接不上时会导致判断错误,即使该交换器已存在也会因为连接不上判断为不存在
	 */
	public final boolean hasExchange(final String exchangeName) {
		return rabbitTemplate.execute(new ChannelCallback<Boolean>() {
			public Boolean doInRabbit(final Channel channel) {
				try {
					// 检测exchange是否存在，不存在则抛出IO异常
					channel.exchangeDeclarePassive(exchangeName);
				} catch (IOException e) {
					logger.info(getClass().getName() + "#hasExchange", e);
					return false;
				}
				return true;
			}
		});
	}

	/**
	 * 删除交换器
	 * 
	 * @param exchangeName
	 *            交换器名称
	 * @param forceDelete
	 *            是否强制删除。true,不管该交换器是否被使用都删除; false, 被使用的情况下不删除
	 * @return state
	 */
	public final AMQP.Exchange.DeleteOk deleteExchange(
			final String exchangeName, final boolean forceDelete) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Exchange.DeleteOk>() {
					public AMQP.Exchange.DeleteOk doInRabbit(
							final Channel channel) throws IOException {
						if (forceDelete) {
							// 直接删除，不考虑该交换器是否在使用
							return channel.exchangeDelete(exchangeName);
						} else {
							// 只有该交换器未被使用的情况下才删除
							return channel.exchangeDelete(exchangeName, true);
						}
					}
				});
	}

	/**
	 * 强制删除交换器，不管其是否正在被使用
	 * 
	 * @param exchangeName
	 *            交换器名称
	 * @return state
	 */
	public final AMQP.Exchange.DeleteOk deleteExchange(final String exchangeName) {
		return deleteExchange(exchangeName, true);
	}

	/**
	 * 声明一个队列
	 * 
	 * @param queueName
	 *            队列名称
	 * @param durable
	 *            是否持久化.持久化后当RabbitMQ重启后队列依然存在,建议设置为true
	 * @param exclusive
	 *            是否独占.若为true,则只有当前连接可使用该队列.建议设置为false
	 * @param autoDelete
	 *            是否自动删除.建议设为false
	 * @param arguments
	 *            其它参数
	 * @return state
	 */
	public final AMQP.Queue.DeclareOk declareQueue(final String queueName,
			final boolean durable, final boolean exclusive,
			final boolean autoDelete, final Map<String, Object> arguments) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Queue.DeclareOk>() {
					public AMQP.Queue.DeclareOk doInRabbit(final Channel channel)
							throws IOException {
						return channel.queueDeclare(queueName, durable,
								exclusive, autoDelete, arguments);
					}
				});
	}

	/**
	 * 创建一个持久化队列
	 * 
	 * @param queueName
	 *            队列名称
	 * @return state
	 */
	public final AMQP.Queue.DeclareOk declareQueue(final String queueName) {
		return declareQueue(queueName, true, false, false, null);
	}

	/**
	 * 校验队列是否存在
	 * 
	 * @param queueName
	 *            队列名称
	 * @return true,存在;false,不存在.当网络连接不上时会导致判断错误,即使该队列已存在也会因为连接不上判断为不存在
	 */
	public final boolean hasQueue(final String queueName) {
		return rabbitTemplate.execute(new ChannelCallback<Boolean>() {
			public Boolean doInRabbit(final Channel channel) {
				try {
					// 检测queue是否存在，不存在则抛出IO异常
					channel.queueDeclarePassive(queueName);
				} catch (IOException e) {
					logger.info(getClass().getName() + "#hasQueue", e);
					return false;
				}
				return true;
			}
		});
	}

	/**
	 * 删除队列.队列删除后,与其相关的绑定也会被删除
	 * 
	 * @param queueName
	 *            队列名称
	 * @param ifUnused
	 *            true,只有在要删除的队列未被使用的情况下才删除;false,不考虑要删除的队列是否正在被使用
	 * @param ifEmpty
	 *            true,只有在要删除的队列为空的情况下才删除;false,不考虑要删除的队列是否为空
	 * @return state
	 */
	public final AMQP.Queue.DeleteOk deleteQueue(final String queueName,
			final boolean ifUnused, final boolean ifEmpty) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Queue.DeleteOk>() {
					public AMQP.Queue.DeleteOk doInRabbit(final Channel channel)
							throws IOException {
						return channel
								.queueDelete(queueName, ifUnused, ifEmpty);
					}
				});
	}

	/**
	 * 强制删除队列.不考虑要删除的队列是否正在被使用以及是否为空的情况,立即删除
	 * 
	 * @param queueName
	 *            队列名称
	 * @return state
	 * @see #deleteQueue(String, boolean, boolean)
	 */
	public final AMQP.Queue.DeleteOk deleteQueue(final String queueName) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Queue.DeleteOk>() {
					public AMQP.Queue.DeleteOk doInRabbit(final Channel channel)
							throws IOException {
						return channel.queueDelete(queueName);
					}
				});
	}

	/**
	 * 绑定队列
	 * 
	 * @param queueName
	 *            队列名称
	 * @param exchangeName
	 *            交换器名称
	 * @param routingKey
	 *            路由关键字
	 * @return state
	 */
	public final AMQP.Queue.BindOk bindQueue(final String queueName,
			final String exchangeName, final String routingKey) {
		return rabbitTemplate.execute(new ChannelCallback<AMQP.Queue.BindOk>() {
			public AMQP.Queue.BindOk doInRabbit(final Channel channel)
					throws IOException {
				return channel.queueBind(queueName, exchangeName, routingKey);
			}
		});
	}

	/**
	 * 解除绑定
	 * 
	 * @param queueName
	 *            队列名称
	 * @param exchangeName
	 *            交换器名称
	 * @param routingKey
	 *            路由关键字
	 * @return state
	 */
	public final AMQP.Queue.UnbindOk unbindQueue(final String queueName,
			final String exchangeName, final String routingKey) {
		return rabbitTemplate
				.execute(new ChannelCallback<AMQP.Queue.UnbindOk>() {
					public AMQP.Queue.UnbindOk doInRabbit(final Channel channel)
							throws IOException {
						return channel.queueUnbind(queueName, exchangeName,
								routingKey);
					}
				});

	}

}
