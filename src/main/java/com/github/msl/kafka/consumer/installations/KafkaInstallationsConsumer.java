package com.github.msl.kafka.consumer.installations;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.verisure.vcp.sbn.avro.InstallationDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaInstallationsConsumer {

	@Autowired
	KafkaConsumerConfig consumerConfig;

//	@KafkaListener(topics = {"${consumer.topic.names}"}, containerFactory = "kafkaListenerContainerFactory")
	@KafkaListener(topics = { "SBN_INSTALLATIONS_GBR" }, containerFactory = "kafkaListenerContainerFactory")
	public void avroConsumer(List<ConsumerRecord<Integer, InstallationDTO>> records) {
		log.info("Received {} records.", records.size());
		for (ConsumerRecord<Integer, InstallationDTO> record : records) {
			log.info("Received record with key:" + record.key() + ",  message: " + record.value());
		}
	}

}