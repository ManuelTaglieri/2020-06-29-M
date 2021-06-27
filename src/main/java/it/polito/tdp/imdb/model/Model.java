package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	private int attoriMassimi;
	private List<Director> percorsoMigliore;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public void creaGrafo(int anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		this.dao.getRegistiPerAnno(anno, idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		List<Adiacenza> archi = this.dao.getArchi(anno, idMap);
		for (Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, a.getD1(), a.getD2(), a.getPeso());
		}
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getRegisti() {
		List<Director> risultato = new LinkedList<>();
		for (Director d : this.grafo.vertexSet()) {
			risultato.add(d);
		}
		Collections.sort(risultato, new Comparator<Director>() {

			@Override
			public int compare(Director o1, Director o2) {
				return o1.getId() - o2.getId();
			}
			
		});
		return risultato;
	}
	
	public List<Director> getAdiacenti(Director d) {
		
		List<Director> risultato = Graphs.neighborListOf(this.grafo, d);
		
		for (Director dA : risultato) {
			dA.setPesoAdiacente(this.grafo.getEdgeWeight(this.grafo.getEdge(d, dA)));
		}
		
		Collections.sort(risultato, new Comparator<Director>(){

			@Override
			public int compare(Director o1, Director o2) {
				return (int) -(o1.getPesoAdiacente() - o2.getPesoAdiacente());
			}
		
		});
		
		return risultato;
		
	}
	
	public void camminoMassimo(Director d, int c) {
		
		this.attoriMassimi = 0;
		this.percorsoMigliore = new LinkedList<>();
		ArrayList<Director> percorso = new ArrayList<Director>();
		percorso.add(d);
		ricorsione(d, c, 0, percorso);
		
	}

	private void ricorsione(Director d, int c, int pesoTot, ArrayList<Director> percorso) {
		for (Director d2 : Graphs.neighborListOf(this.grafo, d)) {
			if ((this.grafo.getEdgeWeight(this.grafo.getEdge(d, d2)) + pesoTot)<=c && !percorso.contains(d2)) {
				pesoTot = (int) (pesoTot+this.grafo.getEdgeWeight(this.grafo.getEdge(d, d2)));
				if (pesoTot>attoriMassimi) {
					this.attoriMassimi = pesoTot;
					this.percorsoMigliore = new ArrayList<>(percorso);
				}
				percorso.add(d2);
				ricorsione(d2, c, pesoTot, percorso);
				percorso.remove(d2);
			}
		}
		
	}

	public int getAttoriMassimi() {
		return attoriMassimi;
	}

	public List<Director> getPercorsoMigliore() {
		return percorsoMigliore;
	}

}
