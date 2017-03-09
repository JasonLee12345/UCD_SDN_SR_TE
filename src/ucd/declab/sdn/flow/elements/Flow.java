package ucd.declab.sdn.flow.elements;

public class Flow {

	private String src;
	private String dst;
	private FlowDetails flowDetails;
	
	public Flow() {
		this.src = "";
		this.dst = "";
		this.flowDetails = new FlowDetails();
	}
	
	public Flow(String src, String dst) {
		this.src = src;
		this.dst = dst;
		this.flowDetails = new FlowDetails();
	}
	
	public Flow(String src, String dst, FlowDetails flowDetails) {
		this.src = src;
		this.dst = dst;
		this.flowDetails = flowDetails;
	}
	
	public void setSrc(String src) { this.src = src; }
	public void setDst(String dst) { this.dst = dst; }
	public void setFlowDetails(FlowDetails flowDetails) { this.flowDetails = flowDetails; }
	
	public String getSrc() { return this.src; }
	public String getDst() { return this.dst; }
	public FlowDetails getFlowDetails() { return this.flowDetails; }
}
