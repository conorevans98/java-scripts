
/**
 * @author Conor Evans
 * @version 1.0 
 * @date 08/04/2018
 */
import java.util.InputMismatchException;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestants’
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *     Each contestant walks at a given estimated speed.
 *     The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

public class CompetitionDijkstra {
	private EdgeWeightedDigraph G;
	private double slowestSpeed;
	private double longestDistance;
	private double weight;
	private boolean validFile;
	private int startVertex;
	private int destinationVertex;
	private int V;

	/**
	 * @param filename:
	 *            A filename containing the details of the city road network
	 * @param sA,
	 *            sB, sC: speeds for 3 contestants
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) {
		// store speed of slowest contestant
		// divide by 1000 to turn speeds (km) to be in same unit
		// as weights (m)
		if (sA <= 0 || sB <= 0 || sC <= 0)
			slowestSpeed = -1.0;
		else
			slowestSpeed = (double) min(sA, sB, sC) / 1000;

		try {
			In in = new In(filename);
			// if reaches this point, vF = true
			validFile = true;
			if (in.hasNextLine()) {
				V = in.readInt();
				G = new EdgeWeightedDigraph(V);

			} else {
				validFile = false;
				return;
			}
			if (in.hasNextLine()) {
				@SuppressWarnings("unused") // having issues skipping line,
											// readLine() is maintaining the
											// line in
											// the input stream
				int edges = in.readInt();
			} else {
				validFile = false;
				return;
			}
			// if no vertex/weight details available, file invalid
			if (!in.hasNextLine()) {
				validFile = false;
				return;
			}
			while (in.hasNextLine()) {
				// get values for each line in file
				if (in.hasNextInt())
					startVertex = in.readInt();
				else {
					validFile = false;
					return;
				}
				if (in.hasNextInt())
					destinationVertex = in.readInt();
				else {
					validFile = false;
					return;
				}
				if (in.hasNextDouble())
					weight = in.readDouble();
				else {
					validFile = false;
					return;
				}
				// create edge
				DirectedEdge edge = new DirectedEdge(startVertex, destinationVertex, weight);
				// add each edge to EWD
				G.addEdge(edge);
			}
		} catch (NullPointerException Exception) {
			validFile = false;
		} catch (InputMismatchException Exception) {
			validFile = false;
		}

	}

	/**
	 * @return int: minimum minutes that will pass before the three contestants
	 *         can meet
	 */
	public int timeRequiredforCompetition() {
		// if an invalid speed was entered, or the file was invalid, return -1.
		if (slowestSpeed == -1.0 || validFile == false)
			return -1;
		for (int i = 0; i < V; i++) {
			DijkstraSP sp = new DijkstraSP(G, i);
			for (int j = 0; j < V; j++)
				// if path from current start vertex (i) to another vertex (j)
				// is longer than current
				// stored longestDistance, update the longestDistance value.
				if (sp.hasPathTo(j) && sp.distTo(j) > longestDistance)
					longestDistance = sp.distTo(j);
				else if (!sp.hasPathTo(j))
					return -1;
		}
		return (int) Math.ceil(longestDistance / slowestSpeed);
	}

	/**
	 * @return boolean: best to keep the variable private, so just have a getter
	 *         function for it.
	 */
	public boolean validFile() {
		return this.validFile;
	}

	/**
	 * @return int: minimum speed of 3 contestants.
	 */
	public static int min(int a, int b, int c) {
		if (a <= b && a <= c)
			return a;
		else if (b <= a && b <= c)
			return b;
		else
			return c;
	}

}