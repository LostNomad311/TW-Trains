package com.thoughtworks.trains.model;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.trains.exception.NullRouteException;

public class DefaultTrip implements Trip {

	private List<Route> routes;
	
	public DefaultTrip() {
		routes = new LinkedList<Route>();
	}

	@Override
	public int getDistance() {
		int distance = 0;
		for (Route route : routes) {
			distance += route.getDistance();
		}
		
		return distance;
	}

	@Override
	public void addRoute(Route route) throws NullRouteException {
		if (route == null) {
			throw new NullRouteException();
		}
		routes.add(route);
	}

	@Override
	public List<Route> getRoutes() {
		return new LinkedList<Route>(routes);
	}

	@Override
	public void clearRoutes() {
		routes.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Route route : routes) {
			sb.append(route.getId() + " ");
		}
		
		return sb.toString().trim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)  return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
		
		DefaultTrip that = (DefaultTrip)obj;
		List<Route> thatRoutes = that.getRoutes();
		List<Route> thisRoutes = this.getRoutes();
		if (thatRoutes.size() != thisRoutes.size()) return false;
		
		for (int idx=0; idx<thatRoutes.size(); idx++) {
			Route tripRoute = thatRoutes.get(idx);
			Route thisRoute = thisRoutes.get(idx);
			if (!tripRoute.equals(thisRoute)) return false;
		}
		
		return true;
	}

}
