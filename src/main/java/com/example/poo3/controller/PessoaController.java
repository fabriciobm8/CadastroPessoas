package com.example.poo3.controller;

import com.example.poo3.model.Pessoa;
import com.example.poo3.model.PessoaRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

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

  @GetMapping("/pessoas/pdf")
  public void gerarRelatorioPdf(HttpServletResponse response) throws IOException {
    // Configurar a resposta para PDF
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"pessoas.pdf\"");
    // Criar o documento PDF
    PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
    Document document = new Document(pdfDocument);
    // Criar fonte Helvetica
    PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    // Obter a lista de pessoas do repositório
    List<Pessoa> pessoas = pessoaRepository.findAll();
    // Ordenar a lista pelo nome
    Collections.sort(pessoas, Comparator.comparing(Pessoa::getNome));
    // Adicionar título com fonte Helvetica
    document.add(new Paragraph("Lista de Pessoas")
        .setFont(helveticaFont) // Usando Helvetica
        .setFontSize(18));
    // Adicionar a quantidade de registros
    document.add(new Paragraph("Total de Registros: " + pessoas.size())
        .setFont(helveticaFont) // Usando Helvetica
        .setFontSize(14));
    // Criar uma tabela
    Table table = new Table(2); // 2 colunas: CPF e Nome
    table.addHeaderCell("CPF");
    table.addHeaderCell("Nome");
    // Adicionar dados à tabela
    for (Pessoa pessoa : pessoas) {
      table.addCell(pessoa.getCpf());
      table.addCell(pessoa.getNome());
    }
    // Adicionar a tabela ao documento
    document.add(table);
    // Fechar o documento
    document.close();
  }

  @GetMapping("/pessoas/excel")
  public void gerarRelatorioExcel(HttpServletResponse response) throws IOException {
    // Configurar a resposta para Excel
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=\"pessoas.xlsx\"");
    // Criar um novo workbook e uma planilha
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Pessoas");
    // Criar cabeçalho
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("CPF");
    headerRow.createCell(1).setCellValue("Nome");
    // Obter a lista de pessoas do repositório
    List<Pessoa> pessoas = pessoaRepository.findAll();
    // Ordenar a lista pelo nome
    Collections.sort(pessoas, Comparator.comparing(Pessoa::getNome));
    // Adicionar dados à planilha
    int rowNum = 1; // Começa na segunda linha
    for (Pessoa pessoa : pessoas) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(pessoa.getCpf());
      row.createCell(1).setCellValue(pessoa.getNome());
    }
    // Ajustar a largura das colunas
    for (int i = 0; i < 2; i++) { // 2 colunas: CPF e Nome
      sheet.autoSizeColumn(i);
    }
    // Escrever o arquivo Excel na resposta
    workbook.write(response.getOutputStream());
    workbook.close();
  }

}
