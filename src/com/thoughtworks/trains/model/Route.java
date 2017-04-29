package com.thoughtworks.trains.model;

/**
 * 
 * @author Jerome BG
 * 
 * @deprecated Does not support multi-character Town ids. Replaced by {@link #UtfRoute}
 *
 */
public interface Route {
	
	public int getDistance();
	
	public Town getStartTown();
	
	public Town getEndTown();

	public String getId();
	
}
