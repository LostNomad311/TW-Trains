package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

public abstract class RouteFactory {

	public abstract Route createRoute(Town startTown, Town endTown, int distance) throws NullTownException;

}
