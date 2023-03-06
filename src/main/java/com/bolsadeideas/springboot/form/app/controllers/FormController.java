package com.bolsadeideas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidador;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("usuario") // con esto mantiene el indetificador que le pasamos al usuario lo muestra
public class FormController {
	
	@Autowired
	private UsuarioValidador validador;
	
	// hace la validacion de manera transparente
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //siempre va con guiones el tipo de fecha
		dateFormat.setLenient(false);
		
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateFormat, true));
		
		// para covertir a mayusculas y sin espacios
		binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class, "apellido", new NombreMayusculaEditor());
	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return Arrays.asList(
				new Pais(1, "ES", "Espania"),
				new Pais(2, "MX", "Mexico"),
				new Pais(3, "CL", "Chile"),
				new Pais(4, "AR", "Argentina"),
				new Pais(5, "PE", "Peru"),
				new Pais(6, "CO", "Colombia"),
				new Pais(7, "VE", "Venezuela"));
	}
	
	@ModelAttribute("paises")
	public List<String> paises(){
		return Arrays.asList("Espania", "Mexico", "Chile", "Argentina", "Peru", "Colombia", "Venezuela");
	}
	
	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap(){
		Map<String, String> paises = new HashMap<String, String>();
		paises.put("ES", "Espania");
		paises.put("MX", "Mexico");
		paises.put("CL", "Chile");
		paises.put("AR", "Argentina");
		paises.put("PE", "Peru");
		paises.put("CO", "Colombia");
		paises.put("VE", "Venezuela");
		
		return paises;
	}
	
	//metodo handler
	
	@GetMapping("/form")
	public String form(Model model) {
		// agregar dato e informacion por defecto en el formulario
		Usuario usuario = new Usuario();
		usuario.setNombre("John");
		usuario.setApellido("Doe");
		
		usuario.setIdentificador("12.467.757-K");
		
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuario", usuario); // para que no pase el nullPointerException
		return "form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status) { //@ResquestParam para capturar los parametros que nos llegan en el formulario como name
		
		// validamos de forma explicita
		//validador.validate(usuario, result);
		
		//paramos el valor a la vista
				model.addAttribute("titulo", "Resultado form");
				
	    // validaciones si es valido o no
		if(result.hasErrors()) {
			//Map<String, String> errores = new HashMap<>();
			//result.getFieldErrors().forEach(err -> {
			//	errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			//});
			//model.addAttribute("error", errores);
			return "form";
		}
		
		model.addAttribute("usuario", usuario); // pasamos el objeto del modelo usuario con los datos de nuestra aplic
	
		status.setComplete(); // de manera automatico se elimina objeto usuario de la session
		
		return "resultado";
	}

}
