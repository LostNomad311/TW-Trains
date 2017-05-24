package com.thoughtworks.trains.model;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.trains.exception.NullRouteException;

public class DefaultUtfTrip implements UtfTrip {

	private List<UtfRoute> routes;
	
	public DefaultUtfTrip() {
		routes = new LinkedList<UtfRoute>();
	}

	@Override
	public int getDistance() {
		int distance = 0;
		for (UtfRoute route : routes) {
			distance += route.getDistance();
		}
		
		return distance;
	}

	@Override
	public void addRoute(UtfRoute route) throws NullRouteException {
		if (route == null) {
			throw new NullRouteException();
		}
		routes.add(route);
	}

	@Override
	public List<UtfRoute> getRoutes() {
		return new LinkedList<UtfRoute>(routes);
	}

	@Override
	public void clearRoutes() {
		routes.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (UtfRoute route : routes) {
			sb.append(route.getId() + " ");
		}
		
		return sb.toString().trim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)  return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
		
		DefaultUtfTrip that = (DefaultUtfTrip)obj;
		List<UtfRoute> thatRoutes = that.getRoutes();
		List<UtfRoute> thisRoutes = this.getRoutes();
		if (thatRoutes.size() != thisRoutes.size()) return false;
		
		for (int idx=0; idx<thatRoutes.size(); idx++) {
			UtfRoute tripRoute = thatRoutes.get(idx);
			UtfRoute thisRoute = thisRoutes.get(idx);
			if (!tripRoute.equals(thisRoute)) return false;
		}
		
		return true;
	}

}
