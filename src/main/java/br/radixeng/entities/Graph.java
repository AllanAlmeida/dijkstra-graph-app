package br.radixeng.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "graph")
public class Graph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8023088477220190632L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "graph_id")
	private long id;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Route> data = new ArrayList<>();

	public Graph(){
		//default construtor
	}
	
	public Graph(List<Route> listaRotas) {
		this.data = listaRotas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Route> getData() {
		return data;
	}

	public void setData(List<Route> data) {
		this.data = data;
	}
}
