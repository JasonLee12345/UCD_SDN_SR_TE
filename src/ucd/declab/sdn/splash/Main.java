package ucd.declab.sdn.splash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import ucd.declab.sdn.graph.*;
import ucd.declab.sdn.flow.FlowAssignmentAlgorithm;
import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.flow.extracts.*;
import ucd.declab.sdn.segmentrouting.*;
import ucd.declab.sdn.topology.*;
import ucd.declab.sdn.topology.extracts.*;
import ucd.declab.sdn.utils.Utilities;

public class Main {
	
	/**
	 * @param args
	 * 	nodes_file=topology/nodes.json
		links_file=topology/links.json
		flows_file=flow/flow_catalogue.json
		debug=true
	 */
	public static void main(String[] args) {
		HashMap<String, String> arguments = Utilities.parseCMD(args);
		String nodesFile = arguments.get(Utilities.CMD_NODES);
		String linksFile = arguments.get(Utilities.CMD_LINKS);
		String flowsFile = arguments.get(Utilities.CMD_FLOWS);
		boolean DEBUG = Boolean.parseBoolean(arguments.get(Utilities.DEBUG));
		
		ArrayList<Object> rawNodes = Utilities.readJSONFileAsArrays(nodesFile);
		ArrayList<Object> rawLinks = Utilities.readJSONFileAsArrays(linksFile);
		HashMap<String, Object> rawFlows = Utilities.readJSONFileAsPairs(flowsFile);
		
		
		// Build the topolgy.
		TopologyBuilder topologyBuilder = new TopologyBuilder(rawNodes, rawLinks);
		TopologyCollection topoCollection = topologyBuilder.getTopologyCollection();
		
		
		// Use the topology information to build a graph stucture.
		GraphBuilder graphBuilder = new GraphBuilder(topoCollection);
		graphBuilder.buildGraphStreamTopology();
		//graphBuilder.displayPoorGraph(graphBuilder.getGraph(), false);
		//graphBuilder.displayGraph(graphBuilder.getGraph(), false);
		
		
		// Dealing with the flows.
		FlowBuilder flowBuilder = new FlowBuilder(rawFlows);
		FlowCollection flowCollection = flowBuilder.getFlowCollection();
		
		// Flow Assignment Algorithm.
		long deltaTimeFlowAssignment;
		long deltaTimeSegmentRouting;
		
		FlowAssignmentAlgorithm faa = new FlowAssignmentAlgorithm();
		faa.init(graphBuilder.getGraph());
		faa.setTrafficFlows(flowCollection);
		deltaTimeFlowAssignment = System.currentTimeMillis();
		faa.compute();
		deltaTimeFlowAssignment = System.currentTimeMillis() - deltaTimeFlowAssignment;
		faa.terminate();
		
		Graph finalGraph = faa.getUpdatedGraph();
		FlowCollection finalTrafficFlowAssignment = faa.getFlowAssignment();
		graphBuilder.displayGraphWithFlows(finalGraph, finalTrafficFlowAssignment, false);
		
		// Display the flow assignment results.
		if (DEBUG) {
			System.out.println("FLOW ASSIGNMENT ALGORITHM:");
			System.out.println("Time used for Flow Assignment: " + deltaTimeFlowAssignment);
			
			System.out.println("\tID\t\tFlow\t\tBandwidth\t\tPath");
			for (FlowInfo fi : finalTrafficFlowAssignment) {
				System.out.print("\t" + fi.getId());
				System.out.print("\t\t(" + fi.getNodeSource() + "," + fi.getNodeDestination() + ")");
				System.out.print("\t\t" + fi.getBandwidth());
				System.out.print("\t\t\t");
				String path = "";
				for (Edge e : fi.getPath().getEdgeSet()) {
					path += "(" + e.getSourceNode() + "," + e.getTargetNode() + "),";				
				}
				if (path.length() > 0) {
					System.out.print(path.substring(0, path.length() - 1));
				}
				else {
					System.out.println("NO PATH!");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		// Segment Routing Algorithm.
		SegmentRoutingCollection srCollection = new SegmentRoutingCollection();
		deltaTimeSegmentRouting = System.currentTimeMillis();
		
		for (FlowInfo fi : finalTrafficFlowAssignment) {
			Path assignedPath = fi.getPath();
			Path naturalPath = SegmentRouting.getNaturalPath(graphBuilder.getGraph(), fi.getNodeSource(), fi.getNodeDestination());
			try {
				Node[] segments = SegmentRouting.getSegments(graphBuilder.getGraph(), assignedPath);
				srCollection.addSegmentRoutingElement(fi, naturalPath, segments);
				flowBuilder.addSegmentsToTrafficFlows(fi, segments);
			}
			catch (Exception e) {
				e.printStackTrace();
				srCollection.addSegmentRoutingElement(fi, naturalPath, new Node[0]);
				flowBuilder.addSegmentsToTrafficFlows(fi, new Node[0]);
			}
		}
		deltaTimeSegmentRouting = System.currentTimeMillis() - deltaTimeSegmentRouting;
		
		if (DEBUG) {
			System.out.println();
			System.out.println("SEGMENT ROUTING ALGORITHM:");
			System.out.println("Time used for Segment Routing: " + deltaTimeSegmentRouting);
			
			int countFlows = 0;
			for (FlowInfo fi : srCollection.getFlowElements()) {
				System.out.println("\tFlow: (" + fi.getNodeSource() + "," + fi.getNodeDestination() + "): " + fi.getId());
				
				Path assignedPath = srCollection.getAssignedPath(fi.getId());
				Path naturalPath = srCollection.getNaturalPath(fi.getId());
				
				System.out.println("\t\tAssigned path:\t" + assignedPath + " --> Len = " + assignedPath.size());
				System.out.println("\t\tNatural path:\t" + naturalPath + " --> Len = " + naturalPath.size());
				
				try {
					Node[] segments = srCollection.getSegments(fi.getId());
					System.out.println("\t\tSegments:\t" + Arrays.toString(segments) + " --> Len = " + segments.length);
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
				System.out.println();
				countFlows ++;
			}
			System.out.println("\tTotal Number of Flows: " + countFlows);
		}
		
	}
	

}
