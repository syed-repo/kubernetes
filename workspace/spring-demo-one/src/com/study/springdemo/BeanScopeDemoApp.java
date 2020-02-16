package com.study.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanScopeDemoApp {

	public static void main(String[] args) {
		
		// load the spring configuration file
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beanscope-applicationContext.xml");
		
		// retrieve the bean from the spring container
		Coach theCoach = context.getBean("myCoach", Coach.class);
	
		// retrieve the bean from the spring container
		Coach alphaCoach = context.getBean("myCoach", Coach.class);
		
		boolean result = (theCoach == alphaCoach);
		
		System.out.println("Pointing to the same location:" + result);
		
		System.out.println(theCoach);
		System.out.println(alphaCoach);
		
		// call methods on bean
		System.out.println(theCoach.getDailyWorkout());
		
		// method call using constructor dependency injection
		System.out.println(theCoach.getDailyFortune());
	
		// close the context
		context.close();
	}

}
