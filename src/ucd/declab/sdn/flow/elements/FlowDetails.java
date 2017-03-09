package ucd.declab.sdn.flow.elements;

public class FlowDetails {

	private String id;
	private FlowDetailsEssential in;
	private FlowDetailsEssential out;
	
	public FlowDetails() {
		this.id = "";
		this.in = new FlowDetailsEssential();
		this.out = new FlowDetailsEssential();
	}
	
	public FlowDetails(String id) {
		this();
		this.id = id;
	}
	
	public FlowDetails(String id, FlowDetailsEssential in, FlowDetailsEssential out) {
		this.id = id;
		this.in = in;
		this.out = out;
	}
	
	public void setId(String id) { this.id = id; }
	public void setIn (FlowDetailsEssential in) { this.in = in; }
	public void setOut (FlowDetailsEssential out) { this.out = out; }
	
	public String getId() { return this.id; }
	public FlowDetailsEssential getIn() { return this.in; }
	public FlowDetailsEssential getOut() { return this.out; }
}
