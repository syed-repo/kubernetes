package com.study.config;

import org.springframework.context.annotation.Bean;
/*import org.springframework.context.annotation.ComponentScan;*/
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
/* @ComponentScan("com.study.config") */
@PropertySource("classpath:sport.properties")
public class SportConfig {
	
	// define bean for a SadFortuneService, method name is the actual bean id
	@Bean
	public FortuneService sadFortuneService() {
		return new SadFortuneService();
	}
	
	@Bean
	public Coach swimCoach() {
		return new SwimCoach(sadFortuneService());
	}
}
