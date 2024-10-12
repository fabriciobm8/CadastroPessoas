package com.example.poo3.controller;

import com.example.poo3.model.Pessoa;
import com.example.poo3.model.PessoaRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PessoaController {

  @Autowired
  private PessoaRepository pessoaRepository;

  // Rota para a página inicial acessada por /index
  @GetMapping("/index")
  public String index() {
    return "index"; // Retorna o template 'index.html'
  }

  // Rota para a página de pessoas
  @GetMapping("/pessoas")
  public String showData(Model model) {
    List<Pessoa> pessoas = pessoaRepository.findAll();

    // Ordenar a lista de pessoas pelo nome
    Collections.sort(pessoas, Comparator.comparing(Pessoa::getNome));

    // Usar a lista ordenada ao adicionar ao modelo
    model.addAttribute("pessoas", pessoas); // Usa a lista ordenada
    model.addAttribute("contador", pessoas.size()); // Adiciona o contador com a lista ordenada

    return "pessoas"; // Atualiza para retornar a página 'pessoas.html'
  }


  @GetMapping("/pessoas/add")
  public String showAddPage() {
    return "add"; // Retorna o nome do template HTML 'add.html'
  }

  @PostMapping("/pessoas")
  public String insertData(@RequestParam String nome, @RequestParam String cpf) {
    Pessoa pessoa = new Pessoa(nome, cpf);
    pessoaRepository.save(pessoa);
    return "redirect:/pessoas"; // Redireciona para a lista após adicionar
  }

  @GetMapping("/pessoas/update/{cpf}")
  public String showUpdatePage(@PathVariable String cpf, Model model) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      model.addAttribute("pessoa", pessoaOpt.get());
      return "update"; // Retorna o template 'update.html' para edição
    }
    return "redirect:/pessoas"; // Redireciona para a lista se a pessoa não for encontrada
  }

  @PostMapping("/pessoas/update/{cpf}")
  public String updateData(@PathVariable String cpf, @RequestParam String nome) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      Pessoa pessoa = pessoaOpt.get();
      pessoa.setNome(nome);
      pessoaRepository.save(pessoa);
    }
    return "redirect:/pessoas"; // Redireciona para a lista após atualizar
  }

  @GetMapping("/pessoas/delete/{cpf}")
  public String deleteData(@PathVariable String cpf) {
    pessoaRepository.deleteById(cpf);
    return "redirect:/pessoas"; // Redireciona para a lista após deletar
  }
}
