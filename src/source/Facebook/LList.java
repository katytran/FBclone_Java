/*
 @author Canh Hanh Chi Tran
  wednesday April 29, 2020
*/
package source.Facebook;
public class LList<T> implements ListInterface<T>
{
	private Node firstNode; // Reference to first node of chain
	private int numberOfEntries;
	
	public LList () {
		initializeDataFields();
	}
	
	/** Removes all entries from this list. */
	public void clear() {
		initializeDataFields();
	}
	
	/** Adds a new entry to the end of this list.
	Entries currently in the list are unaffected.
	The list's size is increased by 1.
	* @param newEntry The object to be added as a new entry.
	*/
	public void add(T newEntry) {
		Node newNode = new Node(newEntry);
		if (isEmpty())
			firstNode = newNode;
		else
		{
			Node lastNode = getNodeAt(numberOfEntries);
			lastNode.setNextNode(newNode);  
			// make last node reference new node
		}
		numberOfEntries++;
	}


	/** Adds a new entry at a specified position within this list.
	* Entries originally at and above the specified position
	* are at the next higher position within the list.
	* The list's size is increased by 1.
	* @param newPosition An integer that specifies the desired
	* position of the new entry.
	* @param newEntry The object to be added as a new entry.
	 * @throws Exception 
	* @throws IndexOutOfBoundsException if either
	*    newPosition less than 1, or
	*    newPosition greater than getLength()+1. 
	*/
	public void add(int newPosition, T newEntry) {
		if ((newPosition>=1) && (newPosition<= numberOfEntries+1)) {
			Node newNode = new Node(newEntry);
			if (newPosition==1) {
				newNode.setNextNode(firstNode); // make new node reference first node
				firstNode = newNode;
			}
			else {
				Node nodeBefore = getNodeAt(newPosition-1);
			    Node nodeAfter = nodeBefore.getNextNode();
			    newNode.setNextNode(nodeAfter); 
			    nodeBefore.setNextNode(newNode);
			} 
			numberOfEntries++;
		}
		else 
			throw new IndexOutOfBoundsException("Illegal position given to add operation");
	}


	/** Removes the entry at a given position from this list.
	* Entries originally at positions higher than the given
	* position are at the next lower position within the list,
	* and the list's size is decreased by 1.
	* @param givenPosition An integer that indicates the position of
	* the entry to be removed.
	* @return A reference to the removed entry.
	* @throws IndexOutOfBoundsException if either
	*    givenPosition less than 1, or
	*    givenPosition greater than getLength()+1. 
	*/
	public T remove(int givenPosition){
		T removeData = null; 
		if ((givenPosition>=1)&& (givenPosition<=numberOfEntries)){
			if(givenPosition==1) {
				removeData = firstNode.getData();
				firstNode = firstNode.getNextNode(); 
			}
			else {
				Node nodeBefore = getNodeAt(givenPosition-1);
				Node nodeToRemove = nodeBefore.getNextNode(); 
				removeData = nodeToRemove.getData();
				Node nodeAfter  = nodeToRemove.getNextNode();
				nodeBefore.setNextNode(nodeAfter);
			}
			numberOfEntries--;
			return removeData; 
		}
		else
			throw new IndexOutOfBoundsException("Illegal Position given to remove operation.");
	
	}




	/** Replaces the entry at a given position in this list.
	* @param givenPosition An integer that indicates the position of the
	* entry to be replaced.
	* @param newEntry The object that will replace the entry at the
	* position givenPosition.
	* @return The original entry that was replaced.
	* @throws IndexOutOfBoundsException if either
	*    givenPosition less than 1, or
	*    givenPosition greater than getLength()+1. 
	*/
	public T replace(int givenPosition, T newEntry){
		if ((givenPosition>=1)&& (givenPosition<=numberOfEntries)){
			assert !isEmpty();
			Node desiredNode = getNodeAt(givenPosition);
			T result = desiredNode.getData();
			desiredNode.setData(newEntry);
			return result; 
		}
		else
			throw new IndexOutOfBoundsException("Ilegal position given to remove operation.");	
	} // end replace


	/** Retrieves the entry at a given position in this list.
	* @param givenPosition An integer that indicates the position of
	* the desired entry.
	* @return A reference to the indicated entry.
	* @throws IndexOutOfBoundsException if either
	*    givenPosition less than 1, or
	*    givenPosition greater than getLength()+1. 
	*/
	public T getEntry(int givenPosition) {
		if ((givenPosition>=1)&& (givenPosition<=numberOfEntries)){
			assert !isEmpty();
			Node desiredNode = getNodeAt(givenPosition);
			T result = desiredNode.getData();
			return result; 
			// == return getNodeAt(givenPosition).getData(); 
		}
		else 
			throw new IndexOutOfBoundsException("Illegal position given to getEntry operation."); 
	}


	/** Sees whether this list contains a given entry.
	* @param anEntry The object that is the desired entry.
	* @return True if the list contains anEntry, or false if not. 
	*/
	public boolean contains(T anEntry) {
		boolean found = false; 
		Node currentNode = firstNode;
		while (!found && (currentNode!= null)) {
			if(anEntry.equals(currentNode.getData()))
				found = true;
			else
				currentNode= currentNode.getNextNode();
		} // end while
		return found;
	} // end contains


	/** Gets the length of this list.
	* @return The integer number of entries currently in the list. 
	*/
	public int getLength(){
		return numberOfEntries;
	}


	/** Sees whether this list is empty.
	* @return True if the list is empty, or false if not.
	*/
	public boolean isEmpty(){
		boolean result;
		if (numberOfEntries==0) {
			assert firstNode ==null;
			result = true;
		}
		else {
			assert firstNode !=null;
			result = false;
		}
		return result; 
	}


	/** Retrieves all entries that are in this list in the order in which
	they occur in the list. 
	@return A newly allocated array of all the entries in the list.
	*/
	public T[] toArray() {
		// The cast is safe because the new array contains null entries
		@SuppressWarnings("unchecked")
		T[] result = (T[]) new Object[numberOfEntries];
		int index = 0;
		Node currentNode = firstNode;
		while ((index < numberOfEntries) && (currentNode != null)) {
			result[index]=currentNode.getData();
			currentNode= currentNode.getNextNode();
			index++;
		}
		return result; 
	}
	
	
	
	// Initializes the class's data fields to indicate an empty list.
	private void initializeDataFields(){
	firstNode = null;
	numberOfEntries = 0;
	} // end initializeDataFields
	
	 
	// Returns a reference to the node at a given position.
	// Precondition: The chain is not empty;
	// 1 <= givenPosition <= numberOfEntries.
	private Node getNodeAt(int givenPosition)
	{
		assert !isEmpty() && (1 <= givenPosition) && (givenPosition <= numberOfEntries);
		Node currentNode = firstNode;
		
		// Traverse the chain to locate the desired node
		// (skipped if givenPosition is 1)
		for (int counter = 1; counter < givenPosition; counter++)
		currentNode = currentNode.getNextNode();
		
		assert currentNode != null;
	
		return currentNode;
	} // end getNodeAt
	
	 
	
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