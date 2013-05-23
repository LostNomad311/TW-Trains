package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public class DefaultRouteFactory extends RouteFactory {

	@Override
	public Route createRoute(Town startTown, Town endTown, int distance) throws NullTownException {
		String id = ("" + startTown.getId() + endTown.getId());
		return new DefaultRoute(id, startTown, endTown, distance);
	}

}
