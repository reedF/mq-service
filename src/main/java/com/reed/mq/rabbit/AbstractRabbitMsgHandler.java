package com.reed.mq.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.reed.mq.domain.Message;

/**
 * Abstract Rabbit Msg Handler using @Transactional to control tx
 */
public class AbstractRabbitMsgHandler<T> {

	/** log */
	private Logger logger = LoggerFactory
			.getLogger(AbstractRabbitMsgHandler.class);

	@SuppressWarnings("unchecked")
	@Transactional
	public void handleMessage(Object message) {
		Message<T> m = (Message<T>) message;
		try {
			if (m != null) {
				logger.info(">>>>>>>>>>>rabbit-mq msg:{}", message.toString());
				// business
				this.execute(m.getId(), m.getData());
			}
		} catch (Exception e) {
			logger.error(
					">>>>>>>>>>>msg handler failed to conver msg:{},ex:{}",
					message.toString(), e.getMessage());
		}
	}

	/**
	 * 业务逻辑方法，子类可重写此方法实现各自消息体的后续业务逻辑
	 * 
	 * @param id
	 * @param map
	 * @throws Exception
	 */
	public void execute(String id, T t) throws Exception {
		// TODO
	}

}
