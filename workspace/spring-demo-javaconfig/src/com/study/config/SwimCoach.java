package com.study.config;

import org.springframework.beans.factory.annotation.Value;

public class SwimCoach implements Coach {
	
	private FortuneService fortuneService;
	
	@Value("${foo.email}")
	private String emailAddress;
	
	@Value("${foo.team}")
	private String team;

	public SwimCoach(FortuneService fortuneService) {
		super();
		this.fortuneService = fortuneService;
	}
	
	@Override
	public String getDailyWorkout() {
		return "Swim for 100 meters as warm up";
	}
	
	@Override
	public String getDailyFortune() {
		return fortuneService.getFortune();
	}

	public FortuneService getFortuneService() {
		return fortuneService;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getTeam() {
		return team;
	}
	
	

}
