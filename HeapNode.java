
public class HeapNode {

	/**
	 * @param args
	 */
	HeapNode child, parent, leftsib, rightsib;
	int nodedata, degree;
	boolean mark;
	
	/** Constructor **/
    public HeapNode(int data)
    {
        this.rightsib = this;
        this.leftsib = this;
        this.nodedata = data;
        this.mark = false;
    }

}
