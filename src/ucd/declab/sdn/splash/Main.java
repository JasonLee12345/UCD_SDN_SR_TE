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
import ucd.declab.sdn.utils.CreateJSONForBinaryTree;
import ucd.declab.sdn.utils.Utilities;

public class Main {
	
	/**
	 * @param args
	 * 	nodes_file=nodes.json
		links_file=links.json
		flows_file=flow_catalogue.json
		debug=true
	 */
	public static void main(String[] args) {
		//CreateJSONForBinaryTree JSONFiles = new CreateJSONForBinaryTree(3);
		//JSONFiles.generateAll();
		
		Build build = new Build(args);
		/**
		 * Prefixes that can be selected from: 
		 * Colt_2010_08-153N.
		 * Example1.
		 * Example2.
		 * Example3.
		 * BT2.
		 */
		String filePrefix = "BT3.";
		build.readFiles(filePrefix);
		build.build();
		
		Excecute exce = new Excecute(build.graphBuilder, build.flowBuilder, build.DEBUG);
		exce.excecuteFAA();
		exce.excecuteSR(false);
		exce.DebugALL();
		
	}
	
}
