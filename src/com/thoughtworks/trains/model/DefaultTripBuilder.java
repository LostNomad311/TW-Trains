package com.thoughtworks.trains.model;

import java.util.List;

import com.thoughtworks.trains.exception.InvalidTripRouteException;
import com.thoughtworks.trains.exception.NullRouteException;

public class DefaultTripBuilder extends TripBuilder {

	public DefaultTripBuilder() {
		createTrip();
	}
	
	@Override
	public Trip createTrip() {
		return ( trip = new DefaultTrip() );
	}

	@Override
	public Trip createTrip(List<Route> routes) throws NullRouteException, InvalidTripRouteException {
		trip = new DefaultTrip();
		
		for (Route route : routes) {
			addRoute(route);
		}
		
		return trip;
	}

	@Override
	public Trip addRoute(Route route) throws NullRouteException, InvalidTripRouteException {
		if (route == null) {
			throw new NullRouteException();
		}
		List<Route> routes = trip.getRoutes();
		if (routes.size() > 0 && !routes.get(routes.size()-1).getEndTown().equals(route.getStartTown())) {
			throw new InvalidTripRouteException();
		}
		
		trip.addRoute(route);
		
		return trip;
	}

	@Override
	public Trip getTrip() {
		return trip;
	}

}
