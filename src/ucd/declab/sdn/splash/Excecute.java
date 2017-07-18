package ucd.declab.sdn.splash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import ucd.declab.sdn.flow.FlowAssignmentAlgorithm;
import ucd.declab.sdn.flow.FlowBuilder;
import ucd.declab.sdn.flow.extracts.FlowCollection;
import ucd.declab.sdn.flow.extracts.FlowInfo;
import ucd.declab.sdn.graph.GraphBuilder;
import ucd.declab.sdn.segmentrouting.SegmentRouting;
import ucd.declab.sdn.segmentrouting.SegmentRoutingCollection;

public class Excecute {
	
	long deltaTimeFlowAssignment;
	long deltaTimeSegmentRouting;
	
	GraphBuilder gb;
	FlowBuilder fb;
	boolean DEBUG;
	
	Graph finalGraph;
	FlowCollection finalTrafficFlowAssignment;
	SegmentRoutingCollection srCollection;
	
	public Excecute(GraphBuilder gb, FlowBuilder fb, boolean DEBUG) {
		this.deltaTimeFlowAssignment = 0;
		this.deltaTimeSegmentRouting = 0;
		this.gb = gb;
		this.fb = fb;
		this.DEBUG = DEBUG;
	}
	
	/**
	 * Flow Assignment Algorithm.
	 * @param isControlled, If this is set, the algorithm will control 
	 * the number of flows that need to be assigned.
	 * @param flowNum, The number of flows that we defined need to be assigned.
	 */
	void excecuteFAA(boolean isControlled, int flowNum) {
		FlowAssignmentAlgorithm faa = new FlowAssignmentAlgorithm();
		faa.init(gb.getGraph());
		FlowCollection finalFlows = new FlowCollection();
		FlowCollection fc = fb.getFlowCollection();
		//int totalFlowNum = fc.size(); 
		//System.out.println("totalFlowNum: " + totalFlowNum);
		
		if (isControlled) {
			for (int i = 0; i < flowNum; ++i) {
				finalFlows.add(fc.get(i));
			}
		} else finalFlows = fc;
		
		faa.setTrafficFlows(finalFlows);
		deltaTimeFlowAssignment = System.currentTimeMillis();
		faa.compute();
		deltaTimeFlowAssignment = System.currentTimeMillis() - deltaTimeFlowAssignment;
		faa.terminate();
		
		finalGraph = faa.getUpdatedGraph();
		finalTrafficFlowAssignment = faa.getFlowAssignment();
		//gb.displayGraphWithFlows(finalGraph, finalTrafficFlowAssignment, false);

		System.out.println("Total Number of Flows Need to be Assigned: " + flowNum);
		System.out.println("Total Number of Flows Have been Assigned: " + finalTrafficFlowAssignment.size() + "\n");
	}

	// Segment Routing Algorithm.
	void excecuteSR(boolean isTE) {
		SegmentRouting.isTE = isTE;
		srCollection = new SegmentRoutingCollection();
		
		Graph g;
		if (isTE)
			g = finalGraph;
		else
			g = gb.getGraph();
		
		deltaTimeSegmentRouting = System.currentTimeMillis();
		for (FlowInfo fi : finalTrafficFlowAssignment) {
			Path assignedPath = fi.getPath();
			Path naturalPath = SegmentRouting.getNaturalPath(g, fi.getNodeSource(), fi.getNodeDestination());
			
			try {
				Node[] segments = SegmentRouting.getSegments(g, assignedPath);
				srCollection.addSegmentRoutingElement(fi, naturalPath, segments);
				fb.addSegmentsToTrafficFlows(fi, segments);
			}
			catch (Exception e) {
				e.printStackTrace();
				srCollection.addSegmentRoutingElement(fi, naturalPath, new Node[0]);
				fb.addSegmentsToTrafficFlows(fi, new Node[0]);
			}
		}
		deltaTimeSegmentRouting = System.currentTimeMillis() - deltaTimeSegmentRouting;	
	}
	
	void DebugALL() {
		if (DEBUG) {
			DebugFAA();
			DebugSR();
		}
	}

	// Display the Flow Assignment Algorithm results.
	void DebugFAA() {
		System.out.println("FLOW ASSIGNMENT ALGORITHM:");
		System.out.println("Time used for Flow Assignment: " + deltaTimeFlowAssignment + "\n");
		
		/*System.out.println("\tID\t\tFlow\t\tBandwidth\t\tPath");
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
		System.out.println();*/
	}

	// Display the Segment Routing Algorithm results.
	void DebugSR() {
		System.out.println("SEGMENT ROUTING ALGORITHM:");
		System.out.println("Time used for Segment Routing: " + deltaTimeSegmentRouting + "\n");
		
		/*HashMap<Integer, Integer> SRDistribution = new HashMap<Integer, Integer>();
		for (FlowInfo fi : srCollection.getFlowElements()) {
			try {
				Node[] segments = srCollection.getSegments(fi.getId());
				Integer num = SRDistribution.get(segments.length);
				if (num == null) num = 1;
				else num++;
				SRDistribution.put(segments.length, num);
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		Iterator it = SRDistribution.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pair = (HashMap.Entry)it.next();
	        System.out.println("SR Path Length " + pair.getKey() + " = " + pair.getValue());
	        it.remove();
	    }*/
	    
		/*HashMap<Integer, Integer> TEDistribution = new HashMap<Integer, Integer>();
		for (FlowInfo fi : srCollection.getFlowElements()) {
			Path assignedPath = srCollection.getAssignedPath(fi.getId());
			Integer num = TEDistribution.get(assignedPath.size());
			if (num == null) num = 1;
			else num++;
			TEDistribution.put(assignedPath.size(), num);
		}
		
		Iterator it = TEDistribution.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pair = (HashMap.Entry)it.next();
	        System.out.println("TE Path Length " + pair.getKey() + " = " + pair.getValue());
	        it.remove();
	    }*/
		
		HashMap<Integer, Integer> NaturalDistribution = new HashMap<Integer, Integer>();
		for (FlowInfo fi : srCollection.getFlowElements()) {
			Path naturalPath = srCollection.getNaturalPath(fi.getId());
			Integer num = NaturalDistribution.get(naturalPath.size());
			if (num == null) num = 1;
			else num++;
			NaturalDistribution.put(naturalPath.size(), num);
		}
		
		Iterator it = NaturalDistribution.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pair = (HashMap.Entry)it.next();
	        System.out.println("Natural Path Length " + pair.getKey() + " = " + pair.getValue());
	        it.remove();
	    }
		
		/*for (FlowInfo fi : srCollection.getFlowElements()) {
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
		}*/
	}
}
