package com.minji.msa.status.system.queue;

import com.fasterxml.jackson.databind.JsonNode;
import com.minji.msa.status.domain.vo.StatusVO;
import com.minji.msa.status.springboot.service.StatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumer {

	@Autowired
	private StatusServiceImpl statusServiceImpl;

	@KafkaListener(topics="minji-kafka-test")
    public void processMessage(String jsonString) throws IOException {
		System.out.println("kafkaMessage : =====> " + jsonString);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonString);

		StatusVO statusVO = new StatusVO();
		statusVO.set_id(UUID.randomUUID().toString());
		statusVO.setStatusName("ORDERED");
		statusVO.setOrderId(jsonNode.get("orderId").asText());
		statusVO.setOrderName(jsonNode.get("orderName").asText());
		statusVO.setUserId(jsonNode.get("userId").asText());
		statusVO.setUserName(jsonNode.get("userName").asText());
		StatusVO resultStatusVO = statusServiceImpl.addStatus(statusVO);


		System.out.println("Queue Message from Sender : resultStatusVO is ==> " + resultStatusVO);


    }
}