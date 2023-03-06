package com.bolsadeideas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.editors.PaisPropertyEditor;
import com.bolsadeideas.springboot.form.app.editors.RolesEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Role;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.services.PaisService;
import com.bolsadeideas.springboot.form.app.services.RoleService;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidador;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("usuario") // con esto mantiene el indetificador que le pasamos al usuario lo muestra
public class FormController {
	
	@Autowired
	private UsuarioValidador validador;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PaisPropertyEditor paisEditor; 
	
	@Autowired
	private RolesEditor roleEditor;
	
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
		
		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
		binder.registerCustomEditor(Role.class, "roles", roleEditor);
	}
	
	@ModelAttribute("genero")
	public List<String> genero() {
		return Arrays.asList("Hombre", "Mujer");
	}
	
	@ModelAttribute("listaRoles")
	public List<Role> listaRoles() {
		return this.roleService.listar();
	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return paisService.listar();
	}
	
	@ModelAttribute("listaRolesString")
	public List<String> listaRolesString(){
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_MODERATOR");
		return roles;
	}
	
	@ModelAttribute("listaRolesMap")
	public Map<String, String> listaRolesMap(){
		Map<String, String> roles = new HashMap<String, String>();
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "Usuario");
		roles.put("ROLE_MODERATOR", "Moderador");
		
		return roles;
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
		
		usuario.setHabilitar(true);
		
		usuario.setValorSecreto("Algun valor secreto ****");
		
		// para mantener un valor por defecto en el formulario pais
		usuario.setPais(new Pais(3, "CL", "Chile"));
		
		// para mantener un calor por defecto en el formularios roles
		usuario.setRoles(Arrays.asList(new Role(2, "Usuario", "ROLE_USER")));
		
		
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuario", usuario); // para que no pase el nullPointerException
		return "form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model) { //@ResquestParam para capturar los parametros que nos llegan en el formulario como name
		
		// validamos de forma explicita
		//validador.validate(usuario, result);
				
	    // validaciones si es valido o no
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Resultado form");
			//Map<String, String> errores = new HashMap<>();
			//result.getFieldErrors().forEach(err -> {
			//	errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			//});
			//model.addAttribute("error", errores);
			return "form";
		}
	
		return "redirect:/ver";
	}
	
	
	// para que no se vuelva a mandar el formulario cuando hacen un refresh en la pagina
	@GetMapping("/ver")
	public String ver(@SessionAttribute(name ="usuario", required= false) Usuario usuario, Model model, SessionStatus status) {
		if(usuario == null) {
			return "redirect:/form";
		}
		model.addAttribute("titulo", "Resultado form");
		
		status.setComplete(); // de manera automatico se elimina objeto usuario de la session
		return "resultado";
	}

}
