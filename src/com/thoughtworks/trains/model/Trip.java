package com.thoughtworks.trains.model;

import java.util.List;

import com.thoughtworks.trains.exception.NullRouteException;

public interface Trip {

	int getDistance();

	void addRoute(Route route) throws NullRouteException;

	List<Route> getRoutes();

	void clearRoutes();
	
	//TODO This should be mandatory for each implementing class
	//boolean equals(Object obj);

}
