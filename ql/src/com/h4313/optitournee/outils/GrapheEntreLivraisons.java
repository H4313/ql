//-- Date de generation: Wed Dec 4 16:20:13 UTC+0100 2013

package com.h4313.optitournee.outils;

import java.util.*;

/**
 * La classe <code>GrapheEntreLivraisons</code> impl�mente l'interface
 * <code>Graph</code>. On se r�f�rera � la Javadoc de cette derni�re
 * pour avoir des d�tails sur l'utilit� des m�thodes.
 *
 * @see Graph
 *
 * @author H4403
 */
public class GrapheEntreLivraisons implements Graph {
	private int nbVertices;
	private int maxArcCost;
	private int minArcCost;
	private int[][] cost; 
	private ArrayList<ArrayList<Integer>> succ; 
	
	public GrapheEntreLivraisons(int nbVertices, int[][] cost) {
		this.nbVertices = nbVertices;
		int tempMaxCost = -1;
		int tempMinCost = Integer.MAX_VALUE;
		succ = new ArrayList<ArrayList<Integer>>(); 
		for(int i=0 ; i<nbVertices ; i++) {
			for(int j=0 ; j<nbVertices ; j++) {
				if(cost[i][j] > tempMaxCost) {
					tempMaxCost = cost[i][j];
				}
				if(cost[i][j] < tempMinCost && cost[i][j] > 0) {
					tempMinCost = cost[i][j];
				}
			}
		}
		this.maxArcCost = tempMaxCost;
		this.minArcCost = tempMinCost;
		for(int i=0 ; i<nbVertices ; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			for(int j=0 ; j<nbVertices ; j++) {
				if(cost[i][j] < 0) {
					cost[i][j] = this.maxArcCost+1;
				}
				else {
					l.add(j);
				}
			}
			succ.add(i,l);
		}
		this.cost = cost;
	}
	
	public int getMaxArcCost() {
		return maxArcCost;
	}

	public int getMinArcCost() {
		return minArcCost;
	}

	public int getNbVertices() {
		return nbVertices;
	}

	public int[][] getCost(){
		return cost;
	}

	public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException{
		if ((i<0) || (i>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		int[] tab = new int[succ.get(i).size()];
		for(int j=0;j<tab.length;j++){
			tab[j] = succ.get(i).get(j);
		}
		return tab;
	}


	public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
		if ((i<0) || (i>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		return succ.get(i).size();
	}

}

