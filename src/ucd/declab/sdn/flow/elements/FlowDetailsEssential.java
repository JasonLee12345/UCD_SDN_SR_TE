package ucd.declab.sdn.flow.elements;

public class FlowDetailsEssential {

	private boolean allocated;
	private String dstPort;
	private String[] path;
    private double size;
    private String srcPort;
    private String type;
    
	public void setAllocated(boolean allocated) { this.allocated = allocated; }
	public void setDstPort(String dstPort) { this.dstPort = dstPort; }
    public void setPath(String[] path) { this.path = path; }
    public void setSize(double size) { this.size = size; }
	public void setSrcPort(String srcPort) { this.srcPort = srcPort; }
	public void setType(String type) { this.type = type; }
	
	public boolean getAllocated() { return this.allocated; }
	public String getDstPort() { return this.dstPort; }
    public String[] getPath() { return this.path; }
    public double getSize() { return this.size; }
    public String getSrcPort() { return this.srcPort; }
	public String getType() { return this.type; }
}
