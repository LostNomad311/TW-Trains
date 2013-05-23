package com.thoughtworks.trains.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.thoughtworks.trains.QueryParser;
import com.thoughtworks.trains.RegExQueryParser;
import com.thoughtworks.trains.model.DefaultRouteFactory;
import com.thoughtworks.trains.model.DefaultTripBuilder;
import com.thoughtworks.trains.model.OneWayTripInformationProvider;
import com.thoughtworks.trains.model.RouteFactory;
import com.thoughtworks.trains.model.TripBuilder;
import com.thoughtworks.trains.model.TripInformationProvider;

public class CommandLineController {
	//TODO Wrap System inputs and outputs as a view (MVC)
	//TODO Improve exception handling for better user feedback
	
	public static void main(String [] args) {
		RouteFactory routeFactory = new DefaultRouteFactory();
		TripBuilder tripBuilder = new DefaultTripBuilder();
		TripInformationProvider tripInformationProvider = new OneWayTripInformationProvider(tripBuilder);
		QueryParser queryParser = new RegExQueryParser(tripInformationProvider, tripBuilder, routeFactory);
		
		// Load script
		Scanner input = null;
		try {
			if (args.length > 1) {
				System.err.println("Usage: %s <script_file_path>");
				System.err.println("	script_file_path - Path to a text file with queries separated by new lines.");
			}
			if (args.length > 0)
			{
				input = new Scanner(new File(args[0]));
			}
		} catch (FileNotFoundException ex) {
      System.err.println("error: the script file could not be loaded.");
      input = null;
		}
		
		try {
			if (input != null) {
				// Display script results
				StringBuilder script = new StringBuilder();
				while (input.hasNextLine()) {
					script.append(input.nextLine()).append("\n");
				}
				input.close();
				System.out.println("Input");
				System.out.println("----------------");
				System.out.println(script.toString());
				try {
					queryParser.parseBlock(script.toString());
				} catch (Exception e) {
					System.err.println("error: error parsing script.");
				}
				System.out.println("Output");
				System.out.println("----------------");
				for (String result : queryParser.getResults()) {
					System.out.println(result);
				}
				System.out.println();
			}
			
			// Prompt for queries
			System.out.println("Enter queries below, type 'exit' to quit");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				String query = br.readLine();
				query = query.trim();
				if (query.toLowerCase().equals("exit")){
					System.exit(0);
				}
				try {
					queryParser.parseQuery(query);
					if (!queryParser.getResults().isEmpty()){
						System.out.println(queryParser.getResults().get(0));
					} else {
						System.out.println();
					}
				} catch (Exception e) {
					System.err.println("error: error parsing query.");
				}
			}
		} catch (Exception e) {
			System.err.println("error: unexpected error, exiting.");
			System.exit(1);
		}
	}
	
}
