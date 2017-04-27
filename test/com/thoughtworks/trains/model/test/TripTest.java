package com.thoughtworks.trains.model.test;

import static org.junit.Assert.*;

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

public class TripTest {
	
	TripBuilder tripBuilder;
	RouteFactory routeFactory;
	TripInformationProvider tripInformationProvider;
	
	@Before
	public void setUp() throws DuplicateTownException, DuplicateRouteException, NullRouteException, NullTownException, InvalidRouteException {
		routeFactory = new DefaultRouteFactory();
		tripBuilder = new DefaultTripBuilder();
		tripInformationProvider = new OneWayTripInformationProvider(tripBuilder);
		
		// Towns
		tripInformationProvider.addTown("A");
		tripInformationProvider.addTown("B");
		tripInformationProvider.addTown("C");
		tripInformationProvider.addTown("D");
		tripInformationProvider.addTown("E");
		
		// Routes
		Map<String, Town> towns = tripInformationProvider.getTowns();
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("A"), towns.get("B"), 5)); //AB5
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("B"), towns.get("C"), 4)); //BC4
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("C"), towns.get("D"), 8)); //CD8
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("D"), towns.get("C"), 8)); //DC8
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("D"), towns.get("E"), 6)); //DE6
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("A"), towns.get("D"), 5)); //AD5
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("C"), towns.get("E"), 2)); //CE2
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("E"), towns.get("B"), 3)); //EB3
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get("A"), towns.get("E"), 7)); //AE7
	}
	
	@Test
	public void testGetDistance_ABC() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AB"));
		tripBuilder.addRoute(routes.get("BC"));
		Trip trip = tripBuilder.getTrip();
		
		int expectedDistance = routes.get("AB").getDistance() + routes.get("BC").getDistance();
		assertEquals("Distance", expectedDistance, trip.getDistance(), .1);
	}
	
	@Test
	public void testGetDistance_AD() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AD"));
		Trip trip = tripBuilder.getTrip();
		
		int expectedDistance = routes.get("AD").getDistance();
		assertEquals("Distance", expectedDistance, trip.getDistance(), .1);
	}
	
	@Test
	public void testGetDistance_ADC() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AD"));
		tripBuilder.addRoute(routes.get("DC"));
		Trip trip = tripBuilder.getTrip();
		
		int expectedDistance = routes.get("AD").getDistance() + routes.get("DC").getDistance();
		assertEquals("Distance", expectedDistance, trip.getDistance(), .1);
	}
	
	@Test
	public void testGetDistance_AEBCD() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AE"));
		tripBuilder.addRoute(routes.get("EB"));
		tripBuilder.addRoute(routes.get("BC"));
		tripBuilder.addRoute(routes.get("CD"));
		Trip trip = tripBuilder.getTrip();
		
		int expectedDistance = routes.get("AE").getDistance() + routes.get("EB").getDistance()
				+ routes.get("BC").getDistance() + routes.get("CD").getDistance();
		assertEquals("Distance", expectedDistance, trip.getDistance(), .1);
	}
	
	@Test (expected = NullRouteException.class)
	public void testGetDistance_AED() throws NullRouteException, InvalidTripRouteException {
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AE"));
		tripBuilder.addRoute(routes.get("ED"));
	}
	
	@Test
	public void testAddRoute() throws NullRouteException {
		tripBuilder.createTrip();
		Trip trip = tripBuilder.getTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		trip.addRoute(routes.get("AB"));
		Route route = routes.get("BC");
		trip.addRoute(route);
		List<Route> tripRoutes = trip.getRoutes();
		
		// 2 routes were added
		assertEquals("Number of routes", 2, tripRoutes.size());
		// The 2nd route added is last in the list
		assertSame("Last Route", route, tripRoutes.get(tripRoutes.size() - 1));
	}
	
	@Test
	public void testClearRoutes() throws NullRouteException {
		tripBuilder.createTrip();
		Trip trip = tripBuilder.getTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		trip.addRoute(routes.get("AB"));
		Route route = routes.get("CD");
		trip.addRoute(route);
		trip.clearRoutes();
		
		assertEquals("Number of Routes", 0, trip.getRoutes().size());
	}

}
