package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private DefaultDirectedWeightedGraph<Album, DefaultWeightedEdge> graph;
	private ItunesDAO dao;
	private List<Album> allNodes;
	private List<Album> bestPath;
	private int BestScore;
	private Album source;
	
	public Model() {
		this.dao = new ItunesDAO();
	}
	
	public void BuildGraph(Integer n) {
		this.graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//La durata data in input è in secondi, nel database servono i millisecondi
		int durata = n*1000;
		//Creazione vertici
		this.allNodes = new ArrayList<>();
		allNodes = this.dao.getVertici(durata);
		Graphs.addAllVertices(this.graph, allNodes);
		
		//Crea archi
		for (Album vertice: this.graph.vertexSet() ) {
			for (Album nodo: this.graph.vertexSet()) {
				if (nodo!= vertice && nodo.getDurata()!= vertice.getDurata() && ((nodo.getDurata()+vertice.getDurata())> 4*n)) {
					if (this.graph.getEdge(nodo, vertice)!=null ||
							this.graph.getEdge(vertice, nodo)!= null)
						continue;
					if (nodo.getDurata()>vertice.getDurata()) {
						Graphs.addEdge(this.graph, vertice, nodo, vertice.getDurata()+nodo.getDurata());
					}else {
						Graphs.addEdge(this.graph, nodo, vertice, vertice.getDurata()+nodo.getDurata());
					}
					
				}
			}
		}	
	}
	
	public List<Album>getVertici(){
		List<Album> result =new ArrayList<Album>(this.graph.vertexSet());
		Collections.sort(result);
		return result;
	}
	public List<DefaultWeightedEdge>getArchi(){
		return new ArrayList<DefaultWeightedEdge>(this.graph.edgeSet());
	}
	public VerticeBilanciato BilanciaVertice(Album a) {
		Double pesoVertice = 0.0;
		for (DefaultWeightedEdge edge :this.graph.incomingEdgesOf(a) ) {
			pesoVertice += this.graph.getEdgeWeight(edge);
		}
		for (DefaultWeightedEdge edge :this.graph.outgoingEdgesOf(a) ) {
			pesoVertice -= this.graph.getEdgeWeight(edge);
		}
		return new VerticeBilanciato(a, pesoVertice);  			
	}
	public List<VerticeBilanciato> stampaAdiacenze(Album root) {
		this.source = root;
		List<Album> successori = Graphs.successorListOf(this.graph, root);
		Collections.sort(successori);
		List<VerticeBilanciato> result = new ArrayList<>();
		for (int i=0; i<successori.size();i++ ) {
			VerticeBilanciato vb= this.BilanciaVertice(successori.get(i));
			result.add(vb);
			successori.get(i).setVb(vb);
		}
		Collections.sort(result);
		return result;
	}
	
	public List<Album> getPath( Album target,int pesoMinimo ) {
		List<Album> parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.BestScore = 0;
		parziale.add(this.source);
		this.ricorsione(parziale, target, pesoMinimo);
		
		return this.bestPath;
		
	}
	public void ricorsione(List<Album> parziale, Album target, int pesoMinimo) {
		Album current = parziale.get(parziale.size()-1);
		
		if (current.equals(target)) {
			if (this.getScore(parziale)> this.BestScore){
				this.BestScore = this.getScore(parziale);
				this.bestPath = new ArrayList<>(parziale);
			}
			return;			
		}
		
		//Continuo ad aggiungere elementi in parziale
		List<Album> successori = Graphs.successorListOf(this.graph, current);
		
		for (Album a : successori) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(current, a))>=pesoMinimo
					&& !parziale.contains(a)) {
				parziale.add(a);
				ricorsione(parziale, target, pesoMinimo);
				parziale.remove(a);
			}
		}	
	}
	
	private int getScore(List<Album> parziale) {
		int score =0;
		for (Album a: parziale.subList(1, parziale.size()-1)) {
			if (this.BilanciaVertice(a).getPeso()>  this.BilanciaVertice(this.source).getPeso()) {
				score +=1;
			}
			
		}
		return score;
	}
}
