package com.github.msl.kafka.consumer.installations;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.verisure.vcp.sbn.avro.InstallationDTO;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.Data;

@Component
@Configuration
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaConsumerConfig {

	@NotBlank
	private String topicImages; 
	
	@Autowired
	private SpringKafkaConsumerConfig kafkaProperties;
	
	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, InstallationDTO>> customkafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, InstallationDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(10);
		factory.setBatchListener(true);
		return factory;
	}
	
	@Bean
	public ConsumerFactory<Integer, InstallationDTO> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(getConsumerProperties());
	}
	
	private Map<String, Object> getConsumerProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
//		props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getConsumer().getClientId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		if(kafkaProperties.getConsumer().getSecurity().getProtocol() != null) {
			props.put("security.protocol", kafkaProperties.getConsumer().getSecurity().getProtocol());
		}
		//props.put("sasl.mechanism", consumerSaslMechanism);
		//props.put("sasl.jaas.config", consumerSaslJaasConfig);
		Map<String, String> extraProperties = kafkaProperties.getProperties();
		for (Map.Entry<String, String> entry : extraProperties.entrySet()) {
			if(entry.getValue() != null) {
				props.put(entry.getKey(), entry.getValue());
			}
			
		}
		
		// ensures records are properly converted to the SpecificRecord: AlarmlogDTO
		props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
		return props;
	}
}
