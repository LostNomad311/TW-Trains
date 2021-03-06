package com.thoughtworks.trains.model;

import java.util.List;
import java.util.Map;

import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.InvalidRouteException;
import com.thoughtworks.trains.exception.NullRouteException;


/**
 * 
 * @author Jerome BG
 * 
 * @deprecated Does not support multi-character Town ids. Replaced by {@link #UtfTripInformationProvider}
 *
 */
public interface TripInformationProvider extends BaseTripInformationProvider {

	Town addTown(char id) throws DuplicateTownException;

	Map<Character, Town> getTowns();

	void clearTowns();

	Route addRoute(Route route) throws DuplicateRouteException, NullRouteException, InvalidRouteException;

	Map<String, Route> getRoutes();

	Trip getShortestTrip(Town startTown, Town endTown) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown, int maximumStops, int minimumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown, int maximumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown, int maxiumumDistance,
			int minimumDistance) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown, int maxiumumDistance) throws NullRouteException;

	void clearRoutes();

}
