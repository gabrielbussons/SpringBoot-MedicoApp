package com.medicoapp.controllers;

import java.util.ArrayList;
import java.util.List;

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
public class MedicoController {

	@Autowired
	private MedicoRepository medRepository;
	
	@Autowired
	private EspecialidadeRepository espRepository;

	@RequestMapping(value="/cadastrarMedico", method=RequestMethod.GET)
	public ModelAndView form() {
		
		ModelAndView modelView = new ModelAndView("medico/cadastroMedico");
		
		return modelView;
	}
	
	@RequestMapping(value="/cadastrarMedico", method=RequestMethod.POST)
	public String form(MedicoModel medicoModel) throws Exception {
		
		try {
			validaFilto(medicoModel);
			medicoModel.setAtivo(true);
			medRepository.save(medicoModel);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/obterMedicos")
	public Iterable<MedicoModel> obterMedicos(){
				
		return  medRepository.findAll();
	}
	
	@RequestMapping(value="/medico.{id}", method=RequestMethod.GET)
	public ModelAndView medico(@PathVariable("id") long id) {
		
		MedicoModel medico = medRepository.findById(id);
		Iterable<EspecialidadeModel> medicoEspecialidades = espRepository.findByMedicos_Id(id);
		Iterable<EspecialidadeModel> medicoNaoEspecialidades = new ArrayList<>();
		
		if(medicoEspecialidades.iterator().hasNext()) {
		
			List<Long> listaIds = new ArrayList<Long>();
			medicoEspecialidades.forEach(f -> listaIds.add(f.getId()));
			medicoNaoEspecialidades = espRepository.findByIdNotIn(listaIds);
		
		} else {
			medicoNaoEspecialidades = espRepository.findAll();
		}
		
		
		ModelAndView modelView = new ModelAndView("medico/alterarMedico");
		
		modelView.addObject("medico", medico);
		modelView.addObject("medicoEspecialidades", medicoEspecialidades);
		modelView.addObject("medicoNaoEspecialidades", medicoNaoEspecialidades);
		
		return modelView;
	}
	
	@RequestMapping(value="/associarEspecialidade.{idMedico}-{idEspecialidade}", method=RequestMethod.GET)
	public RedirectView associarEspecialidade(@PathVariable("idMedico") long idMedico, @PathVariable("idEspecialidade") long idEspecialidade) {
		
		MedicoModel medico = medRepository.findById(idMedico);
		EspecialidadeModel especialidade = espRepository.findById(idEspecialidade);
		
		medico.getEspecialidades().add(especialidade);
		medRepository.save(medico);
		
		RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/medico.{idMedico}");
		
		return rv;
	}
	
	@RequestMapping(value="/alterarSituacaoMedico.{id}", method=RequestMethod.GET)
	public String alterarSituacaoMedico(@PathVariable("id") long id) {
		
		MedicoModel medico = medRepository.findById(id);
		medico.setAtivo(!medico.isAtivo());
		medRepository.save(medico);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/editarMedico.{id}", method=RequestMethod.POST)
	public String editarMedico(MedicoModel medicoModel) throws Exception {
		
		try {
			validaFilto(medicoModel);
			medRepository.save(medicoModel);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/excluirMedico.{id}")
	public String excluirMedico(@PathVariable("id") long id) {
		
		MedicoModel medicoModel = medRepository.findById(id);
		medRepository.delete(medicoModel);
		
		return "redirect:/";
	}
	
	private boolean validaFilto(MedicoModel medico) throws Exception {
		if(medico.getNome() == null || medico.getNome() == "") {
			throw new Exception("Campo nome é obrigatório.");
		}
		
		if(medico.getDataNascimento() == null) {
			throw new Exception("Campo dataNascimento é obrigatório.");
		}
		
		return true;
	}
}
