package com.study.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloSpringApp {

	public static void main(String[] args) {
		// load the spring configuration file
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		// retrieve the bean from the spring container
		Coach theCoach = context.getBean("myCoach", Coach.class);
		
		// call methods on bean
		System.out.println(theCoach.getDailyWorkout());
		
		// method call using constructor dependency injection
		System.out.println(theCoach.getDailyFortune());
		
		// close the context
		context.close();
	}
}
