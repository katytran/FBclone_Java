package source.Graph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import source.Facebook.User;
import views.FriendOfFriend;
import views.Login;
import views.friendDisplay;

public class DirectedGraph<T> implements GraphInterface<T> {
	private DictionaryInterface<T, VertexInterface<T>> vertices;
	private int edgeCount;

	public DirectedGraph() {
		vertices = new LinkedDictionary<>();
		edgeCount = 0;
	} // end default constructor

	protected void resetVertices() {
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext()) {
			VertexInterface<T> nextVertex = vertexIterator.next();
			nextVertex.unvisit();
			nextVertex.setCost(0);
			nextVertex.setPredecessor(null);
		} // end while
	} // end resetVertices

	@Override
	public boolean addVertex(T vertexLabel) {
		VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
		return addOutcome == null; // Check addition to dictionary successfull
	}

	public void removeVertex(T vertexLabel) {
		vertices.remove(vertexLabel);
	}
	@Override
	public boolean addEdge(T begin, T end, double edgeWeight) {
		boolean result = false;

		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if ((beginVertex != null) && (endVertex != null))
			result = beginVertex.connect(endVertex, edgeWeight);
		if (result)
			edgeCount++;
		return result;
	}

	public boolean removeEdge(T begin, T end, double edgeWeight) {
		boolean result = false;

		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if ((beginVertex != null) && (endVertex != null) && hasEdge(begin, end))
			result = beginVertex.disconnect(endVertex, edgeWeight);
		if (result)
			edgeCount--;
		return result;
		
	}
	
	public boolean removeEdge(T begin, T end) {
		return removeEdge(begin, end, 0);
	}

	@Override
	public boolean addEdge(T begin, T end) {
		return addEdge(begin, end, 0);
	}

	@Override
	public boolean hasEdge(T begin, T end) {
		boolean found = false;

		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		if ((beginVertex != null) && (endVertex != null)) {
			Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();

			while (!found && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();
				if (endVertex.equals(nextNeighbor))
					found = true;
			}
		}

		return found;
	}

	@Override
	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	@Override
	public int getNumberOfVertices() {
		return vertices.getSize();
	}

	@Override
	public int getNumberOfEdges() {
		return edgeCount;
	}

	@Override
	public void clear() {
		vertices.clear();
		edgeCount = 0;
	}

	public VertexInterface<T> getVertex(final T label) {
		VertexInterface<T> result = null;
		if (this.vertices.contains(label)) {
			result = this.vertices.getValue(label);
		}
		return result;
	}

	@Override
	public QueueInterface<T> getBreadthFirstTraversal(T origin) {
		resetVertices();

		QueueInterface<T> traversalOrder = new LinkedQueue<>();
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();

		VertexInterface<T> originVertex = vertices.getValue(origin);
		originVertex.visit();
		traversalOrder.enqueue(origin);
		vertexQueue.enqueue(originVertex);

		while (!vertexQueue.isEmpty()) {
			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

			while (neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();
				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					traversalOrder.enqueue(nextNeighbor.getLabel());
					vertexQueue.enqueue(nextNeighbor);
				}
			}
		}
		return traversalOrder;
	}

	public QueueInterface<T> getDepthFirstTraversal(T origin) {
		// Assumes graph is not empty
		resetVertices();
		QueueInterface<T> traversalOrder = new LinkedQueue<T>();
		StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<>();

		VertexInterface<T> originVertex = vertices.getValue(origin);
		originVertex.visit();
		traversalOrder.enqueue(origin); // Enqueue vertex label
		vertexStack.push(originVertex); // Enqueue vertex

		while (!vertexStack.isEmpty()) {
			VertexInterface<T> topVertex = vertexStack.peek();
			VertexInterface<T> nextNeighbor = topVertex.getUnvisitedNeighbor();

			if (nextNeighbor != null) {
				nextNeighbor.visit();
				traversalOrder.enqueue(nextNeighbor.getLabel());
				vertexStack.push(nextNeighbor);
			} else // All neighbors are visited
				vertexStack.pop();
		} // end while

		return traversalOrder;
	} // end getDepthFirstTraversal

	public StackInterface<T> getTopologicalOrder() {
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++) {
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for

		return vertexStack;
	} // end getTopologicalOrder

	/** Precondition: path is an empty stack (NOT null) */
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean done = false;
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();

		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		originVertex.visit();
		// Assertion: resetVertices() has executed setCost(0)
		// and setPredecessor(null) for originVertex

		vertexQueue.enqueue(originVertex);

		while (!done && !vertexQueue.isEmpty()) {
			VertexInterface<T> frontVertex = vertexQueue.dequeue();

			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
			while (!done && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();

				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					nextNeighbor.setCost(1 + frontVertex.getCost());
					nextNeighbor.setPredecessor(frontVertex);
					vertexQueue.enqueue(nextNeighbor);
				} // end if

				if (nextNeighbor.equals(endVertex))
					done = true;
			} // end while
		} // end while

		// Traversal ends; construct shortest path
		int pathLength = (int) endVertex.getCost();
		path.push(endVertex.getLabel());

		VertexInterface<T> vertex = endVertex;
		while (vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		} // end while

		return pathLength;
	} // end getShortestPath

	public void Traverse(T origin) {
		StackInterface<T> path = new LinkedStack<>();
		Iterator<VertexInterface<T>> verticeIterator = vertices.getValueIterator();

		while (verticeIterator.hasNext()) {
			T end = verticeIterator.next().getLabel();
			int shortestPath = getShortestPath(origin, end, path);
			if (shortestPath == 2) {
				if (!(origin.equals(end))) {
					System.out.println(end);
					String name2 = end.toString();
					System.out.println("name2" + name2);
					try {
						Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
								"root", "Conheoanh123");

						PreparedStatement st = (PreparedStatement) connection
								.prepareStatement("select * from profile where username = ?");

						st.setString(1, name2);

						ResultSet rs = st.executeQuery();
						if (rs.next()) {
							friendDisplay.friendModel.addElement(new User(rs.getString("username"), rs.getString("password"), rs.getString("name"), 
									rs.getString("status"),rs.getString("gender"), rs.getBlob("profilePic")));
						}
						st.close();

					} catch (SQLException sqlException) {
						sqlException.printStackTrace();	
					}
				}
			}
		}

	}

	/** Precondition: path is an empty stack (NOT null) */
	public double getCheapestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean done = false;

		// Use EntryPQ instead of Vertex because multiple entries contain
		// the same vertex but different costs - cost of path to vertex is EntryPQ's
		// priority value
		PriorityQueueInterface<EntryPQ> priorityQueue = new HeapPriorityQueue<>();

		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		priorityQueue.add(new EntryPQ(originVertex, 0, null));

		while (!done && !priorityQueue.isEmpty()) {
			EntryPQ frontEntry = priorityQueue.remove();
			VertexInterface<T> frontVertex = frontEntry.getVertex();
			if (!frontVertex.isVisited()) {
				frontVertex.visit();
				frontVertex.setCost(frontEntry.getCost());
				frontVertex.setPredecessor(frontEntry.getPredecessor());

				if (frontVertex.equals(endVertex))
					done = true;
				else {
					Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
					Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
					while (neighbors.hasNext()) {
						VertexInterface<T> nextNeighbor = neighbors.next();
						Double weightOfEdgeToNeighbor = edgeWeights.next();

						if (!nextNeighbor.isVisited()) {
							double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
							priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
						} // end if
					} // end while
				} // end if
			} // end if
		} // end while

		// Traversal ends, construct cheapest path
		double pathCost = endVertex.getCost();
		path.push(endVertex.getLabel());

		VertexInterface<T> vertex = endVertex;
		while (vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		} // end while

		return pathCost;
	} // end getCheapestPath

	protected VertexInterface<T> findTerminal() {
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext()) {
			VertexInterface<T> nextVertex = vertexIterator.next();

			// If nextVertex is unvisited AND has only visited neighbors)
			if (!nextVertex.isVisited()) {
				if (nextVertex.getUnvisitedNeighbor() == null) {
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges() {
		//System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		//System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext()) {
			((Vertex<T>) (vertexIterator.next())).display();
		} // end while
	} // end displayEdges

	public void displayNeighbor(T label) {
		Iterator<VertexInterface<T>> i = getVertex(label).getNeighborIterator();
		
		while (i.hasNext()) {
			Vertex<T> v = (Vertex<T>) i.next();
			System.out.print(v +  " "); //without display weight
			// System.out.print(v + " " + w.next() + " ");

		} // end while
		
	}
	private class EntryPQ implements Comparable<EntryPQ> {
		private VertexInterface<T> vertex;
		private VertexInterface<T> previousVertex;
		private double cost; // cost to nextVertex

		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex) {
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor

		public VertexInterface<T> getVertex() {
			return vertex;
		} // end getVertex

		public VertexInterface<T> getPredecessor() {
			return previousVertex;
		} // end getPredecessor

		public double getCost() {
			return cost;
		} // end getCost

		public int compareTo(EntryPQ otherEntry) {
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int) Math.signum(otherEntry.cost - cost);
		} // end compareTo

		public String toString() {
			return vertex.toString() + " " + cost;
		} // end toString
	} // end EntryPQ

} // end DirectedGraph
