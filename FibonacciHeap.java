import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FibonacciHeap {

	/**
	 * @param args
	 */
	private HeapNode start;
	public HeapNode max;
	private HeapNode curr;
	int rootcount = 0;
	int totalNodes = 0;
    
	/** Constructor **/
    public FibonacciHeap()
    {
    	start = null;
    }

	//Inserts a node into the fibonacci heap
	public void heapInsertion(HeapNode node) {
		if(start!= null)
    	{
    		curr = start;
    		while(curr.rightsib != start)
    		{
    			curr = curr.rightsib;
    		}
    		node.rightsib = start;
    		node.leftsib = curr;
    		curr.rightsib = node;
    		start.leftsib = node;
    		node.degree = 0;
    		rootcount++;
    		totalNodes++;
    		if(node.nodedata > max.nodedata)
    			max = node;
    	}
    	else
    	{
    		start = node;
    		start.leftsib = start;
    		start.rightsib = start;
    		start.degree = 0;
    		rootcount++;
    		totalNodes++;
    		max = node;
    	}
	}
	
	//Reinsert a removed node back into the heap
	public void reinsertHeapNode(HeapNode node)
    {
    	heapInsertion(node);
    }
    
	//Create a new node in the heap for a new key
    public HeapNode InsertHeapNodes(int data)
    {
    	HeapNode node = new HeapNode(data);
    	node.nodedata = data;
    	heapInsertion(node);
    	return node;
    }
    
	//Remove the maximum node from the heap
    public HeapNode removeMax()
    {
    	HeapNode node = max;
    	int children = 0;
    	HeapNode child = null;
    	HeapNode temp = null;
    	HeapNode temp1 = null;
    	
    	if(max == start)
    	{
    		start = start.rightsib;
    	}
    	//find left right and children
    	HeapNode prev = node.leftsib;
        HeapNode next = node.rightsib;
        children = node.degree;
        
        //disconnect the node
        node.rightsib = node;
        node.leftsib = node;
        rootcount--;
        
        if(children == 0)
        {
        	prev.rightsib = next;
        	next.leftsib = prev;
        }
        else
        {
        	child = node.child;
        	node.child = null;
			//if no nodes are left in the root list update the start pointer to the child of the removed node
        	if(rootcount == 0)
        	{
        		start = child;
        		
        	}
        }
        while(children > 0)
        {
        	temp = child.rightsib;
        	temp1 = child.leftsib;
        	
        	//separate child from the children list
        	temp.leftsib = child.leftsib;
        	temp1.rightsib = child.rightsib;	
        	child.rightsib = child;
        	child.leftsib = child;
        	
        	//add this child to the root list
        	if(rootcount != 0)
        	{
        		prev.rightsib = child;
        		child.leftsib = prev;
        		next.leftsib = child;
        		child.rightsib = next;
        	}
        	//disconnect its parent link
        	child.parent = null;
        	
        	prev = child;
        	if(rootcount == 0)
        		next = child;
        	child = temp;
        	children--;
        	rootcount++;
        }
		//consolidate the heap
        pairwiseCombine();
        return node;
    }
    
	//consolidates the heap by combining nodes of equal degree
    protected void pairwiseCombine()
    {
        int arraySize = totalNodes;
		List<HeapNode> arrDegrees = new ArrayList<HeapNode>(arraySize);
        // Initialize degree array
        for (int i = 0; i < arraySize; i++) {
        	arrDegrees.add(null);
        }

        // Find the number of root nodes.
        int roots = 0;
        HeapNode x = start;
        
        if (x != null) {
        	roots++;
            x = x.rightsib;

            while (x != start) {
                roots++;
                x = x.rightsib;
                
            }
        }
        //find nodes of equal degree in the root list
        while (roots > 0) 
        {        	
            int d = x.degree;
            HeapNode next = x.rightsib;
            for (;;) 
            {
                HeapNode y = arrDegrees.get(d);
                if(y == x)
                	break;
                if (y == null) 
                {
                    break;
                }
                if (x.nodedata < y.nodedata)
                {
                	HeapNode temp = y;
                    y = x;
                    x = temp;
                    
                }
                if(y == start)
                	start = start.rightsib;
               	meld(y, x);
               	arrDegrees.set(d, null);
                d++;
            }
            arrDegrees.set(d, x);
            x = next;
            roots--;
        }
        max = null;
        for (int i = 0; i < arraySize; i++) {
            HeapNode checkNode = arrDegrees.get(i);
            if (checkNode == null) {
                continue;
            }
			//set a new max node
            if (max != null) {

            	checkNode.leftsib.rightsib = checkNode.rightsib;
            	checkNode.rightsib.leftsib = checkNode.leftsib;

            	checkNode.leftsib = max;
            	checkNode.rightsib = max.rightsib;
                max.rightsib = checkNode;
                checkNode.rightsib.leftsib = checkNode;

                // Checking for a new max.
                if (checkNode.nodedata > max.nodedata) {
                    max = checkNode;
                }
            } else {
                max = checkNode;
            }
        }
    }
    
	//make a smaller node the child of the bigger node
    public void meld(HeapNode childnode, HeapNode parentnode)
    {
    	// remove child node from root list of heap
        childnode.leftsib.rightsib = childnode.rightsib;
        childnode.rightsib.leftsib = childnode.leftsib;

        // make child node a child of parent node
        childnode.parent = parentnode;

        if (parentnode.child == null) {
            parentnode.child = childnode;
            childnode.rightsib = childnode;
            childnode.leftsib = childnode;
        } else {
            childnode.leftsib = parentnode.child;
            childnode.rightsib = parentnode.child.rightsib;
            parentnode.child.rightsib = childnode;
            childnode.rightsib.leftsib = childnode;
        }
        parentnode.degree++;
        rootcount--;
        childnode.mark = false;
    }
    
    //Increase the value of a node
    public void IncreaseKey(HeapNode node, int data)
    {
    	int temp = node.nodedata;
    	node.nodedata = node.nodedata + data;
    	if(node.parent != null && node.nodedata > node.parent.nodedata)
    	{
    			HeapNode oldparent = node.parent;
				//remove the node and add it to root list
    			childCut(node);
				//check the parent for childcut value = false and call childcut again
    			cascadingCut(oldparent);
    	}
    	if(node.nodedata > max.nodedata)
    		max = node;
    }
    
	//disconnects the node and inserts it in the root list
    public void childCut(HeapNode node)
    {
    	HeapNode parentnode = node.parent;

        node.leftsib.rightsib = node.rightsib;
        node.rightsib.leftsib = node.leftsib;
        parentnode.degree--;

        if (parentnode.child == node) {
        	parentnode.child = node.rightsib;
        }

        if (parentnode.degree == 0) {
        	parentnode.child = null;
        }

        // add node to root list of heap
        node.leftsib = start;
        node.rightsib = start.rightsib;
        start.rightsib = node;
        node.rightsib.leftsib = node;
        rootcount++;

        node.parent = null;
        node.mark = false;
    }
    
	//recursively call the function until you find a node with childcut value false.
    public void cascadingCut(HeapNode node)
    {
    	if(node.parent != null)
    	{
    		if(node.mark == false)
    		{
				//mark the node as true
    			node.mark = true;
    		}
    		else
    		{
    			HeapNode oldparent = node.parent;
    			childCut(node);
    			cascadingCut(oldparent);
    		}
    	}
    }
}
