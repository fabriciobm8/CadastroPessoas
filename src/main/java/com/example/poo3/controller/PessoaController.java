package com.example.poo3.controller;

import com.example.poo3.model.Pessoa;
import com.example.poo3.model.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller // Atualizado para @Controller para suportar páginas HTML
@RequestMapping("/pessoas")
public class PessoaController {

  @Autowired
  private PessoaRepository pessoaRepository;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping
  public String showData(Model model) {
    model.addAttribute("pessoas", pessoaRepository.findAll());
    return "pessoas"; // Atualiza para retornar a página 'pessoas.html'
  }

  @GetMapping("/add")
  public String showAddPage() {
    return "add"; // Retorna o nome do template HTML 'add.html'
  }

  @PostMapping
  public String insertData(@RequestParam String nome, @RequestParam int cpf) {
    Pessoa pessoa = new Pessoa(nome, cpf);
    pessoaRepository.save(pessoa);
    return "redirect:/pessoas"; // Redireciona para a lista após adicionar
  }

  @GetMapping("/update/{cpf}")
  public String showUpdatePage(@PathVariable int cpf, Model model) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      model.addAttribute("pessoa", pessoaOpt.get());
      return "update"; // Retorna o template 'update.html' para edição
    }
    return "redirect:/pessoas"; // Redireciona para a lista se a pessoa não for encontrada
  }

  @PostMapping("/update/{cpf}")
  public String updateData(@PathVariable int cpf, @RequestParam String nome) {
    Optional<Pessoa> pessoaOpt = pessoaRepository.findById(cpf);
    if (pessoaOpt.isPresent()) {
      Pessoa pessoa = pessoaOpt.get();
      pessoa.setNome(nome);
      pessoaRepository.save(pessoa);
    }
    return "redirect:/pessoas"; // Redireciona para a lista após atualizar
  }

  @GetMapping("/delete/{cpf}")
  public String deleteData(@PathVariable int cpf) {
    pessoaRepository.deleteById(cpf);
    return "redirect:/pessoas"; // Redireciona para a lista após deletar
  }
}
