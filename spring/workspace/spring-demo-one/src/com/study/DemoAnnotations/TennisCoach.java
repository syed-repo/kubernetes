package com.study.DemoAnnotations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TennisCoach implements Coach {
	
	@Autowired
	@Qualifier("randomFortuneService")
	private FortuneService fortuneService;

	/*
	 * public TennisCoach(FortuneService fortuneService) { super();
	 * this.fortuneService = fortuneService; }
	 */
	
	public TennisCoach() {
		super();
		System.out.println("Inside the default constructor of TennisCoach");
	}

	/*
	 * // setter function for Autowiring ..... actually it can be any method name
	 * 
	 * @Autowired public void setFortuneService(FortuneService fortuneService) {
	 * System.out.println("Inside the set method setFortuneService of TennisCoach "
	 * ); this.fortuneService = fortuneService; }
	 */

	@Override
	public String getDailyWorkout() {
		return "Practice your backhand volley";
	}

	@Override
	public String getDailyFortune() {
		return fortuneService.getFortune();
	}
	
	// define the init method
	@PostConstruct
	public void myInitWork() {
		System.out.println("Init Work");
	}
	
	// define the destroy method
	@PreDestroy
	public void myCleanupWork() {
		System.out.println("Cleanup Work");
	}
	
}
