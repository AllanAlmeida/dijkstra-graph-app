package br.radixeng.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "graph")
public class Graph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8023088477220190632L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
