package com.thoughtworks.trains;

import java.util.List;

import com.thoughtworks.trains.exception.InvalidCountDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidCountStopsQueryException;
import com.thoughtworks.trains.exception.InvalidDistanceQueryException;
import com.thoughtworks.trains.exception.InvalidGraphQueryException;
import com.thoughtworks.trains.exception.InvalidQueryException;
import com.thoughtworks.trains.exception.NotReadyTripInformationProviderException;
import com.thoughtworks.trains.exception.NullRouteException;
import com.thoughtworks.trains.exception.NullTownException;

public interface QueryParser {
	
	public void parseBlock(String queryBlock) throws InvalidGraphQueryException, 
		InvalidCountDistanceQueryException, InvalidCountStopsQueryException, 
		InvalidDistanceQueryException, NullTownException, NullRouteException, 
		NotReadyTripInformationProviderException, InvalidQueryException;
	
	public void parseQuery(String query) throws InvalidGraphQueryException, 
		InvalidCountDistanceQueryException, InvalidCountStopsQueryException, 
		InvalidDistanceQueryException, NullTownException, NullRouteException, 
		NotReadyTripInformationProviderException, InvalidQueryException;

	public List<String> getResults();
	
}
