package ucd.declab.sdn.splash;

import java.util.ArrayList;
import java.util.HashMap;

import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.graph.GraphBuilder;
import ucd.declab.sdn.topology.TopologyBuilder;
import ucd.declab.sdn.topology.extracts.TopologyCollection;
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
	
	public void readFiles(String fileExtension) {
		rawNodes = Utilities.readJSONFileAsArrays("topology/" + fileExtension + nodesFile);
		rawLinks = Utilities.readJSONFileAsArrays("topology/" + fileExtension + linksFile);
		rawFlows = Utilities.readJSONFileAsPairs("flow/" +  fileExtension + flowsFile);
	}
	
	// Build the topology, graph and flows.
	public void build() {
		topologyBuilder = new TopologyBuilder(rawNodes, rawLinks);
		TopologyCollection topoCollection = topologyBuilder.getTopologyCollection();
		
		// Use the topology information to build a graph structure.
		graphBuilder = new GraphBuilder(topoCollection);
		graphBuilder.buildGraphStreamTopology();
		//graphBuilder.displayPoorGraph(graphBuilder.getGraph(), false);
		//graphBuilder.displayGraph(graphBuilder.getGraph(), false);
		
		// Dealing with the flows.
		flowBuilder = new FlowBuilder(rawFlows);
	}
	
}
