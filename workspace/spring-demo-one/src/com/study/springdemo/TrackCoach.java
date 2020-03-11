package com.study.springdemo;

public class TrackCoach implements Coach {
	// define the private field for dependency injection
	FortuneService fortuneService;
	
	// add a initialization method
	void doMyInit() {
		System.out.println("Init: Do my startup stuff");
	}
	
	// add a destroy method
	void doMyCleanup() {
		System.out.println("Destory: do the clean up stuff");
	}
	
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
