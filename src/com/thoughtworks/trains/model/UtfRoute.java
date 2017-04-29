package com.thoughtworks.trains.model;

public interface UtfRoute {
	
	public int getDistance();
	
	public UtfTown getStartTown();
	
	public UtfTown getEndTown();

	public String getId();
	
}
