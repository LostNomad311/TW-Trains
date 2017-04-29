package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public abstract class UtfRouteFactory {

	public abstract UtfRoute createRoute(UtfTown startTown, UtfTown endTown, int distance) throws NullTownException;

}
