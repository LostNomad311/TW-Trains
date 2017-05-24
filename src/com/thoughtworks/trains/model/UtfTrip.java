package com.thoughtworks.trains.model;

import java.util.List;

import com.thoughtworks.trains.exception.NullRouteException;

/**
 * 
 * @author Jerome BG
 * 
 */
public interface UtfTrip {

	int getDistance();

	void addRoute(UtfRoute route) throws NullRouteException;

	List<UtfRoute> getRoutes();

	void clearRoutes();
	
	boolean equals(Object obj);

}
