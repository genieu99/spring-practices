package com.poscodx.container.videosystem;

public class IronMan implements DigitalVideoDisc {
	
	private String title = "Iron Man";
	private String studio = "Marvel";
	
	@Override
	public String play() {
		return "Playing Movie " + studio + "'s " + title;
	}
}
