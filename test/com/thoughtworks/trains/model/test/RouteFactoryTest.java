package com.thoughtworks.trains.model.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.NullTownException;
import com.thoughtworks.trains.model.DefaultRouteFactory;
import com.thoughtworks.trains.model.DefaultTripBuilder;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.Route;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.Town;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class RouteFactoryTest {

	TripBuilder tripBuilder;
	RouteFactory routeFactory;
	TripInformationProvider tripInformationProvider;

	@Before
	public void setUp() throws DuplicateTownException {
		routeFactory = new DefaultRouteFactory();
		tripBuilder = new DefaultTripBuilder();
		tripInformationProvider = new OneWayTripInformationProvider(tripBuilder);
		
		// Towns
		tripInformationProvider.addTown("A");
		tripInformationProvider.addTown("B");
		tripInformationProvider.addTown("C");
		tripInformationProvider.addTown("D");
		tripInformationProvider.addTown("E");
	}
	
	@Test
	public void testCreateRoute() throws DuplicateRouteException, NullTownException {
		Map<String, Town> towns = tripInformationProvider.getTowns();
		Town startTown = towns.get("A");
		Town endTown = towns.get("B");
		Route route = routeFactory.createRoute(startTown, endTown, 5); //AB5
		
		assertEquals("" + startTown.getId() + endTown.getId(), route.getId());
		assertEquals(5, route.getDistance(), .1);
		assertEquals(towns.get("A"), route.getStartTown());
		assertEquals(towns.get("B"), route.getEndTown());
	}

}
