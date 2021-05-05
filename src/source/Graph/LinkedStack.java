package source.Graph;
/**   
   @author Canh Hanh Chi Tran
   Wednesday 6 may 2020
   @
*/


public final class LinkedStack<T> implements StackInterface<T>
{

	private Node topNode; // References the first node in the chain
	
	//default constructor
	public LinkedStack(){
		topNode = null;
	}
	
	// Implement the unimplemented methods 
	/** Adds a new entry to the top of this stack.
    @param newEntry  An object to be added to the stack. */
	public void push(T newEntry) {
		topNode = new Node(newEntry, topNode);
		// Node newNode = new Node(newEntry, topNode);
		//  topNode = newNode;
	}

	/** Removes and returns this stack's top entry.
	    @return  The object at the top of the stack. 
	    @throws  EmptyStackException if the stack is empty before the operation. */
	public T pop() {
		T top = peek();
		topNode = topNode.getNextNode();
		return top; 
	}

	/** Retrieves this stack's top entry.
	    @return  The object at the top of the stack.
	    @throws  EmptyStackException if the stack is empty. */
	public T peek() {
		if(isEmpty()) {
			throw new EmptyStackException(); 
		}
		else
			return topNode.getData();
		
	}
	
	/** Detects whether this stack is empty.
	    @return  True if the stack is empty. */
	public boolean isEmpty() {
		return (topNode == null);
	}
	
	/** Removes all entries from this stack. */
	public void clear() {
		topNode = null; 
	}
	

	private class Node
	{
		private T data;
		private Node next;
		
		// Default Constructor
		private Node(T data){
			this(data, null);
		} 
		
		// Constructor
		private Node(T data, Node nextNode){
			this.data = data;
			this.next = nextNode;
		}
		
		// Setter 
		private void setData(T newData){
			data = newData;
		}
		
		private void setNextNode(Node nextNode){
			next = nextNode;
		}
		
		//Getter
		private T getData(){
			return data;
		}
		private Node getNextNode(){      
			return next;
		}
		
	}// end Node

 

}// end LinkedStack