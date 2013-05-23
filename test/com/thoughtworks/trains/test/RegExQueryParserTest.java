package com.thoughtworks.trains.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.trains.QueryParser;
import com.thoughtworks.trains.RegExQueryParser;
import com.thoughtworks.trains.exception.InvalidCountDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidCountStopsQueryException;
import com.thoughtworks.trains.exception.InvalidDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidGraphQueryException;
import com.thoughtworks.trains.exception.InvalidQueryException;
import com.thoughtworks.trains.exception.NotReadyTripInformationProviderException;
import com.thoughtworks.trains.exception.NullRouteException;
import com.thoughtworks.trains.exception.NullTownException;
import com.thoughtworks.trains.model.DefaultRouteFactory;
import com.thoughtworks.trains.model.DefaultTripBuilder;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class RegExQueryParserTest {

	private static RegExQueryParser regExQueryParser;
	private static TripBuilder tripBuilder;
	private static RouteFactory routeFactory;
	private static TripInformationProvider tripInformationProvider;
	
	// Input
	private static final String GRAPH = "graph AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7.";
	private static final String Q1 = "distance trip A-B-C";
	private static final String Q2 = "length route A-D";
	private static final String Q3 = "distance route from A to D to C.";
	private static final String Q4 = "length trip A-E-B-C-D.";
	private static final String Q5 = "length trip A-E-D.";
	private static final String Q6 = "count trip from C to C stops maximum 3";
	private static final String Q7 = "number of routes from A-C stop exactly 4.";
	private static final String Q8 = "shortest length from A to C.";
	private static final String Q9 = "shortest distance from B-B";
	private static final String Q10 = "number of trips from C-C distance maximum 29";
	
	// Output
	private final String[] expectedResults = {
			null, //NOTE Place holder for parse graph query which has no output
			String.valueOf(9),
			String.valueOf(5),
			String.valueOf(13),
			String.valueOf(22),
			OneWayTripInformationProvider.NO_SUCH_ROUTE,
			String.valueOf(2),
			String.valueOf(3),
			String.valueOf(9),
			String.valueOf(9),
			String.valueOf(7)
	};
	
	@BeforeClass
	public static void setUpClass() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		routeFactory = new DefaultRouteFactory();
		tripBuilder = new DefaultTripBuilder();
		tripInformationProvider = new OneWayTripInformationProvider(tripBuilder);
		regExQueryParser = new RegExQueryParser(tripInformationProvider, tripBuilder, routeFactory);
		
		regExQueryParser.parseQuery(GRAPH);
	}

	@Before
	public void setUp() {
		regExQueryParser.clearResults();
	}
	
	@Test
	public void testParse_GraphQuery() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(GRAPH);
		
		assertEquals("Number of routes", 9, tripInformationProvider.getRoutes().size());
	}

	@Test
	public void testParse_DistanceQuery_ABC() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q1);
		
		assertEquals("Distance", expectedResults[1], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_DistanceQuery_AD() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q2);
		
		assertEquals("Distance", expectedResults[2], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_DistanceQuery_ADC() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q3);
		
		assertEquals("Distance", expectedResults[3], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_DistanceQuery_AEBCD() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q4);
		
		assertEquals("Distance", expectedResults[4], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_DistanceQuery_AED() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q5);
		
		assertEquals("Distance", expectedResults[5], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_CountStopsMaxQuery() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q6);
		
		assertEquals("Number of routes", expectedResults[6], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_CountStopsExactlyQuery() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q7);
		
		assertEquals("Number of routes", expectedResults[7], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_ShortestDistanceQuery_AC() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q8);
		
		assertEquals("Shortest distance", expectedResults[8], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_ShortestDistanceQuery_BB() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q9);
		
		assertEquals("Shortest distance", expectedResults[9], regExQueryParser.getResults().get(0));
	}

	@Test
	public void testParse_CountDistanceMaxQuery() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		regExQueryParser.parseQuery(Q10);
		
		assertEquals("Number of routes", expectedResults[10], regExQueryParser.getResults().get(0));
	}
	
	//TODO testParse_CountDistanceExactlyQuery
	
	@Test
	public void testParseBlock() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		String queryBlock = new StringBuilder().append(GRAPH).append("\n").append(Q1).append("\n").append(Q2).append("\n")
				.append(Q3).append("\n").append(Q4).append("\n").append(Q5).append("\n").append(Q6).append("\n").append(Q7).append("\n").append(Q8).append("\n")
				.append(Q9).append("\n").append(Q10).append("\r\n").toString();
		regExQueryParser.parseBlock(queryBlock);

		List<String> results = regExQueryParser.getResults();
		assertEquals("Number of results", expectedResults.length - 1, results.size());
		for (int idx = 0; idx < results.size(); idx++) {
			assertEquals("Q" + (idx + 1), expectedResults[idx + 1], results.get(idx));
		}
	}
	
	@Test
	public void testParseBlock_2X() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		StringBuilder sb = new StringBuilder().append(GRAPH).append("\r\n").append(Q1).append("\n").append(Q2).append("\n")
				.append(Q3).append("\n").append(Q4).append("\n").append(Q5).append("\n").append(Q6).append("\n").append(Q7).append("\n").append(Q8).append("\n")
				.append(Q9).append("\n").append(Q10).append("\n");
		String queryBlock = sb.append(sb).toString();
		regExQueryParser.parseBlock(queryBlock);

		List<String> results = regExQueryParser.getResults();
		int expectedResultSize = expectedResults.length - 1;
		assertEquals("Number of results", expectedResultSize * 2, results.size());
		for (int idx = 0; idx < expectedResultSize; idx++) {
			assertEquals("Q" + (idx + 1), expectedResults[idx + 1], results.get(idx));
		}
		for (int idx = 0; idx < expectedResultSize; idx++) {
			assertEquals("Q" + (idx + 1), expectedResults[idx + 1], results.get(idx + expectedResultSize));
		}
	}
	
	@Test
	public void testParseBlock2X2() throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		String queryBlock = new StringBuilder().append(GRAPH).append("\n").append(Q1).append("\n").append(Q2).append("\n")
				.append(Q3).append("\n").append(Q4).append("\n").append(Q5).append("\n").append(Q6).append("\n").append(Q7).append("\n").append(Q8).append("\n")
				.append(Q9).append("\n").append(Q10).append("\r\n").toString();
		
		regExQueryParser.parseBlock(queryBlock);
		regExQueryParser.parseBlock(queryBlock);

		List<String> results = regExQueryParser.getResults();
		int expectedResultSize = expectedResults.length - 1;
		assertEquals("Number of results", expectedResultSize * 2, results.size());
		for (int idx = 0; idx < expectedResultSize; idx++) {
			assertEquals("Q" + (idx + 1), expectedResults[idx + 1], results.get(idx));
		}
		for (int idx = 0; idx < expectedResultSize; idx++) {
			assertEquals("Q" + (idx + 1), expectedResults[idx + 1], results.get(idx + expectedResultSize));
		}
	}

}
