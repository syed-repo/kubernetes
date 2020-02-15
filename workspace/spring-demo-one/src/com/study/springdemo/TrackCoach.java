package com.study.springdemo;

public class TrackCoach implements Coach {
	// define the private field for dependency injection
	FortuneService fortuneService;
	
	public TrackCoach(FortuneService fortuneService) {
		super();
		this.fortuneService = fortuneService;
	}

	@Override
	public String getDailyWorkout() {		
		return "Run a hard 5K";
	}

	@Override
	public String getDailyFortune() {
		return "Just do it " + fortuneService.getFortune();
	}

	public TrackCoach() {
		super();
	}
	
	
}
