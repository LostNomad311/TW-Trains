package com.thoughtworks.trains.model;

import java.util.List;
import java.util.Map;

import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.InvalidRouteException;
import com.thoughtworks.trains.exception.NullRouteException;


public interface UtfTripInformationProvider extends BaseTripInformationProvider {

	UtfTown addUtfTown(String id) throws DuplicateTownException;

	Map<String, UtfTown> getUtfTowns();

	void clearTowns();

	UtfRoute addRoute(UtfRoute route) throws DuplicateRouteException, NullRouteException, InvalidRouteException;

	Map<String, UtfRoute> getRoutes();

	Trip getShortestTrip(UtfTown startTown, UtfTown endTown) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(UtfTown startTown, UtfTown endTown, int maximumStops, int minimumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxStops(UtfTown startTown, UtfTown endTown, int maximumStops) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(UtfTown startTown, UtfTown endTown, int maxiumumDistance,
			int minimumDistance) throws NullRouteException;

	List<Trip> generateTripsWithMaxDistance(UtfTown startTown, UtfTown endTown, int maxiumumDistance) throws NullRouteException;

	void clearRoutes();

}
