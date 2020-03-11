package com.study.springdemo;

public class BaseballCoach implements Coach {
	
	// define the private field for dependency injection
	FortuneService fortuneService;
	
	public BaseballCoach() {
		super();
	}

	// define the constructor for dependency injection
	BaseballCoach(FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}
	
	@Override
	public String getDailyWorkout() {
		return "Spend 30 minutes of batting practise";
	}
	
	@Override
	public String getDailyFortune() {
		// use my fortuneService to get a fortune
		return fortuneService.getFortune();
	}
}
