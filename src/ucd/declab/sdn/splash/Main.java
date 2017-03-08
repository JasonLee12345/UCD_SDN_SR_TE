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
		Build build = new Build(args);
		build.readFiles();
		build.build();
		
		Excecute exce = new Excecute(build.graphBuilder, build.flowBuilder, build.DEBUG);
		exce.excecuteFAA();
		exce.excecuteSR();
		exce.DebugALL();
	}
	
}
