package com.example.poo3.controller;

import com.example.poo3.model.Pessoa;
import com.example.poo3.model.PessoaRepository;
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
    model.addAttribute("pessoas", pessoaRepository.findAll());
    model.addAttribute("contador", pessoaRepository.count()); // Adiciona o contador ao modelo
    return "pessoas"; // Atualiza para retornar a página 'pessoas.html'
  }

  @GetMapping("/pessoas/add")
  public String showAddPage() {
    return "add"; // Retorna o nome do template HTML 'add.html'
  }

  @PostMapping("/pessoas")
  public String insertData(@RequestParam String nome, @RequestParam int cpf) {
    Pessoa pessoa = new Pessoa(nome, cpf);
    pessoaRepository.save(pessoa);
    return "redirect:/pessoas"; // Redireciona para a lista após adicionar
  }

  @GetMapping("/pessoas/update/{cpf}")
  public String showUpdatePage(@PathVariable int cpf, Model model) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      model.addAttribute("pessoa", pessoaOpt.get());
      return "update"; // Retorna o template 'update.html' para edição
    }
    return "redirect:/pessoas"; // Redireciona para a lista se a pessoa não for encontrada
  }

  @PostMapping("/pessoas/update/{cpf}")
  public String updateData(@PathVariable int cpf, @RequestParam String nome) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      Pessoa pessoa = pessoaOpt.get();
      pessoa.setNome(nome);
      pessoaRepository.save(pessoa);
    }
    return "redirect:/pessoas"; // Redireciona para a lista após atualizar
  }

  @GetMapping("/pessoas/delete/{cpf}")
  public String deleteData(@PathVariable int cpf) {
    pessoaRepository.deleteById(cpf);
    return "redirect:/pessoas"; // Redireciona para a lista após deletar
  }
}
