package com.github.msl.kafka.consumer.installations;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.verisure.vcp.sbn.avro.InstallationDTO;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Data
@Slf4j
public class KafkaConsumerConfig {

	@Value(value = "${consumer.bootstrapAddress}")
	private String consumerBootstrapAddress;

	@Value(value = "${consumer.security.protocol}")
	private String consumerSecurityProtocol;

	@Value(value = "${consumer.sasl.jaas.config}")
	private String consumerSaslJaasConfig;

	@Value(value = "${consumer.sasl.mechanism}")
	private String consumerSaslMechanism;

	@Value(value = "${consumer.topic.names}")
	private List<String> consumerTopicNames;
	
	@Value(value = "${consumer.schema.registry.url}")
	private String consumerSchemaRegistryUrl;
	
	@Value(value = "${consumer.group.id}")
	private String consumerGroupId;


	private Map<String, Object> getConsumerProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getConsumerBootstrapAddress());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, getClientId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		props.put("security.protocol", consumerSecurityProtocol);
		props.put("sasl.mechanism", consumerSaslMechanism);
		props.put("sasl.jaas.config", consumerSaslJaasConfig);
		props.put("schema.registry.url", consumerSchemaRegistryUrl);
		return props;
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, InstallationDTO>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, InstallationDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(1000);
		factory.setBatchListener(true);
		return factory;
	}

	@Bean
	public ConsumerFactory<Integer, InstallationDTO> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(getConsumerProperties());
	}

	private static String getClientId() {
		String clientId = "default";
		try {
			String hostAddress = InetAddress.getLocalHost().getCanonicalHostName();
			clientId = hostAddress;
			log.info("Client Id for kafka producer:" + clientId);
		} catch (UnknownHostException e) {
			log.error("Error getting host name, using default clientId:" + clientId, e);
		}
		return clientId;
	}

}
