package ucd.declab.sdn.flow;

import java.util.ArrayList;
import java.util.HashMap;

import org.graphstream.graph.Node;

import ucd.declab.sdn.flow.elements.*;
import ucd.declab.sdn.flow.extracts.*;

public class FlowBuilder {

	private Flows flows;
	private FlowCollection flowCollection;
	
	public FlowBuilder() {
		this.flows = new Flows();
		this.flowCollection = new FlowCollection();
	}
	
	public FlowBuilder(HashMap<String, Object> rawFlows) {
		this.flows = FlowUtils.extractFlowsFromRaw(rawFlows);
		this.flowCollection = FlowUtils.extractFlowInfoFromFlows(this.flows);
	}
	
	public Flows getFlows() { return this.flows; }
	public FlowCollection getFlowCollection() { return this.flowCollection; }
	
	public void addSegmentsToTrafficFlows(FlowInfo fi, Node[] segments) {
		for (String key : this.flows.keySet()) {
			Flow flow = this.flows.get(key);
			String flowID = flow.getFlowDetails().getId();
			if (fi.getId().contains(flowID)) {
				ArrayList<String> segmentsArrayList = new ArrayList<String>();
				for (int jj = 0;jj < segments.length; jj++) {
					segmentsArrayList.add(segments[jj].getId());
				}
				
				//if (fe.getId().contains("_in")) {
				if (fi.getRelationType().equals(FlowInfo.IN)) {
					flow.getFlowDetails().getIn().setPath(segmentsArrayList.toArray(new String[]{}));
					flow.getFlowDetails().getIn().setAllocated(true);
					return;
				}
				
				//if (fe.getId().contains("_out")) {
				if (fi.getRelationType().equals(FlowInfo.OUT)) {
					flow.getFlowDetails().getOut().setPath(segmentsArrayList.toArray(new String[]{}));
					flow.getFlowDetails().getOut().setAllocated(true);
					return;
				}
			}
		}
	}
}
