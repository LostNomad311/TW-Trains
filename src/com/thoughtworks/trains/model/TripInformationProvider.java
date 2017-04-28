package com.thoughtworks.trains.model;

import java.util.List;
import java.util.Map;

import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.InvalidRouteException;
import com.thoughtworks.trains.exception.NullRouteException;


public interface TripInformationProvider {

	/**
	 * Adds a Town to the TripInformationProvider
	 * 
	 * @param id the Town´s id
	 * @return the Town
	 * @throws DuplicateTownException
	 * 
	 * @deprecated Does not support multi-character town ids. Replaced by {@link #addTown(String id)}
	 */
	@Deprecated
	Town addTown(char id) throws DuplicateTownException;

	Town addUtfTown(String id) throws DuplicateTownException;

	/**
	 * Returns the Towns that have been added
	 * 
	 * @return A collection of Towns
	 * @throws DuplicateTownException
	 * 
	 * @deprecated Does not support multi-character town ids. Replaced by {@link #getUtfTowns()}
	 */
	@Deprecated
	Map<Character, Town> getTowns();

	Map<String, Town> getUtfTowns();

	Route addRoute(Route route) throws DuplicateRouteException, NullRouteException, InvalidRouteException;

	Map<String, Route> getRoutes();

	Trip getShortestTrip(Town startTown, Town endTown) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown, int maximumStops, int minimumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown, int maximumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown, int maxiumumDistance,
			int minimumDistance) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown, int maxiumumDistance) throws NullRouteException;

	void clearTowns();

	void clearRoutes();

	void reset();
	
	boolean isReady();

}
