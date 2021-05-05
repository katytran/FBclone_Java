package source.Graph;
/* Edited by Canh Hanh Chi Tran */

public final class LinkedQueue<T> implements QueueInterface<T>
{ 
	//QUEUE: LAST IN LAST OUT
	private Node firstNode = null;
	private Node lastNode = null;

	
	/** Adds a new entry to the back of this queue.
	@param newEntry  An object to be added. */
	public void enqueue(T newEntry) {
		Node newNode = new Node(newEntry, null);
		
		if(isEmpty()) {
			firstNode = newNode; 
		} 
		else
			lastNode.next= newNode;
		lastNode = newNode;
		
	}
	
	/** Removes and returns the entry at the front of this queue.
	    @return  The object at the front of the queue. 
	    @throws  EmptyQueueException if the queue is empty before the operation. */
	public T dequeue() {
		
		T data = getFront();
		assert firstNode !=null;
		firstNode.setData(null);
		firstNode = firstNode.getNextNode();
		
		if (firstNode == null)
			lastNode = null;
		return data;
	}
	
	/**  Retrieves the entry at the front of this queue.
	    @return  The object at the front of the queue.
	    @throws  EmptyQueueException if the queue is empty. */
	public T getFront() {
		
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		else 
			return firstNode.getData();
		
	}
	
	/** Detects whether this queue is empty.
	    @return  True if the queue is empty,s or false otherwise. */
	public boolean isEmpty() {
		return (firstNode==null) && (lastNode==null);
		
	}
	
	/** Removes all entries from this queue. */
	public void clear() {
		firstNode = lastNode = null;
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
	}

	
} 

 