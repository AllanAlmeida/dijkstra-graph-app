package br.radixeng.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Route implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4095658960187068323L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String source;
	

	private String target;
	
	private Integer distance;
	
	public Route(){
		//default construtor
	}
	
	public Route(String source, String target, Integer distance) {
		this.source = source;
		this.target = target;
		this.distance = distance;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public Integer getDistance() {
		return distance;
	}
	
	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
}
