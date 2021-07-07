package com.minji.msa.order.system.queue;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Value("${kafka_server_ip}")
	private String kafka_server_ip;
	@Value("${kafka_server_port}")
	private String kafka_server_port;

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		String kafka_uri = "http://"+kafka_server_ip+":"+kafka_server_port;
		System.out.println("producer kafka_uri : " + kafka_uri);
	    Map<String, Object> configProps = new HashMap<>();
		//configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_uri);
	    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    return new DefaultKafkaProducerFactory<>(configProps);
	}
 
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
	    return new KafkaTemplate<>(producerFactory());
	}
}