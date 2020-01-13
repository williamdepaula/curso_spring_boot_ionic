package com.de.paula.william.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.de.paula.william.domain.Cliente;
import com.de.paula.william.domain.enums.TipoCliente;
import com.de.paula.william.dto.ClienteNewDTO;
import com.de.paula.william.repositories.ClienteRepository;
import com.de.paula.william.resources.exception.FieldMessage;
import com.de.paula.william.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}
	
	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		
		if (objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente emailExistente = repo.findByEmail(objDTO.getEmail());
		if (emailExistente != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		addErrorInValidator(list, context);
		
		return list.isEmpty();
	}
	
	private void addErrorInValidator(List<FieldMessage> list, ConstraintValidatorContext context) {
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
			.addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
	}

}
