package com.study.DemoAnnotations;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomFortuneService implements FortuneService {

	String[] messages = { "Beware of sheep in wolf clothing",
			"Diligence is the mother of good work",
			"Journey is the reward" };
	
	private Random random = new Random();
	
	@Override
	public String getFortune() {
		int index = random.nextInt(messages.length);
		return messages[index];
	}
}
