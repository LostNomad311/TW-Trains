package com.thoughtworks.trains.model;

import java.util.List;

import com.thoughtworks.trains.exception.InvalidTripRouteException;
import com.thoughtworks.trains.exception.NullRouteException;

public abstract class TripBuilder {
	
	protected Trip trip;

	public abstract Trip createTrip();

	public abstract Trip createTrip(List<Route> routes) throws NullRouteException, InvalidTripRouteException;

	public abstract Trip addRoute(Route route) throws NullRouteException, InvalidTripRouteException;

	public abstract Trip getTrip();

}
