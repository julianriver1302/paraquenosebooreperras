package com.codiPlayCo.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String apellido;
	private String email;
	private String password;
	private String documento;
	private String tipoDocumento;
	private String celular;
	private Date fechaNacimiento;
	private Date fecharegistro;
	private Date ultimoAcceso;
	private String activo;
	private String avatar;

	@OneToMany(mappedBy = "usuario")
	private List<ActividadesEstudiantes> actividadesEstudiantes;

	@OneToMany(mappedBy = "usuario")
	private List<AsignacionDocente> asignacionDocente;

	@OneToMany(mappedBy = "usuario")
	private List<Actividades> actividades;

	@ManyToOne
	private Rol rol;

	public Usuario() {

	}

	public Usuario(Integer id, String nombre, String apellido, String email, String password, String documento,
			String tipoDocumento,  String celular, Date fechaNacimiento, Date fecharegistro,
			Date ultimoAcceso, String activo, String avatar, List<ActividadesEstudiantes> actividadesEstudiantes,
			List<AsignacionDocente> asignacionDocente, List<Actividades> actividades, Rol rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.documento = documento;
		this.tipoDocumento = tipoDocumento;

		this.celular = celular;
		this.fechaNacimiento = fechaNacimiento;
		this.fecharegistro = fecharegistro;
		this.ultimoAcceso = ultimoAcceso;
		this.activo = activo;
		this.avatar = avatar;
		this.actividadesEstudiantes = actividadesEstudiantes;
		this.asignacionDocente = asignacionDocente;
		this.actividades = actividades;
		this.rol = rol;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Date getFecharegistro() {
		return fecharegistro;
	}

	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}

	public Date getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(Date ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<ActividadesEstudiantes> getActividadesEstudiantes() {
		return actividadesEstudiantes;
	}

	public void setActividadesEstudiantes(List<ActividadesEstudiantes> actividadesEstudiantes) {
		this.actividadesEstudiantes = actividadesEstudiantes;
	}

	public List<AsignacionDocente> getAsignacionDocente() {
		return asignacionDocente;
	}

	public void setAsignacionDocente(List<AsignacionDocente> asignacionDocente) {
		this.asignacionDocente = asignacionDocente;
	}

	public List<Actividades> getActividades() {
		return actividades;
	}

	public void setActividades(List<Actividades> actividades) {
		this.actividades = actividades;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email
				+ ", password=" + password + ", documento=" + documento + ", tipoDocumento=" + tipoDocumento
				+ ", celular=" + celular + ", fechaNacimiento=" + fechaNacimiento + ", fecharegistro=" + fecharegistro
				+ ", ultimoAcceso=" + ultimoAcceso + ", activo=" + activo + ", avatar=" + avatar + "]";
	}

}