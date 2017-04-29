package com.thoughtworks.trains.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.trains.LimitedSearchAlgorithm;
import com.thoughtworks.trains.exception.DuplicateRouteException;
import com.thoughtworks.trains.exception.DuplicateTownException;
import com.thoughtworks.trains.exception.InvalidRouteException;
import com.thoughtworks.trains.exception.NullRouteException;


import de.vogella.algorithms.dijkstra.engine.DijkstraAlgorithm;
import de.vogella.algorithms.dijkstra.model.Edge;
import de.vogella.algorithms.dijkstra.model.Graph;
import de.vogella.algorithms.dijkstra.model.Vertex;

/**
 * 
 * @author Jerome BG
 * 
 * @deprecated Does not support multi-character Town ids.
 *
 */
public class OneWayTripInformationProvider implements TripInformationProvider {
	//TODO Why aren't routes created through TripInformationProvider? (SoC)
	//TODO The search algorithms can be refactored into a Strategy pattern. DijkstraAlgorithm, being third party could be accompanied by an Adapter or Proxy class (design) 

	private Map<Character, Town> towns;
	private Map<String, Route> routes;
	private TripBuilder tripBuilder;
	
	public static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
	
	public OneWayTripInformationProvider(TripBuilder tripBuilder) {
		this.tripBuilder = tripBuilder;
		
		this.towns = new HashMap<Character, Town>();
		this.routes = new HashMap<String, Route>();
	}
	
	@Override
	public Town addTown(char id) throws DuplicateTownException {
		for (Town existingTown : towns.values()) {
			if (existingTown.getId() == id) {
				throw new DuplicateTownException();
			}
		}
		
		Town result = new DefaultTown(id);
		towns.put(id, result);
		
		return result;
	}

	@Override
	public Map<Character, Town> getTowns() {
		return new HashMap<Character, Town>(towns);
	}

	@Override
	public Route addRoute(Route route) throws DuplicateRouteException, NullRouteException, InvalidRouteException {
		if (route == null) {
			throw new NullRouteException();
		}
		for (Route existingRoute : routes.values()) {
			if (existingRoute.getStartTown().equals(route.getStartTown())
					&& existingRoute.getEndTown().equals(route.getEndTown())) {
				throw new DuplicateRouteException();
			}
		}
		if (!towns.containsKey(route.getStartTown().getId()) || !towns.containsKey(route.getEndTown().getId())) {
			throw new InvalidRouteException();
		}
		
		routes.put(route.getId(), route);
		
		return route;
	}

	@Override
	public Map<String, Route> getRoutes() {
		return new HashMap<String, Route>(routes);
	}
	
	private Map<String, Vertex> getGraphNodes() {
		Map<String, Vertex> nodes = new HashMap<String, Vertex>(towns.size());
		for (Town town : towns.values()) {
			String id = String.valueOf(town.getId());
			nodes.put(id, new Vertex(id, id));
		}
		
		return nodes;
	}
	
	private List<Edge> getGraphEdges(Map<String, Vertex> nodes) {
		List<Edge> edges = new ArrayList<Edge>(routes.size());
		for (Route route : routes.values()) {
			Vertex source = nodes.get(String.valueOf(route.getStartTown().getId()));
			Vertex destination = nodes.get(String.valueOf(route.getEndTown().getId()));
			edges.add(new Edge(route.getId(), source, destination, route.getDistance()));
		}
		
		return edges;
	}
	
	private Graph getGraph() {
		Map<String, Vertex> nodes = getGraphNodes();
		List<Edge> edges = getGraphEdges(nodes);
		return new Graph(new ArrayList<Vertex>(nodes.values()), edges);
	}

