package com.study.springdemo;

public class CircketCoach implements Coach {
	
	private FortuneService fortuneService;
	
	// add new private fields for email address and team
	private String emailAddress;
	private String team;
	
	// create no argument constructor
	public CircketCoach() {
		System.out.println("No args constructror in CircketCoach");
	}
	
	public FortuneService getFortuneService() {
		return fortuneService;
	}

	public void setFortuneService(FortuneService fortuneService) {
		System.out.println("Setter method of CircketCoach");
		this.fortuneService = fortuneService;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		System.out.println("Setter: emailAddress");
		this.emailAddress = emailAddress;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		System.out.println("Setter: team");
		this.team = team;
	}

	@Override
	public String getDailyWorkout() {
		return "Practice bowling for 15 minutes";
	}

	@Override
	public String getDailyFortune() {
		return "Cricket " + fortuneService.getFortune();
	}

}
