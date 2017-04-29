package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public class DefaultUtfRouteFactory extends UtfRouteFactory {

	@Override
	public UtfRoute createRoute(UtfTown startTown, UtfTown endTown, int distance) throws NullTownException {
		String id = ("" + startTown.getUtfId() + endTown.getUtfId());
		return new DefaultUtfRoute(id, startTown, endTown, distance);
	}

}
