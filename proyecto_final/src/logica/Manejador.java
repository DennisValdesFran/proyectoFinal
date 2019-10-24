package logica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import persistencia.Conn;
import persistencia.ManejadorBD;

public class Manejador {
	public ArrayList<Usuario> usuarios = new ArrayList<>();
	public ArrayList<Libro> libros = new ArrayList<>();;
	public ArrayList<Prestamo> prestamos = new ArrayList<>();;
	private ManejadorBD manBD = new ManejadorBD();

	private static Manejador instance = null;

	private Manejador() {
		this.usuarios = new ArrayList<Usuario>();
		this.libros = new ArrayList<Libro>();
		this.prestamos = new ArrayList<Prestamo>();
	}

	public static Manejador getInstance() {
		if (instance == null) {
			instance = new Manejador();
		}

		return instance;
	}

	// Methods

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public ArrayList<Libro> getLibros() {
		return libros;
	}

	public void setLibros(ArrayList<Libro> libros) {
		this.libros = libros;
	}

	public ArrayList<Prestamo> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(ArrayList<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}

	// Parsear TipoUsuario

	public TipoUsuario parsearTipoUsuario(String tipo) {
		TipoUsuario tipoUsuario = null;

		if (tipo == "ESTUDIANTE") {
			tipoUsuario = TipoUsuario.ESTUDIANTE;
		}
		if (tipo == "ESTUDIANTE") {
			tipoUsuario = TipoUsuario.PROFESOR;
		}
		if (tipo == "ESTUDIANTE") {
			tipoUsuario = TipoUsuario.BIBLIOTECARIO;
		}

		return tipoUsuario;
	}

