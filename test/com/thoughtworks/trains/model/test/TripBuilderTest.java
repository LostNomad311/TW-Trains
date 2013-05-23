package com.thoughtworks.trains.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.InvalidRouteException;
import com.thoughtworks.trains.exception.InvalidTripRouteException;
import com.thoughtworks.trains.exception.NullRouteException;
import com.thoughtworks.trains.exception.NullTownException;
import com.thoughtworks.trains.model.DefaultRouteFactory;
import com.thoughtworks.trains.model.DefaultTripBuilder;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.Route;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.Town;
import com.thoughtworks.trains.model.Trip;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class TripBuilderTest {

	//TODO Refactor duplicate test code
	TripBuilder tripBuilder;
	RouteFactory routeFactory;
	TripInformationProvider tripInformationProvider;
	
	
	@Before
	public void setUp() throws DuplicateTownException, DuplicateRouteException, NullRouteException, NullTownException, InvalidRouteException {
		routeFactory = new DefaultRouteFactory();
		tripBuilder = new DefaultTripBuilder();
		tripInformationProvider = new OneWayTripInformationProvider(tripBuilder);
		
		// Towns
		tripInformationProvider.addTown('A');
		tripInformationProvider.addTown('B');
		tripInformationProvider.addTown('C');
		tripInformationProvider.addTown('D');
		tripInformationProvider.addTown('E');
		
		// Routes
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('A'), towns.get('B'), 5)); //AB5
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('B'), towns.get('C'), 4)); //BC4
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('C'), towns.get('D'), 8)); //CD8
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('D'), towns.get('C'), 8)); //DC8
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('D'), towns.get('E'), 6)); //DE6
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('A'), towns.get('D'), 5)); //AD5
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('C'), towns.get('E'), 2)); //CE2
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('E'), towns.get('B'), 3)); //EB3
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('A'), towns.get('E'), 7)); //AE7
	}
	
	@Test
	public void testAddRoute() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AD"));
		Route route = routes.get("DE");
		tripBuilder.addRoute(route);
		Trip trip = tripBuilder.getTrip();
		List<Route> tripRoutes = trip.getRoutes();
		
		// 2 routes were added
		assertEquals("Number of routes", 2, tripRoutes.size());
		// The 2nd route added is last in the list
		assertSame("Last route", route, tripRoutes.get(tripRoutes.size() - 1));
	}
	
	@Test
	public void testCreateTripWithRoutes() throws NullRouteException, InvalidTripRouteException {
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		List<Route> expTripRoutes = new ArrayList<Route>();
		expTripRoutes.add(routes.get("AB"));
		expTripRoutes.add(routes.get("BC"));
		expTripRoutes.add(routes.get("CD"));
		tripBuilder.createTrip(expTripRoutes);
		Trip trip = tripBuilder.getTrip();
		List<Route> tripRoutes = trip.getRoutes();
		
		assertEquals("Number of routes", expTripRoutes.size(), tripRoutes.size());
		for (int idx = 0; idx < expTripRoutes.size(); idx++) {
			assertSame("Route " + (idx + 1), expTripRoutes.get(idx), tripRoutes.get(idx));
		}
	}
	
	@Test(expected = InvalidTripRouteException.class)
	public void testCreateTripWithInvalidRoutes() throws NullRouteException, InvalidTripRouteException {
		// Assign
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		List<Route> expTripRoutes = new ArrayList<Route>();
		expTripRoutes.add(routes.get("AB"));
		expTripRoutes.add(routes.get("BC"));
		expTripRoutes.add(routes.get("EB"));
		
		// Act
		tripBuilder.createTrip(expTripRoutes);
		
		// Assert
		Trip trip = tripBuilder.getTrip();
		List<Route> tripRoutes = trip.getRoutes();
		
		assertEquals("Number of routes", expTripRoutes.size(), tripRoutes.size());
		for (int idx = 0; idx < expTripRoutes.size(); idx++) {
			assertSame("Route " + (idx + 1), expTripRoutes.get(idx), tripRoutes.get(idx));
		}
	}

}
