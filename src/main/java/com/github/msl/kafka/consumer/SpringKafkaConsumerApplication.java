package com.github.msl.kafka.consumer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@EnableKafka
public class SpringKafkaConsumerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaConsumerApplication.class, args);		
	}
	
    @Bean
    public ApplicationRunner runner(MeterRegistry registry) {
        return args -> {
            registry.getMeters().forEach(meter -> System.out.println(meter.getId()));
        };
    }
    


}
