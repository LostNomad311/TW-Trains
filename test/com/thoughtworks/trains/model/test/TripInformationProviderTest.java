package com.thoughtworks.trains.model.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
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
import com.thoughtworks.trains.model.DefaultTown;
import com.thoughtworks.trains.model.DefaultTripBuilder;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.Route;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.Town;
import com.thoughtworks.trains.model.Trip;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class TripInformationProviderTest {
	
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
	
	//TODO These tests need to support different trips with the same, shortest lengths. To do so, a method that calculates all of the shortest routes is required.
	@Test
	public void testGetShortestTrip_FromAToC() 
			throws NullRouteException, InvalidTripRouteException, NullTownException {
		// 8. The length of the shortest route (in terms of distance to travel) from A to C.
		
		// Build shortest trip
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("AB"));
		tripBuilder.addRoute(new DefaultRouteFactory().createRoute(new DefaultTown('B'), new DefaultTown('C'), 4));
		Trip shortestTrip = tripBuilder.getTrip();
		
		// Execute test
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		Trip trip = tripInformationProvider.getShortestTrip(towns.get('A'), towns.get('C'));
		
		assertEquals("Trip", shortestTrip, trip);
	}

	@Test
	public void testGetShortestTrip_FromBToB() 
			throws NullRouteException, InvalidTripRouteException {
		// 9. The length of the shortest route (in terms of distance to travel) from B to B.
		
		// Build shortest trip
		tripBuilder.createTrip();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		tripBuilder.addRoute(routes.get("BC"));
		tripBuilder.addRoute(routes.get("CE"));
		tripBuilder.addRoute(routes.get("EB"));
		Trip shortestTrip = tripBuilder.getTrip();
		
		// Execute test
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		Trip trip = tripInformationProvider.getShortestTrip(towns.get('B'), towns.get('B'));
		
		assertEquals("Trip", shortestTrip, trip);
	}
		
	@Test
	public void testGenerateTripsWithMaxStops_FromCToCMax3() throws NullRouteException, InvalidTripRouteException {
		// 6. The number of trips starting at C and ending at C with a maximum of 3 stops.  In the sample data below, there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
		
		// Build trips
		List<Trip> expectedTrips = new LinkedList<Trip>();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		// C-D-C
		tripBuilder.createTrip();
		tripBuilder.addRoute(routes.get("CD"));
		tripBuilder.addRoute(routes.get("DC"));
		expectedTrips.add(tripBuilder.getTrip());
		// C-E-B-C
		tripBuilder.createTrip();
		tripBuilder.addRoute(routes.get("CE"));
		tripBuilder.addRoute(routes.get("EB"));
		tripBuilder.addRoute(routes.get("BC"));
		expectedTrips.add(tripBuilder.getTrip());
		
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		Town startTown = towns.get('C');
		Town endTown = towns.get('C');
		List<Trip> trips = tripInformationProvider.generateTripsWithMaxStops(startTown, endTown, 3);
		
		// Same number of trips
		assertEquals("Number of trips", expectedTrips.size(), trips.size());
		// The expected trips were generated
		for (Trip trip : trips) {
			boolean match = false;
			for(Trip expTrip : expectedTrips) {
				if (expTrip.equals(trip)) {
					match = true;
					break;
				}
			}
			
			assertTrue("Trip does not exist: " + trip.toString(), match);
		}
	}
	
	@Test
	public void testGenerateTripsWithMaxStops_FromAToCMax4Min4() throws NullRouteException, InvalidTripRouteException {
		// 7. The number of trips starting at A and ending at C with exactly 4 stops.  In the sample data below, there are three such trips: A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).

		// Build trips
		List<Trip> expectedTrips = new LinkedList<Trip>();
		Map<String, Route> routes = tripInformationProvider.getRoutes();
		// A-B-C-D-C
		tripBuilder.createTrip();
		tripBuilder.addRoute(routes.get("AB"));
		tripBuilder.addRoute(routes.get("BC"));
		tripBuilder.addRoute(routes.get("CD"));
		tripBuilder.addRoute(routes.get("DC"));
		expectedTrips.add(tripBuilder.getTrip());
		// A-D-C-D-C
		tripBuilder.createTrip();
		tripBuilder.addRoute(routes.get("AD"));
		tripBuilder.addRoute(routes.get("DC"));
		tripBuilder.addRoute(routes.get("CD"));
		tripBuilder.addRoute(routes.get("DC"));
		expectedTrips.add(tripBuilder.getTrip());
		// A-D-E-B-C
		tripBuilder.createTrip();
		tripBuilder.addRoute(routes.get("AD"));
		tripBuilder.addRoute(routes.get("DE"));
		tripBuilder.addRoute(routes.get("EB"));
		tripBuilder.addRoute(routes.get("BC"));
		expectedTrips.add(tripBuilder.getTrip());
		
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		Town startTown = towns.get('A');
		Town endTown = towns.get('C');
		List<Trip> trips = tripInformationProvider.generateTripsWithMaxStops(startTown, endTown, 4, 4);

		// Same number of trips
		assertEquals("Number of trips", expectedTrips.size(), trips.size());
		// The expected trips were generated
		for (Trip trip : trips) {
			boolean match = false;
			for(Trip expTrip : expectedTrips) {
				if (expTrip.equals(trip)) {
					match = true;
					break;
				}
			}
			
			assertTrue("Trip does not exist: " + trip.toString(), match);
		}
	}
	
	@Test
	public void testGenerateTripsWithMaxDistance_FromCToC() throws NullRouteException {
		// 10.The number of different routes from C to C with a distance of less than 30.  In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.

		//TODO Build trips
		// C-D-C
		// C-E-B-C
		// C-E-B-C-D-C
		// C-D-C-E-B-C
		// C-D-E-B-C
		// C-E-B-C-E-B-C
		// C-E-B-C-E-B-C-E-B-C
		
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		Town startTown = towns.get('C');
		Town endTown = towns.get('C');
		List<Trip> trips = tripInformationProvider.generateTripsWithMaxDistance(startTown, endTown, 29);
		
		assertEquals("Number of trips", 7, trips.size());
	}
	
	@Test
	public void testAddTown() throws DuplicateTownException {
		int oldTownCount = tripInformationProvider.getTowns().size();
		tripInformationProvider.addTown('T');
		
		assertEquals("Number of towns", oldTownCount + 1, tripInformationProvider.getTowns().size());
		//NOTE The ordering of the towns is not important
	}
	
	@Test (expected = DuplicateTownException.class)
	public void testAddTown_Duplicate() throws DuplicateTownException {
		tripInformationProvider.addTown('T');
		tripInformationProvider.addTown('T');
	}
	
	@Test
	public void testClearTowns() {
		tripInformationProvider.clearTowns();
		
		assertEquals("Number of towns", 0, tripInformationProvider.getTowns().size());
	}
	
	@Test
	public void testAddRoute() throws DuplicateRouteException, NullRouteException, NullTownException, InvalidRouteException {
		int oldRouteCount = tripInformationProvider.getRoutes().size();
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('E'), towns.get('C'), 11)); //EC11
				
		assertEquals("Number of routes", oldRouteCount + 1, tripInformationProvider.getRoutes().size());
		//NOTE The order of the routes is not important
	}

	@Test(expected = DuplicateRouteException.class)
	public void testAddRoute_Duplicate() throws DuplicateRouteException, NullRouteException, NullTownException, InvalidRouteException {
		Map<Character, Town> towns = tripInformationProvider.getTowns();
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('E'), towns.get('C'), 11)); //EC11
		tripInformationProvider.addRoute(routeFactory.createRoute(towns.get('E'), towns.get('C'), 11)); //EC11
	}
	
	@Test
	public void testClearRoutes() {
		tripInformationProvider.clearRoutes();
		
		assertEquals("Number of routes", 0, tripInformationProvider.getRoutes().size());
	}
	
	@Test
	public void testReset() {
		tripInformationProvider.reset();

		assertEquals("Number of routes", 0, tripInformationProvider.getRoutes().size());
		assertEquals("Number of towns", 0, tripInformationProvider.getTowns().size());
	}

}
