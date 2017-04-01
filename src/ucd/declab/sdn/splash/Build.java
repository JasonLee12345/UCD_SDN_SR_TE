package ucd.declab.sdn.splash;

import java.util.ArrayList;
import java.util.HashMap;

import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.flow.extracts.FlowInfo;
import ucd.declab.sdn.graph.GraphBuilder;
import ucd.declab.sdn.topology.TopologyBuilder;
import ucd.declab.sdn.topology.extracts.Link;
import ucd.declab.sdn.topology.extracts.TopologyCollection;
import ucd.declab.sdn.topology.extracts.Vertex;
import ucd.declab.sdn.utils.Utilities;

public class Build {

	HashMap<String, String> arguments;
	String nodesFile;
	String linksFile;
	String flowsFile;
	boolean DEBUG;
	
	ArrayList<Object> rawNodes;
	ArrayList<Object> rawLinks;
	HashMap<String, Object> rawFlows;
	
	TopologyBuilder topologyBuilder;
	GraphBuilder graphBuilder;
	FlowBuilder flowBuilder;
	
	public Build(String[] args) {
		this.arguments = Utilities.parseCMD(args);
		this.nodesFile = arguments.get(Utilities.CMD_NODES);
		this.linksFile = arguments.get(Utilities.CMD_LINKS);
		this.flowsFile = arguments.get(Utilities.CMD_FLOWS);
		this.DEBUG = Boolean.parseBoolean(arguments.get(Utilities.DEBUG));
	}
	
	public void readFiles(String filePrefixTopo, String filePrefixFlow) {
		rawNodes = Utilities.readJSONFileAsArrays("topology/" + filePrefixTopo + nodesFile);
		rawLinks = Utilities.readJSONFileAsArrays("topology/" + filePrefixTopo + linksFile);
		rawFlows = Utilities.readJSONFileAsPairs("flow/" +  filePrefixFlow + flowsFile);
	}
	
	// Build the topology, graph and flows.
	public void build() {
		topologyBuilder = new TopologyBuilder(rawNodes, rawLinks);
		TopologyCollection topoCollection = topologyBuilder.getTopologyCollection();
		
		/*
		for (Vertex v : topoCollection.getVertices()) {
			System.out.println(v.getLabel());
			System.out.println(v.getType());
			System.out.println(v.getX());
			System.out.println(v.getY());
		}
		
		for (Link l : topoCollection.getLinks()) {
			System.out.println();
			System.out.println(l.getLabel());
			System.out.println(l.getV1() + "  " + l.getV2());
			System.out.println(l.getLoad());
			System.out.println(l.getCapacity());
			System.out.println(l.getType());
		}*/
		
		// Use the topology information to build a graph structure.
		graphBuilder = new GraphBuilder(topoCollection);
		graphBuilder.buildGraphStreamTopology();
		//graphBuilder.displayPoorGraph(graphBuilder.getGraph(), false);
		//graphBuilder.displayGraph(graphBuilder.getGraph(), false);
		
		// Dealing with the flows.
		flowBuilder = new FlowBuilder(rawFlows);
		
		/*
		for (FlowInfo fi : flowBuilder.getFlowCollection()) {
			System.out.println(fi.getId());
			System.out.println(fi.getNodeSource());
			System.out.println(fi.getNodeDestination());
			System.out.println(fi.getBandwidth());
			System.out.println(fi.getRelationID());
			System.out.println(fi.getRelationType());
			System.out.println(fi.getPath());
			System.out.println();
		}*/
	}
	
}
