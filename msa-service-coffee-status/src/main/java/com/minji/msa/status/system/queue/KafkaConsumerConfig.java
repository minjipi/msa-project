package com.minji.msa.status.system.queue;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	@Value("${kafka_server_ip}")
	private String kafka_server_ip;
	@Value("${kafka_server_port}")
	private String kafka_server_port;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		String kafka_uri = "http://"+kafka_server_ip+":"+kafka_server_port;
		System.out.println("Consumer kafka_uri : " + kafka_uri);

	    Map<String, Object> properties = new HashMap<>();
	    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_uri);
	    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
	    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    return new DefaultKafkaConsumerFactory<>(properties);
	}
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, String> 
	    kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();

	    kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
	    return kafkaListenerContainerFactory;
	}
}