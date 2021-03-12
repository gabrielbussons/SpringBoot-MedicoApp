package com.medicoapp.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.medicoapp.models.EspecialidadeModel;
import com.medicoapp.models.MedicoModel;

public interface EspecialidadeRepository extends CrudRepository<EspecialidadeModel, String>{

	EspecialidadeModel findById(long id);
	
	Iterable<EspecialidadeModel> findAllByOrderByIdAsc();

	Iterable<EspecialidadeModel> findByAtivo(boolean ativo);
	
	Iterable<EspecialidadeModel> findByMedicos_Id(long medicoId);
	
	Iterable<EspecialidadeModel> findByIdNotIn(List<Long> id);
	
}
