package ucd.declab.sdn.splash;

import java.util.ArrayList;
import java.util.HashMap;

import ucd.declab.sdn.graph.*;
import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.flow.extracts.*;
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
		
		ArrayList<Object> rawNodes = Utilities.readJSONFileAsArrays(nodesFile);
		ArrayList<Object> rawLinks = Utilities.readJSONFileAsArrays(linksFile);
		HashMap<String, Object> rawFlows = Utilities.readJSONFileAsPairs(flowsFile);
		
		TopologyBuilder topologyBuilder = new TopologyBuilder(rawNodes, rawLinks);
		TopologyCollection topoCollection = topologyBuilder.getTopologyCollection();
		
		/*//For testing purposes.
		for (Vertex vertex : topoCollection.getVertices()) {
			System.out.println(vertex.getLabel());
		}*/
		/*
		for (Link link : topoCollection.getLinks()) {
			System.out.println(link.getLabel());
			System.out.println(link.getV1());
			System.out.println(link.getV2() + "\n");
		}*/
		
		FlowBuilder flowBuilder = new FlowBuilder(rawFlows);
		FlowCollection flowCollection = flowBuilder.getFlowCollection();
		
		/* //For testing purposes.
		for (FlowInfo flowInfo : flowCollection) {
			System.out.println(flowInfo.getId());
			System.out.println(flowInfo.getBandwidth());
			System.out.println(flowInfo.getNodeDestination());
			System.out.println(flowInfo.getNodeSource());
			System.out.println(flowInfo.getRelationID());
			System.out.println(flowInfo.getRelationType());
			System.out.println();
		}*/
		
		GraphBuilder graphBuilder = new GraphBuilder(topoCollection);
		graphBuilder.buildGraphStreamTopology();
		graphBuilder.displayPoorGraph(graphBuilder.getGraph(), false);
		
	}
	

}
