package ucd.declab.sdn.segmentrouting;

import java.util.HashMap;
import java.util.Set;

import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import ucd.declab.sdn.flow.extracts.FlowInfo;

public class SegmentRoutingCollection {

	//SegmentRoutingCollectionElement is the innner class of this class for storing basic SR information.
	private HashMap<FlowInfo, SegmentRoutingCollectionElement> collection = null;

	public SegmentRoutingCollection() {
		collection = new HashMap<>();
	}
	
	public Set<FlowInfo> getFlowElements() { return this.collection.keySet();	}
	
	public void addSegmentRoutingElement(FlowInfo flow, Path naturalPath, Node[] segments) {
		this.collection.put(flow, new SegmentRoutingCollectionElement(naturalPath, segments));
	}
	
	public Path getAssignedPath(String flowID) {
		for (FlowInfo f : this.collection.keySet()) {
			if (f.getId().equals(flowID)) {
				return f.getPath();
			}
		}
		
		return null;
	}
	
	public Path getNaturalPath(String flowID) {
		for (FlowInfo f : this.collection.keySet()) {
			if (f.getId().equals(flowID)) {
				return this.collection.get(f).getNaturalPath();
			}
		}
		
		return null;
	}
	
	public Node[] getSegments(String flowID) {
		for (FlowInfo f : this.collection.keySet()) {
			if (f.getId().equals(flowID)) {
				return this.collection.get(f).getSegments();
			}
		}
		
		return null;
	}
	
	
	private class SegmentRoutingCollectionElement {
		private Path naturalPath;
		private Node[] segments;
	
		public SegmentRoutingCollectionElement(Path naturalPath, Node[] segments) {
			this.naturalPath = naturalPath;
			this.segments = segments;
		}
		
		public Path getNaturalPath() { return this.naturalPath; }
		public Node[] getSegments() { return this.segments; }
	}
}
