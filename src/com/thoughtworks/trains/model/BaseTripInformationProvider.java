package com.thoughtworks.trains.model;


//TODO JBG The Command pattern could be used here to simplify the interface and allow additional commands without changing / adding more interfaces
public interface BaseTripInformationProvider {

	void reset();
	
	boolean isReady();

}
