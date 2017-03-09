package ucd.declab.sdn.topology.elements;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TopologyEdgeDetails {

	private double allocated;
	private double capacity;
	private ArrayList<Object> flows;
	private String id;
	private String view;
	
	private String dst_mac;
	private String dst_port;
	private String dst_port_no;
	private String src_mac;
	private String src_port;
	private String src_port_no;
	
	public TopologyEdgeDetails() {
		this.allocated = 0.0;
		this.capacity = 0.0;
		this.flows = new ArrayList<Object>();
		this.id = "";
		this.view = "";
		
		this.dst_mac = "";
		this.dst_port = "";
		this.dst_port_no = "";
		this.src_mac = "";
		this.src_port = "";
		this.src_port_no = "";
	}
	
	public TopologyEdgeDetails(double allo, double capa, String id) {
		this();
		this.allocated = allo;
		this.capacity = capa;
		this.id = id;
	}
	
	public TopologyEdgeDetails(double allo, double capa, String id, String view) {
		this();
		this.allocated = allo;
		this.capacity = capa;
		this.id = id;
		this.view = view;
	}
	
	public TopologyEdgeDetails(double allo, double capa, ArrayList<Object> flows, String id, String view) {
		this();
		this.allocated = allo;
		this.capacity = capa;
		this.flows = flows;
		this.id = id;
		this.view = view;
	}
	
	public void setAllocated(double allo) { this.allocated = allo; }
	public void setCapacity(double capa) { this.capacity = capa; }
	public void setFlows(ArrayList<Object> flows) { this.flows = flows; }
	public void setId(String id) { this.id = id; }
	public void setView(String view) { this.view = view; }
	
	public void setDstMac(String dst_mac) { this.dst_mac = dst_mac; }
	public void setDstPort(String dst_port) { this.dst_port = dst_port; }
	public void setDstPortNumber(String dst_port_no) { this.dst_port_no = dst_port_no; }
	public void setSrcMac(String src_mac) { this.src_mac = src_mac; }
	public void setSrcPort(String src_port) { this.src_port = src_port; }
	public void setSrcPortNumber(String src_port_no) { this.src_port_no = src_port_no; }
	
	public double getAllocated() { return this.allocated; }
	public double getCapacity() { return this.capacity; }
	public ArrayList<Object> getFlows() { return this.flows; }
	public String getId() { return this.id; }
	public String getView() { return this.view; }
	
	public String getDstMac() { return this.dst_mac; }
	public String getDstPort() { return this.dst_port; }
	public String getDstPortNumber() { return this.dst_port_no; }
	public String getSrcMac() { return this.src_mac; }
	public String getSrcPort() { return this.src_port; }
	public String getSrcPortNumber() { return this.src_port_no; }
}
