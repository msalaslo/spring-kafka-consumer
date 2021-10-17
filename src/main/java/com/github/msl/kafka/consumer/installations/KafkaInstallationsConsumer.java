package com.github.msl.kafka.consumer.installations;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.verisure.vcp.sbn.avro.InstallationDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaInstallationsConsumer {


	@KafkaListener(topics = { "SBN_INSTALLATIONS_GBR_TABLE" }, containerFactory = "customkafkaListenerContainerFactory")
	public void avroConsumer(List<ConsumerRecord<String, InstallationDTO>> records) {
		log.info("Received {} records.", records.size());
		for (ConsumerRecord<String, InstallationDTO> record : records) {
			InstallationDTO installation = record.value();
			log.info("Received record with key:" + record.key() + ",  installation ADDR: " + installation.getADDR());
		}
	}

}
