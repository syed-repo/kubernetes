package com.study.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaConfigDemoApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new 
				AnnotationConfigApplicationContext(SportConfig.class);
		
		// retrieve the bean from the spring container
		Coach theCoach = context.getBean("tennisCoach", Coach.class);
		
		// call methods on bean
		System.out.println(theCoach.getDailyWorkout());
		
		// close the context
		context.close();
	}
}
