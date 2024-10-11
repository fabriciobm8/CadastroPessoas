package com.example.poo3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PessoasPOO3") // Mapeia para a tabela 'PessoasPOO3'
public class Pessoa { // Renomeado de Model para Pessoa
  @Id
  private int cpf;
  private String nome;

  // Construtor padrão
  public Pessoa() {
  }

  public Pessoa(String nome, int cpf) {
    this.nome = nome;
    this.cpf = cpf;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getCpf() {
    return cpf;
  }

  public void setCpf(int cpf) {
    this.cpf = cpf;
  }
}