	// Usuarios: alta, listar, consultar, buscar y modificar
	public void altaUsuario() {

		this.usuarios.clear();

		try {
			Conn connect = new Conn();
			Connection con = connect.conectarMySQL();

			String query1 = "SELECT * FROM estudiantes e INNER JOIN usuarios u ON e.id = u.id";
			String query2 = "SELECT * FROM profesores p INNER JOIN usuarios u ON p.id = u.id";
			String query3 = "SELECT * FROM bibliotecarios b INNER JOIN usuarios u ON b.id = u.id";

			Statement st = con.createStatement();
			ResultSet x = st.executeQuery(query1);

			while (x.next()) {

				int id = x.getInt("id");
				int ci = x.getInt("ci");
				String nombre = x.getString("nombre");
				String apellido = x.getString("apellido");
				String mail = x.getString("mail");
				String pass = x.getString("pass");
				TipoUsuario tipo = parsearTipoUsuario(x.getString("tipo"));

				if (x.getString("orientacion").equals("TIC")) {

					Estudiante estudiante = new Estudiante(id, ci, nombre, apellido, mail, pass, Orientacion.TIC, tipo);
					this.usuarios.add(estudiante);
				}

				if (x.getString("orientacion").equals("ADM")) {

					Estudiante estudiante = new Estudiante(id, ci, nombre, apellido, mail, pass, Orientacion.ADM, tipo);
					this.usuarios.add(estudiante);
				}

			}

			ResultSet y = st.executeQuery(query2);

			while (y.next()) {

				int id = y.getInt("id");
				int ci = y.getInt("ci");
				String nombre = y.getString("nombre");
				String apellido = y.getString("apellido");
				String mail = y.getString("mail");
				String pass = y.getString("pass");
				TipoUsuario tipo = parsearTipoUsuario(y.getString("tipo"));

				if (y.getString("orientacion").equals("TIC")) {

					Profesor profesor = new Profesor(id, ci, nombre, apellido, mail, pass, Orientacion.TIC, tipo);
					this.usuarios.add(profesor);
				}
				if (y.getString("orientacion").equals("ADM")) {

					Profesor profesor = new Profesor(id, ci, nombre, apellido, mail, pass, Orientacion.ADM, tipo);
					this.usuarios.add(profesor);
				}
				if (y.getString("orientacion").equals("TICYADM")) {

					Profesor profesor = new Profesor(id, ci, nombre, apellido, mail, pass, Orientacion.ADMYTIC, tipo);
					this.usuarios.add(profesor);
				}
			}

			ResultSet z = st.executeQuery(query3);

			while (z.next()) {

				int id = z.getInt("id");
				int ci = z.getInt("ci");
				String nombre = z.getString("nombre");
				String apellido = z.getString("apellido");
				String mail = z.getString("mail");
				String pass = z.getString("pass");
				TipoUsuario tipo = parsearTipoUsuario(z.getString("tipo"));

				Bibliotecario bibliotecario = new Bibliotecario(id, ci, nombre, apellido, mail, pass, tipo);
				this.usuarios.add(bibliotecario);
			}

			x.close();
			st.close();

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public Usuario consultaUsuario(String mail) {
		Usuario consulta = null;

		for (int i = 0; i < this.usuarios.size(); i++) {
			if (this.usuarios.get(i).getMail() == mail) {
				consulta = this.usuarios.get(i);
				break;
			}
		}

		return consulta;
	}

	public Usuario buscarUsuario(int CI) {
		Usuario busqueda = null;

		for (int i = 0; i < this.usuarios.size(); i++) {
			if (this.usuarios.get(i).getCI() == CI) {
				busqueda = this.usuarios.get(i);
				break;
			}
		}

		return busqueda;
	}

	public ArrayList<Usuario> listarUsuariosExistentes() {
		return getUsuarios();
	}

	public void modificarDatosUsuarios(int id, int CI, String nombre, String apellido, String mail, String password) {

		for (int i = 0; i < this.usuarios.size(); i++) {
			if (this.usuarios.get(i).getId() == id) {

				this.usuarios.get(i).setCI(CI);
				this.usuarios.get(i).setNombre(nombre);
				this.usuarios.get(i).setApellido(apellido);
				this.usuarios.get(i).setMail(mail);
				this.usuarios.get(i).setPassword(password);

				manBD.modificarDatos(this.usuarios.get(i).getId(), this.usuarios.get(i).getCI(),
						this.usuarios.get(i).getNombre(), this.usuarios.get(i).getApellido(),
						this.usuarios.get(i).getMail(), this.usuarios.get(i).getPassword());
			}
		}

	}

	// Prestamos: alta, consulta, listar
	public void altaPrestamo() {
		this.prestamos.clear();

		try {
			Conn connect = new Conn();
			Connection con = connect.conectarMySQL();

			String query = "SELECT * FROM prestamos";

			Statement st = con.createStatement();
			ResultSet x = st.executeQuery(query);

			while (x.next()) {

				int id = x.getInt("id");
				Boolean devuelto = x.getBoolean("devuelto");
				Date fecha_solicitud = x.getDate("fecha_solicitud");
				Date fecha_devolucion= x.getDate("fecha_devolucion");
				int id_usuario = x.getInt("id_usuario");
				int codigo_libro = x.getInt("codigo_libro");
				
				Prestamo prestamo = new Prestamo(codigo_libro, fecha_devolucion, fecha_devolucion, devuelto);
				
			}

			x.close();
			st.close();

		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	// Libros
 	public void altaLibro() {

		this.usuarios.clear();

		try {
			Conn connect = new Conn();
			Connection con = connect.conectarMySQL();

			String query = "SELECT * FROM libros";

			Statement st = con.createStatement();
			ResultSet x = st.executeQuery(query);

			while (x.next()) {

				String codigoAnima = x.getString("codigo");
				String autor = x.getString("autor");
				int fechaPubl = x.getInt("anio_publicacion");
				String nroEdicion = x.getString("numero_edicion");
				String editorial = x.getString("editorial");
				String descripcion = x.getString("descripcion");
				int cantEjemplares = x.getInt("cant_ejemplares");
				boolean hayEjemplarDisponible = x.getBoolean("ejemplares_disponibles");
				long codigoISBN = x.getLong("isbn");
				String genero = x.getString("genero");
				String imagUrl = x.getString("link_portada");

				Libro libro = new Libro(codigoAnima, autor, fechaPubl, nroEdicion, editorial, descripcion,
						cantEjemplares, hayEjemplarDisponible, codigoISBN, genero, imagUrl);

				System.out.println(libro);

			}

			x.close();
			st.close();

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public ArrayList<Libro> listarLibros() {
		return getLibros();
	}
}
