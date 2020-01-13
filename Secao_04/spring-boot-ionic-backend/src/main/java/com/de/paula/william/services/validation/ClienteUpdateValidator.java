package com.de.paula.william.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.de.paula.william.domain.Cliente;
import com.de.paula.william.dto.ClienteDTO;
import com.de.paula.william.repositories.ClienteRepository;
import com.de.paula.william.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		
	}
	
	@Override
	public boolean isValid(ClienteDTO objDTO, ConstraintValidatorContext context) {
		
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
				
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente emailExistente = repo.findByEmail(objDTO.getEmail());
		if (emailExistente != null && !emailExistente.getId().equals(uriId)) {
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
