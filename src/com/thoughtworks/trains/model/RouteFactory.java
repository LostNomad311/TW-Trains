package com.thoughtworks.trains.model;

import com.thoughtworks.trains.exception.NullTownException;

/**
 * 
 * @author Jerome BG
 * 
 * @deprecated Does not support multi-character Town ids. Replaced by {@link #UtfRouteFactory}
 *
 */
public abstract class RouteFactory {

	public abstract Route createRoute(Town startTown, Town endTown, int distance) throws NullTownException;

}
