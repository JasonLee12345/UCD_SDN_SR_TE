package ucd.declab.sdn.flow.extracts;

import java.util.ArrayList;

import ucd.declab.sdn.flow.elements.Flow;

public class FlowCollection extends ArrayList<FlowInfo> {

	public FlowCollection() {}
	
	public FlowCollection(ArrayList<FlowInfo> data) {
		addAll(data);
	}
}
