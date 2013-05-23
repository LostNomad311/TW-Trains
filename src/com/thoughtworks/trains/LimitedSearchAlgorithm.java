package com.thoughtworks.trains;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.vogella.algorithms.dijkstra.model.Edge;
import de.vogella.algorithms.dijkstra.model.Graph;
import de.vogella.algorithms.dijkstra.model.Vertex;

public class LimitedSearchAlgorithm {
	//TODO Test directly
	
  private final List<Edge> edges;
  private int maximumDepth;
  private int minimumDepth;
  private int maximumDistance;
  private int minimumDistance;
  
  public LimitedSearchAlgorithm(Graph graph) {
    // Create a copy of the array so that we can operate on this array
    this.edges = new ArrayList<Edge>(graph.getEdges());
  }
  
  public LinkedList<LinkedList<Vertex>> executeDepthLimited(Vertex source, Vertex target, int maximumDepth, int minimumDepth) {
  	this.maximumDepth = maximumDepth;
  	this.minimumDepth = minimumDepth;
  	LinkedList<LinkedList<Vertex>> result = new LinkedList<LinkedList<Vertex>>();
  	LinkedList<Vertex> path = new LinkedList<Vertex>();
  	depthLimitedSearch(source, target, path, result, 0);
  	
  	return result.size() == 0 ? null : result;
  }
  
  public LinkedList<LinkedList<Vertex>> executeDistanceLimited(Vertex source, Vertex target, int maximumDistance, int minimumDistance) {
  	this.maximumDistance = maximumDistance;
  	this.minimumDistance = minimumDistance;
  	LinkedList<LinkedList<Vertex>> result = new LinkedList<LinkedList<Vertex>>();
  	LinkedList<Vertex> path = new LinkedList<Vertex>();
  	distanceLimitedSearch(source, target, path, result, 0);
  	
  	return result.size() == 0 ? null : result;
  }
  
  private boolean depthLimitedSearch(Vertex node, Vertex target, LinkedList<Vertex> path,
  		LinkedList<LinkedList<Vertex>> paths, int depth) {
  	boolean pathFound = false;
  	if (depth <= maximumDepth) {
  		path.add(node);
  		if (depth >= minimumDepth && node.equals(target)) {
  			paths.add(new LinkedList<Vertex>(path));
  			pathFound = true;
  		}
  		for (Vertex neighbor : getNeighbors(node)) {
  			depthLimitedSearch(neighbor, target, path, paths, depth + 1);
  		}
  		path.removeLast();
  	}
  	
  	return pathFound;
  }
  
  private boolean distanceLimitedSearch(Vertex node, Vertex target, LinkedList<Vertex> path,
  		LinkedList<LinkedList<Vertex>> paths, int distance) {
  	boolean pathFound = false;
  	if (distance <= maximumDistance) {
  		path.add(node);
  		if (distance >= minimumDistance && node.equals(target)) {
  			paths.add(new LinkedList<Vertex>(path));
  			pathFound = true;
  		}
  		for (Vertex neighbor : getNeighbors(node)) {
  			distanceLimitedSearch(neighbor, target, path, paths, distance + getDistance(node, neighbor));
  		}
  		path.removeLast();
  	}
  	
  	return pathFound;
  }
  
  private List<Vertex> getNeighbors(Vertex node) {
  	List<Vertex> neighbors = new LinkedList<Vertex>();
    for (Edge edge : edges) {
    	if (edge.getSource().equals(node)) {
    		neighbors.add(edge.getDestination());
    	}
    }
    
    return neighbors;
  }
  
  private int getDistance(Vertex node, Vertex target) {
    for (Edge edge : edges) {
      if (edge.getSource().equals(node)
          && edge.getDestination().equals(target)) {
        return edge.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }
  
}
