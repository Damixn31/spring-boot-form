package com.bolsadeideas.springboot.form.app.models.domain;

import java.util.Date;

//import org.springframework.format.annotation.DateTimeFormat;

import com.bolsadeideas.springboot.form.app.validation.IdentificadorRegex;
import com.bolsadeideas.springboot.form.app.validation.Requerido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// esto es un POJO para pasar datos 

public class Usuario {
	// mismo nombre de campos iguales a el name del form
	
	//como no esta en el formulario no se valida
	//@Pattern(regexp = "[0-9]{2}[.][\\d]{3}[.][\\d]{3}[-][A-Z]{1}")
	@IdentificadorRegex
	private String identificador;
	
	//@NotEmpty(message = "El nombre no puede ser vacio!")
	private String nombre;
	
	//@NotEmpty 
	@Requerido
	private String apellido;
	
	
	@NotBlank   // si en el input le pasan espacios vacios que no valide 
	@Size(min = 3, max= 8)   
	private String username;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	@Email(message = "Correo con formato incorrecto!")
	private String email;
	
	@NotNull // para objetos de tipo number, si es int tiene que validar con la anotacion @Min
	@Min(5)
	@Max(5000)
	private Integer cuenta;
	
	@NotNull // las tipo Date se validan con NotNull
	@Past // para que sea una fecha pasada de la actual
	//@Future // para una fecha futura
	//@DateTimeFormat(pattern = "yyyy-MM-dd") // para darle el tipo de formato  y que no se cambie
	private Date fechaNacimiento;
	
	@Valid
	private Pais pais;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	
	public void setIdentificador(String identificador) {
	   this.identificador = identificador;
	}
	
	public Integer getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(Integer cuenta) {
		this.cuenta = cuenta;
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	} 
	
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public Pais getPais() {
		return pais;
	}
	
	public void setPais(Pais pais) {
		this.pais = pais;
	}

}