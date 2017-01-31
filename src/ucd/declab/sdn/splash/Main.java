package ucd.declab.sdn.splash;

import java.util.ArrayList;
import java.util.HashMap;

import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.topology.TopologyBuilder;
import ucd.declab.sdn.utils.Utilities;

public class Main {
	
	/**
	 * @param args
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
		FlowBuilder flowBuilder = new FlowBuilder(rawFlows);
		
		
		
		
	}
	

}
