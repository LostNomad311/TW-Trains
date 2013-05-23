package com.thoughtworks.trains.model;

public interface Route {
	
	public int getDistance();
	
	public Town getStartTown();
	
	public Town getEndTown();

	public String getId();
	
}