	@Override
	public Trip getShortestTrip(Town startTown, Town endTown) throws NullRouteException {
		// Execute DijkstraAlgorithm
		Graph graph = getGraph();
		Map<String, Vertex> nodes = getGraphNodes();
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.execute(nodes.get(String.valueOf(startTown.getId())));
    List<Vertex> tripVertexes = dijkstra.getPath(nodes.get(String.valueOf(endTown.getId())));
    
    if (tripVertexes == null) {
    	return null;
    }
    
    // Build trip
    Trip result = tripBuilder.createTrip();
    for (int idx = 1; idx < tripVertexes.size(); idx++) {
    	Vertex sourceVertex = tripVertexes.get(idx - 1);
    	Vertex destinationVertex = tripVertexes.get(idx);
    	result.addRoute(routes.get(sourceVertex.getId() + destinationVertex.getId()));
    }
    
    return result;
	}

	@Override
	public List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown,
			int maximumStops, int minimumStops) throws NullRouteException {
		// Create DijkstraAlgorithm objects
		Graph graph = getGraph();
		Map<String, Vertex> nodes = getGraphNodes();

		// Execute LimitedSearchAlgorithm
		LimitedSearchAlgorithm depthLimitedSearch = new LimitedSearchAlgorithm(graph);
		LinkedList<LinkedList<Vertex>> tripsVertexes = depthLimitedSearch.executeDepthLimited(
				nodes.get(String.valueOf(startTown.getId())), nodes.get(String.valueOf(endTown.getId())), maximumStops, minimumStops);
		
		if (tripsVertexes == null) {
			return null;
		}
		
    // Build trips
		List<Trip> result = new LinkedList<Trip>();
    for (int idx = 0; idx < tripsVertexes.size(); idx++) {
    	LinkedList<Vertex> tripVertexes = tripsVertexes.get(idx);
    	Trip trip = tripBuilder.createTrip();
    	for (int jdx = 1; jdx < tripVertexes.size(); jdx++) {
      	Vertex sourceVertex = tripVertexes.get(jdx - 1);
      	Vertex destinationVertex = tripVertexes.get(jdx);
      	trip.addRoute(routes.get(sourceVertex.getId() + destinationVertex.getId()));
    	}
    	result.add(trip);
    }
    
    return result;
	}

	@Override
	public List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown,
			int maxiumumDistance, int minimumDistance) throws NullRouteException {
		
		// Create DijkstraAlgorithm objects
		Graph graph = getGraph();
		Map<String, Vertex> nodes = getGraphNodes();

		// Execute LimitedSearchAlgorithm
		LimitedSearchAlgorithm distanceLimitedSearch = new LimitedSearchAlgorithm(graph);
		LinkedList<LinkedList<Vertex>> tripsVertexes = distanceLimitedSearch.executeDistanceLimited(
				nodes.get(String.valueOf(startTown.getId())), nodes.get(String.valueOf(endTown.getId())),
				maxiumumDistance, minimumDistance);
		
		if (tripsVertexes == null) {
			return null;
		}
		
    // Build trips
		List<Trip> result = new LinkedList<Trip>();
    for (int idx = 0; idx < tripsVertexes.size(); idx++) {
    	LinkedList<Vertex> tripVertexes = tripsVertexes.get(idx);
    	Trip trip = tripBuilder.createTrip();
    	for (int jdx = 1; jdx < tripVertexes.size(); jdx++) {
      	Vertex sourceVertex = tripVertexes.get(jdx - 1);
      	Vertex destinationVertex = tripVertexes.get(jdx);
      	trip.addRoute(routes.get(sourceVertex.getId() + destinationVertex.getId()));
    	}
    	result.add(trip);
    }
    
    return result;
	}

	@Override
	public void clearTowns() {
		towns.clear();
	}

	@Override
	public void clearRoutes() {
		routes.clear();
	}

	@Override
	public List<Trip> generateTripsWithMaxStops(Town startTown, Town endTown,
			int maximumStops) throws NullRouteException {
		return generateTripsWithMaxStops(startTown, endTown, maximumStops, 1);
	}

	@Override
	public List<Trip> generateTripsWithMaxDistance(Town startTown, Town endTown,
			int maxiumumDistance) throws NullRouteException {
		return generateTripsWithMaxDistance(startTown, endTown, maxiumumDistance, 1);
	}

	@Override
	public void reset() {
		clearRoutes();
		clearTowns();
	}

	@Override
	public boolean isReady() {
		return routes.size() > 0;
	}

}
