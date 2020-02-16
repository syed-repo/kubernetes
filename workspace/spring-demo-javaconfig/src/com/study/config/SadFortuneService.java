package com.study.config;

public class SadFortuneService implements FortuneService {

	@Override
	public String getFortune() {
		return "Not a SAD day today";
	}
	
}
