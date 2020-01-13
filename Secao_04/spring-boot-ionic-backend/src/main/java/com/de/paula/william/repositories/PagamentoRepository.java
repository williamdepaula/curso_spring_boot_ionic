package com.de.paula.william.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.de.paula.william.domain.Pagamento;;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
