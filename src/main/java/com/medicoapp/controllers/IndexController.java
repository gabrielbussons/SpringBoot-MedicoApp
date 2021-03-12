package com.medicoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.medicoapp.filtros.FiltroSituacao;
import com.medicoapp.models.EspecialidadeModel;
import com.medicoapp.models.MedicoModel;
import com.medicoapp.repositorys.EspecialidadeRepository;
import com.medicoapp.repositorys.MedicoRepository;

@Controller
public class IndexController {
	
	@Autowired
	private EspecialidadeRepository espRepository;
	
	@Autowired
	private MedicoRepository medRepository;

	@RequestMapping("/")
	public ModelAndView index() {
		
		ModelAndView modelView = new ModelAndView("index");
		
		Iterable<EspecialidadeModel> especialidades = espRepository.findAllByOrderByIdAsc();
		Iterable<MedicoModel> medicos = medRepository.findAllByOrderByIdAsc();
		
		modelView.addObject("especialidades", especialidades);
		modelView.addObject("medicos", medicos);
		
		return modelView;
	}
	
	@RequestMapping(value="/filtrarSituacao", method=RequestMethod.POST)
	public ModelAndView filtrarSituacao(FiltroSituacao situacao){
		
		String especialidadeSitaucao = situacao.getEspecialidadeSituacao();
		String medicoSituacao = situacao.getMedicoSituacao();
		
		Iterable<EspecialidadeModel> especialidades;
		Iterable<MedicoModel> medicos;
		
		switch(especialidadeSitaucao) {		
			case "S":
				especialidades = espRepository.findByAtivo(true);
				break;
				
			case "N":
				especialidades = espRepository.findByAtivo(false);
				break;
				
			default:
				especialidades = espRepository.findAll();
				break;
		}
		
		switch(medicoSituacao) {		
			case "S":
				medicos = medRepository.findByAtivo(true);
				break;
				
			case "N":
				medicos = medRepository.findByAtivo(false);
				break;
				
			default:
				medicos = medRepository.findAll();
				break;
		}
		
		ModelAndView modelView = new ModelAndView("index");
		modelView.addObject("especialidadeSitaucao", especialidadeSitaucao);
		modelView.addObject("medicoSituacao", medicoSituacao);
		modelView.addObject("especialidades", especialidades);
		modelView.addObject("medicos", medicos);
		
		
		return modelView;
	}
}
