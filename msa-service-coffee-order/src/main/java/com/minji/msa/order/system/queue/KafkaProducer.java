package com.minji.msa.order.system.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaProducer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String kafkaTopic, KafkaSendMessage kafkaSendMessage) {
	    	    
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(kafkaSendMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	    kafkaTemplate.send(kafkaTopic, jsonString);
	    
	    System.out.println("KafkaProducer send data from msa-service-coffee-order: " + jsonString);
	}
}