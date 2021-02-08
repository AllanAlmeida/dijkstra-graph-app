package br.radixeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.radixeng.entities.Route;

/**
*
*/
@Repository
public interface RouteRepository extends JpaRepository<Route, Long>{}
