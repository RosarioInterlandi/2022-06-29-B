package it.polito.tdp.itunes.model;

import java.util.Objects;

public class VerticeBilanciato implements Comparable<VerticeBilanciato>{
	private Album vertice;
	private Double peso;
	
	public VerticeBilanciato(Album vertice, Double peso) {
		super();
		this.vertice = vertice;
		this.peso = peso;
	}

	public Album getVertice() {
		return vertice;
	}

	public void setVertice(Album vertice) {
		this.vertice = vertice;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		return Objects.hash(vertice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerticeBilanciato other = (VerticeBilanciato) obj;
		return Objects.equals(vertice, other.vertice);
	}

	@Override
	public int compareTo(VerticeBilanciato o) {
		
		return -this.getPeso().compareTo(o.getPeso());
	}
	
	
}
