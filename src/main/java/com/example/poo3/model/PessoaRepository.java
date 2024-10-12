package com.example.poo3.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, String> { // Renomeado de ModelRepository para PessoaRepository
}
