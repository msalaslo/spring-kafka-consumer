package com.github.msl.kafka.consumer.installations;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.verisure.vcp.sbn.avro.InstallationDTO;
import com.verisure.vcp.sbn.avro.InstallationInvDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaInstallationsConsumer {


//	@KafkaListener(id = "installations-consumer", topics = { "#{'${kafka.installationsTopic}'}" }, containerFactory = "customkafkaListenerContainerFactory")
	public void installationsConsumer(List<ConsumerRecord<String, InstallationDTO>> records) {
		log.info("Received {} records.", records.size());
		for (ConsumerRecord<String, InstallationDTO> record : records) {
			InstallationDTO installation = record.value();
			log.info("Received record with key:" + record.key() + ",  Sins: " + installation.getSINST()+ ",  InsNo: " + installation.getINSNO());
		}
	}
	
//	@KafkaListener(id = "installations-inv-consumer", topics = { "#{'${kafka.installationsInvTopic}'}" }, containerFactory = "intallationsInvkafkaListenerContainerFactory")
	@KafkaListener(id = "installations-inv-consumer", topics = { "#{'${kafka.installationsInvTopic}'}" })
	public void installationsInvConsumer(ConsumerRecord<String, InstallationInvDTO> record) {

			InstallationInvDTO installation = record.value();
			log.info("Received record with key:" + record.key() + ",  Sins: " + installation.getSINST()+ ",  InsNo: " + installation.getINSNO());
	}

}
