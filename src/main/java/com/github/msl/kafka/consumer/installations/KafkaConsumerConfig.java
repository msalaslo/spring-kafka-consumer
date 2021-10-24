package com.github.msl.kafka.consumer.installations;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.verisure.vcp.sbn.avro.InstallationDTO;
import com.verisure.vcp.sbn.avro.InstallationInvDTO;

import lombok.Data;

@Component
@Configuration
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaConsumerConfig {

	@NotBlank
	private String intallationsTopic; 
	
	@NotBlank
	private String intallationsInvTopic; 

	@Bean
	public String getIntallationsTopic() {
		return intallationsTopic;
	}
	
	@Bean
	public String getIntallationsInvTopic() {
		return intallationsInvTopic;
	}

	/**
	 * Build a Kafka Listener Container Factory to allow configure the concurrency and other important configurations.
	 * A KafkaListenerContainerFactory implementation to build a ConcurrentMessageListenerContainer that
	 * creates 1 or more KafkaMessageListenerContainers based on concurrency. If the ContainerProperties is configured with TopicPartitions, the TopicPartitions are distributed evenly across the instances.
	 * @param consumerFactory the consumer factory auto-configured by Spring, we use it to have the Kafka Metrics and the Configuration
	 * @return custom configured KafkaListenerContainerFactory
	 */
	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, InstallationDTO>> customkafkaListenerContainerFactory(ConsumerFactory<Integer, InstallationDTO> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Integer, InstallationDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(1);
		factory.getContainerProperties().setPollTimeout(100);
		factory.setBatchListener(true);
		return factory;
	}
	
	/**
	 * Build a Kafka Listener Container Factory to allow configure the concurrency and other important configurations.
	 * A KafkaListenerContainerFactory implementation to build a ConcurrentMessageListenerContainer that
	 * creates 1 or more KafkaMessageListenerContainers based on concurrency. If the ContainerProperties is configured with TopicPartitions, the TopicPartitions are distributed evenly across the instances.
	 * @param consumerFactory the consumer factory auto-configured by Spring, we use it to have the Kafka Metrics and the Configuration
	 * @return custom configured KafkaListenerContainerFactory
	 */
	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, InstallationInvDTO>> intallationsInvkafkaListenerContainerFactory(ConsumerFactory<Integer, InstallationInvDTO> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Integer, InstallationInvDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(1);
		factory.getContainerProperties().setPollTimeout(100);
		factory.setBatchListener(true);
		return factory;
	}
		
}
