package com.grt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grt.entity.Pessoa;

@Controller
public class ControllerPessoa {

	boolean isUpdate = false;
	
	String nomeAntigo;

	List<Pessoa> lista = new ArrayList<Pessoa>();
	
	@GetMapping("/")
	public ModelAndView lista() {
		
		nomeAntigo = null;
		
		ModelAndView mv = new ModelAndView("cadastrar");
		
		Collections.sort(lista, Comparator.comparing(Pessoa::getIdade));
		Collections.reverse(lista);
		mv.addObject("pessoas", lista);
		
		mv.addObject("pessoa", new Pessoa());

		return mv;
		
	}
	
	@GetMapping("/salvar")
	public String salvar(@RequestParam String nome, @RequestParam String idade, RedirectAttributes attr) {
		
		if(idade.equals("") && nome.equals("")) {
			attr.addFlashAttribute("fail", "Preencha os campos!");
			return "redirect:/";
		}
		
		if(idade.equals("")) {
			attr.addFlashAttribute("fail", "Idade vazia!");
			return "redirect:/";
		}
		
		if(nome.equals("")) {
			attr.addFlashAttribute("fail", "Nome vazio!");
			return "redirect:/";
		}
		
		for (int i = 0; i < idade.length(); i++) {
	          if (Character.isLetter(idade.charAt(i))==true) {
	        	  attr.addFlashAttribute("fail", "Idade invalida!");
	        	  return "redirect:/";
	          }
		}
		
		Integer idadePessoa = Integer.parseInt(idade);
		
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome(nome);
		pessoa.setIdade(idadePessoa);
		
		if( isUpdate ) {
			Pessoa remove = null;
			for( Pessoa p : lista ) {
				if( p.getNome().equals( nomeAntigo )) {
					remove = p;
					isUpdate = false;
				}
			}
			lista.remove(remove);
		}
		
		boolean isIgual = false;
		
		if( !lista.isEmpty() ) {
			for( Pessoa p : lista ) {
				if( p.getNome().equals( pessoa.getNome() )) {
					isIgual = true;
					break;
				}
			}
			
			if( !isIgual ) {
				lista.add(pessoa);
			}
			else {
				attr.addFlashAttribute("fail", "Pessoa com mesmo nome ja cadastrado.");
			}
		}
		else {
			lista.add(pessoa);
		}
		
		return "redirect:/";
		
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam String nome) {
		
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome(nome);
		
		Pessoa remove = null;
		
		for( Pessoa p : lista ) {
			if( p.getNome().equals( pessoa.getNome() )) {
				remove = p;
			}
		}
		
		lista.remove(remove);
		
		return "redirect:/";
		
	}
	
	@GetMapping("/ordenar")
	public ModelAndView ordenar() {
		
		ModelAndView mv = new ModelAndView("cadastrar");
		
		int idade = 0;
		boolean desc = false;
		
		for(Pessoa p : lista) {
			if(p.getIdade() >= idade ) {
				idade = p.getIdade();
			}
			else {
				desc = true;
			}
		}
		
		Collections.sort(lista, Comparator.comparing(Pessoa::getIdade));
		
		if( !desc ) {
			Collections.reverse( lista );
		}
		
		mv.addObject("pessoas", lista);
		mv.addObject("pessoa", new Pessoa());

		return mv;
		
	}
	
	@GetMapping("/update")
	public ModelAndView update(@RequestParam String nome) {
		
		ModelAndView mv = new ModelAndView("cadastrar");
		
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome(nome);
		
		Pessoa update = null;
		
		for( Pessoa p : lista ) {
			if( p.getNome().equals( pessoa.getNome() )) {
				update = p;
			}
		}
		
		nomeAntigo = pessoa.getNome();
		isUpdate = true;
		
		pessoa.setIdade(update.getIdade());
		
		mv.addObject("pessoa", pessoa);
		mv.addObject("pessoas", lista);
		
		return mv;
	}
}
