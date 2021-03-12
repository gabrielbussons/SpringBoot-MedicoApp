package com.medicoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.medicoapp.models.EspecialidadeModel;
import com.medicoapp.models.MedicoModel;
import com.medicoapp.repositorys.EspecialidadeRepository;
import com.medicoapp.repositorys.MedicoRepository;

@Controller
public class EspecialidadeController {
	
	@Autowired
	private EspecialidadeRepository espRepository;
	
	@Autowired
	private MedicoRepository medRepository;

	@RequestMapping(value="/cadastrarEspecialidade", method=RequestMethod.GET)
	public ModelAndView form() {
		
		ModelAndView modelView = new ModelAndView("especialidade/cadastroEspecialidade");
		
		return modelView;
	}
	
	@RequestMapping(value="/cadastrarEspecialidade", method=RequestMethod.POST)
	public String form(EspecialidadeModel especialidadeModel) throws Exception {
		
		try {
			validaFilto(especialidadeModel);
			especialidadeModel.setAtivo(true);
			espRepository.save(especialidadeModel);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/obterEspecialidades")
	public Iterable<EspecialidadeModel> obterEspecialidades(){
		
		return espRepository.findAll();
	}
	
	@RequestMapping(value="/especialidade.{id}")
	public ModelAndView especialidade(@PathVariable("id") long id) {
		
		EspecialidadeModel especialidade = espRepository.findById(id);
		
		ModelAndView modelView = new ModelAndView("especialidade/alterarEspecialidade");
		modelView.addObject("especialidade", especialidade);
		
		return modelView;
	}
	
	@RequestMapping(value="/medicosEspecialidade.{id}")
	public ModelAndView medicosEspecialidade(@PathVariable("id") long id) {
		
		EspecialidadeModel especialidade = espRepository.findById(id);
		Iterable<MedicoModel> medicos = medRepository.findByEspecialidades_Id(id);
		
		ModelAndView modelView = new ModelAndView("especialidade/medicosEspecialidade");
		modelView.addObject("especialidade", especialidade);
		modelView.addObject("medicos", medicos);
		
		return modelView;
	}
	
	@RequestMapping(value="/retirarMedico.{idMedico}-{idEspecialidade}", method=RequestMethod.GET)
	public RedirectView retirarMedico(@PathVariable("idMedico") long idMedico, @PathVariable("idEspecialidade") long idEspecialidade) {
		
		MedicoModel medico = medRepository.findById(idMedico);
		EspecialidadeModel especialidade = espRepository.findById(idEspecialidade);
		
		medico.getEspecialidades().remove(especialidade);
		medRepository.save(medico);
		
		RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/medicosEspecialidade.{idEspecialidade}");
		
		return rv;
	}
	
	@RequestMapping(value="/alterarSituacaoEspecialidade.{id}", method=RequestMethod.GET)
	public String alterarSituacaoEspecialidade(@PathVariable("id") long id) {
		
		EspecialidadeModel especialidade = espRepository.findById(id);
		especialidade.setAtivo(!especialidade.isAtivo());
		espRepository.save(especialidade);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/editarEspecialidade.{id}", method=RequestMethod.POST)
	public String editarEspecialidade(EspecialidadeModel especialidadeModel) throws Exception {
		
		try {
			validaFilto(especialidadeModel);
			espRepository.save(especialidadeModel);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/excluirEspecialidade.{id}")
	public String excluirEspecialidade(@PathVariable("id") long id) {
		
		EspecialidadeModel especialidadeModel = espRepository.findById(id);
		espRepository.delete(especialidadeModel);
		
		return "redirect:/";
	}
	
	
	private boolean validaFilto(EspecialidadeModel especialidade) throws Exception {
		if(especialidade.getNome() == null || especialidade.getNome() == "") {
			throw new Exception("Campo nome é obrigatório.");
		}
		
		if(especialidade.getDescricao() == null) {
			throw new Exception("Campo descricao é obrigatório.");
		}
		
		return true;
	}	
	
}