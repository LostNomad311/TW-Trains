package com.thoughtworks.trains;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.trains.exception.InvalidCountDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidCountStopsQueryException;
import com.thoughtworks.trains.exception.InvalidDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidGraphQueryException;
import com.thoughtworks.trains.exception.InvalidQueryException;
import com.thoughtworks.trains.exception.NotReadyTripInformationProviderException;
import com.thoughtworks.trains.exception.NullRouteException;
import com.thoughtworks.trains.exception.NullTownException;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.Route;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.Town;
import com.thoughtworks.trains.model.Trip;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class RegExQueryParser implements QueryParser {
	
	// Query RegEx Templates
	private static final String QT_DISTANCE = "(?:distance|length)\\s+(?:trip|route)\\s+(?:from\\s+)?(.+)"; // (?:distance|length)\s+(?:trip|route)\s+(?:from\s+)?(.+)
	private static final String QT_SHORTEST_DISTANCE = "shortest (?:length|distance) from ([a-zA-Z])(?: to |-)([a-zA-Z])"; // shortest (?:length|distance) from ([a-zA-Z])(?: to |-)([a-zA-Z])
	private static final String QT_COUNT_DEPTH_MAX = "(?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) stop[s]? (?:max(?:imum)? (\\d+))\\.?"; // (?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) stop[s]? (?:max(?:imum)? (\d+))\.?
	private static final String QT_COUNT_DEPTH_EXACTLY = "(?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) stop[s]? (?:exactly (\\d+))\\.?"; // (?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) stop[s]? (?:exactly (\d+))\.?
	private static final String QT_COUNT_DISTANCE_MAX = "(?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) distance (?:max(?:imum)? (\\d+))\\.?"; // (?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) distance (?:max(?:imum)? (\d+))\.?
	private static final String QT_COUNT_DISTANCE_EXACTLY = "(?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) distance (?:exactly (\\d+))\\.?"; // (?:count|number of) (?:trip[s]?|route[s]?) from ([a-zA-z])(?: to |-)([a-zA-z]) distance (?:exactly (\d+))\.?
	private static final String QT_TRIP_SPLIT = "( to |-)"; // ( to |-)
	private static final String QT_GRAPH = "^graph .+"; // ^graph.+
	private static final String QT_ROUTE = "([a-zA-Z][a-zA-Z]\\d+)"; // ([a-zA-Z][a-zA-Z]\d+)

	// RegEx Patterns
	private Pattern rpDistance;
	private Pattern rpShortestPath;
	private Pattern rpCountDepthMax;
	private Pattern rpCountDepthExactly;
	private Pattern rpCountDistanceMax;
	private Pattern rpCountDistanceExactly;
	private Pattern rpGraph;
	private Pattern rpRoute;
	
	private TripInformationProvider tripInformationProvider;
	private TripBuilder tripBuilder;
	private RouteFactory routeFactory;
	private List<String> results; //TODO Expand results to an object that includes errors

	public RegExQueryParser(TripInformationProvider tripInformationProvider, TripBuilder tripBuilder, RouteFactory routeFactory) {
		this.tripInformationProvider = tripInformationProvider;
		this.tripBuilder = tripBuilder;
		this.routeFactory = routeFactory;
		buildPatterns();
	}
	
	@Override
	public void parseBlock(String queryBlock) throws InvalidGraphQueryException,
			InvalidCountDistanceQueryException, InvalidCountStopsQueryException, 
			InvalidDistanceQueryException, NullTownException, NullRouteException, 
			NotReadyTripInformationProviderException, InvalidQueryException {
		
		String[] queries = queryBlock.split("[\\r?\\n]+");
		for (String query : queries) {
			try {
				parseQuery(query);
			} catch (Exception ex) {
				System.err.println(ex.toString() + ": " + query);
			}
		}
	}
	
	@Override
	public void parseQuery(String query) throws InvalidGraphQueryException,
			InvalidCountDistanceQueryException, InvalidCountStopsQueryException,
			InvalidDistanceQueryException, NullTownException, NullRouteException, 
			NotReadyTripInformationProviderException, InvalidQueryException {
		
		String q = query.trim();
		if (q.endsWith(".")) {
			q = q.substring(0, q.length() - 1);
		}
		if (!q.isEmpty())
			processQuery(q);
	}
	
	public void clearResults() {
		if (results != null) results.clear();
	}
	
	private void processQuery(String query) throws InvalidGraphQueryException, InvalidCountDistanceQueryException, InvalidCountStopsQueryException, InvalidDistanceQueryException, NullTownException, NullRouteException, NotReadyTripInformationProviderException, InvalidQueryException {
		
		if (results == null) results = new LinkedList<String>();
		boolean match = false;
		Matcher matcher = rpDistance.matcher(query);
		
		if (match = matcher.matches()){
			processDistanceQuery(matcher);
			return;
		}
		matcher = rpShortestPath.matcher(query);
		if (match = matcher.matches()){
			processShortestDistanceQuery(matcher);
			return;
		}
		matcher = rpCountDepthMax.matcher(query);
		if (match = matcher.matches()){
			processCountStopsMaxQuery(matcher);
			return;
		}
		matcher = rpCountDepthExactly.matcher(query);
		if (match = matcher.matches()){
			processCountStopsExactlyQuery(matcher);
			return;
		}
		matcher = rpCountDistanceMax.matcher(query);
		if (match = matcher.matches()){
			processCountDistanceMaxQuery(matcher);
			return;
		}
		matcher = rpCountDistanceExactly.matcher(query);
		if (match = matcher.matches()){
			processCountDistanceExactlyQuery(matcher);
			return;
		}
		matcher = rpGraph.matcher(query);
		if (match = matcher.matches()){
			processGraphQuery(matcher);
			return;
		}
		
		// Invalid query
		if (!match)
			throw new InvalidQueryException();
	}
	
	private void processGraphQuery(Matcher matcher) throws InvalidGraphQueryException {
		tripInformationProvider.reset();
		try {
			Matcher routeMatcher = rpRoute.matcher(matcher.group());
			while (routeMatcher.find()) {
				String routeGraph = routeMatcher.group(1);
				Town startTown = tripInformationProvider.getTowns().get(routeGraph.charAt(0));
				if (startTown == null)
					startTown = tripInformationProvider.addTown(routeGraph.charAt(0));
				Town endTown = tripInformationProvider.getTowns().get(routeGraph.charAt(1));
				if (endTown == null)
					endTown = tripInformationProvider.addTown(routeGraph.charAt(1));
				Integer distance = Integer.valueOf(routeGraph.substring(2));
				tripInformationProvider.addRoute(routeFactory.createRoute(startTown, endTown, distance));
			}
		} catch (Exception e) {
			throw new InvalidGraphQueryException(e);
		}
	}
	
	private Route parseRouteFromMatcher2Groups(Matcher matcher) throws NullTownException, NullRouteException {
		Town startTown = tripInformationProvider.getTowns().get(matcher.group(1).charAt(0));
		Town endTown = tripInformationProvider.getTowns().get(matcher.group(2).charAt(0));

		return routeFactory.createRoute(startTown, endTown, 0);
	}
		
	private Route parseRouteFromMatcher3Groups(Matcher matcher) throws NullTownException, NullRouteException {
		Town startTown = tripInformationProvider.getTowns().get(matcher.group(1).charAt(0));
		Town endTown = tripInformationProvider.getTowns().get(matcher.group(2).charAt(0));
		Integer distance = Integer.valueOf(matcher.group(3));

		return routeFactory.createRoute(startTown, endTown, distance);
	}

	private void processCountDistanceExactlyQuery(Matcher matcher) throws InvalidCountDistanceQueryException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		try {
			Route route = parseRouteFromMatcher3Groups(matcher);
			List<Trip> trips = tripInformationProvider.generateTripsWithMaxDistance(route.getStartTown(), route.getEndTown(), route.getDistance(), route.getDistance());
			
			// Output results
			results.add(String.valueOf(trips.size()));
		} catch (Exception e) {
			throw new InvalidCountDistanceQueryException();
		}
	}

	private void processCountDistanceMaxQuery(Matcher matcher) throws InvalidCountDistanceQueryException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		try {
			Route route = parseRouteFromMatcher3Groups(matcher);
			List<Trip> trips = tripInformationProvider.generateTripsWithMaxDistance(route.getStartTown(), route.getEndTown(), route.getDistance());
			
			// Output results
			results.add(String.valueOf(trips.size()));
		} catch (Exception e) {
			throw new InvalidCountDistanceQueryException();
		}
	}

	private void processCountStopsExactlyQuery(Matcher matcher) throws InvalidCountStopsQueryException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		try {
			Route route = parseRouteFromMatcher3Groups(matcher);
			//NOTE Using distance property to hold the depth
			int depth = route.getDistance();
			List<Trip> trips = tripInformationProvider.generateTripsWithMaxStops(route.getStartTown(), route.getEndTown(), depth, depth);

			// Output results
			results.add(String.valueOf(trips.size()));
		} catch (Exception e) {
			throw new InvalidCountStopsQueryException();
		}
	}

	private void processCountStopsMaxQuery(Matcher matcher) throws InvalidCountStopsQueryException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		try {
			Route route = parseRouteFromMatcher3Groups(matcher);
			//NOTE Using distance property to hold the depth
			int depth = route.getDistance();
			List<Trip> trips = tripInformationProvider.generateTripsWithMaxStops(route.getStartTown(), route.getEndTown(), depth);

			// Output results
			results.add(String.valueOf(trips.size()));
		} catch (Exception e) {
			throw new InvalidCountStopsQueryException();
		}
	}

	private void processShortestDistanceQuery(Matcher matcher) throws NullTownException, NullRouteException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		Route route = parseRouteFromMatcher2Groups(matcher);
		Trip trip = tripInformationProvider.getShortestTrip(route.getStartTown(), route.getEndTown());

		// Output results
		results.add(String.valueOf(trip.getDistance()));
	}

	private void processDistanceQuery(Matcher matcher) throws InvalidDistanceQueryException, NotReadyTripInformationProviderException {
		if (!tripInformationProvider.isReady())
			throw new NotReadyTripInformationProviderException();
		try {
			String[] towns = matcher.group(1).split(QT_TRIP_SPLIT);
			List<Route> routes = new ArrayList<Route>(towns.length - 1);
			
			for (int idx = 1; idx < towns.length; idx++) {
				String startTown = towns[idx - 1];
				String endTown = towns[idx];
				Route route = tripInformationProvider.getRoutes().get(startTown + endTown);
				routes.add(route);
			}
			Trip trip = tripBuilder.createTrip(routes);
			
			// Output results
			results.add(String.valueOf(trip.getDistance()));
		} catch (NullRouteException e) {
			results.add(OneWayTripInformationProvider.NO_SUCH_ROUTE);
		} catch (Exception e) {
			throw new InvalidDistanceQueryException();
		}
	}

	private void buildPatterns() {
		int flags = Pattern.CASE_INSENSITIVE;
		rpDistance = Pattern.compile(QT_DISTANCE, flags);
		rpShortestPath = Pattern.compile(QT_SHORTEST_DISTANCE, flags);
		rpCountDepthMax = Pattern.compile(QT_COUNT_DEPTH_MAX, flags);
		rpCountDepthExactly = Pattern.compile(QT_COUNT_DEPTH_EXACTLY, flags);
		rpCountDistanceMax = Pattern.compile(QT_COUNT_DISTANCE_MAX, flags);
		rpCountDistanceExactly = Pattern.compile(QT_COUNT_DISTANCE_EXACTLY, flags);
		rpGraph = Pattern.compile(QT_GRAPH, flags);
		rpRoute = Pattern.compile(QT_ROUTE, flags);
	}

	@Override
	public List<String> getResults() {
		return new LinkedList<String>(results);
	}

}
