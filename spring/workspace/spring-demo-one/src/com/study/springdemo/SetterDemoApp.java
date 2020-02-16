package com.study.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SetterDemoApp {

	public static void main(String[] args) {
		// load the spring configuration file
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		// retrieve the bean from the spring container
		CircketCoach theCoach = context.getBean("myCricketCoach", CircketCoach.class);
		
		// call methods on bean
		System.out.println(theCoach.getDailyWorkout());
		
		// method call using constructor dependency injection
		System.out.println(theCoach.getDailyFortune());
		
		System.out.println(theCoach.getEmailAddress());
		System.out.println(theCoach.getTeam());
		
		// close the context
		context.close();
	}
}
