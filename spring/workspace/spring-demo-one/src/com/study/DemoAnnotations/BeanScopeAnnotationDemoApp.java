package com.study.DemoAnnotations;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanScopeAnnotationDemoApp {

	public static void main(String[] args) {
		// load the spring configuration file
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("annotations-applicationContext.xml");
		
		// retrieve the bean from the spring container
		Coach theCoach = context.getBean("tennisCoach", Coach.class);
		// call methods on bean
		System.out.println(theCoach.getDailyWorkout());
		
		System.out.println(theCoach.getDailyFortune());

		Coach alphaCoach = context.getBean("tennisCoach", Coach.class);
		boolean result = (theCoach == alphaCoach);
		
		System.out.println("Both Object is equal: " + result);

		// close the context
		context.close();

	}

}
