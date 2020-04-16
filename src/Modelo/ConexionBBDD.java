package Modelo;
	import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConexionBBDD {

	private String url= "jdbc:oracle:thin:@localhost:1521:XE";
	private String usr = "SYSTEM";
	private String pwd = "1234";
	private Connection conexion;


	public ConexionBBDD()  {

			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conexion = DriverManager.getConnection(url, usr, pwd);

				if(!conexion.isClosed()) {
					System.out.println("Conexión establecida");

					//conexion.close();
				}
				else
					System.out.println("Fallo en Conexión");


			}catch (Exception e) {
				System.out.println("ERROR en conexión con ORACLE");
				e.printStackTrace();
			}

		}



	/*
	 * El método InsertarPersona devuelve un código de error para los siguientes casos:
	 *
	 * 0 - Persona insertada OK!
	 * 1 - Se ha queriro introducir uan persona con un email existente (Primary key violated)
	 * 2 - Otro fallo en el tipo de datos o en la base de datos al hacer la inserción
	 *
	 *
	 */
	public int InsertarPersona(String nombre, String apellido, String email, char sexo, boolean casado) throws SQLException{

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		String auxcasado = "N";
		if(casado == true)
			auxcasado = "S";

		// Preparo la sentencia SQL CrearTablaPersonas
		String insertsql = "INSERT INTO DAVID.PERSONAS VALUES ('" + nombre + "','" + apellido + "','" + email + "','" + sexo +"','"+auxcasado +"')";

		//ejecuto la sentencia
		try{
			int resultado = stm.executeUpdate(insertsql);

			if(resultado != 1)
				System.out.println("Error en la inserción " + resultado);
			else
				System.out.println("Persona insertada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			if(codeErrorSQL.equals("ORA-00001") ){
				System.out.println("Ya existe una persona con  ese email!!");
				return 1;
			}
			else{
				System.out.println("Ha habido algún problema con  Oracle al hacer la insercion");
				return 2;
			}

		}

	}


	/*
	 * El método InsertarPersona devuelve un código de error para los siguientes casos:
	 *
	 * 0 - Persona insertada OK!
	 * 1 - Se ha queriro introducir uan persona con un email existente (Primary key violated)
	 * 2 - Otro fallo en el tipo de datos o en la base de datos al hacer la inserción
	 *
	 *
	 */
	public int ModificarPersona(String nombre, String apellido, String email, char sexo, boolean casado) throws SQLException{

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		String auxcasado = "N";
		if(casado == true)
			auxcasado = "S";

		// Preparo la sentencia SQL CrearTablaPersonas
		String updatesql = "UPDATE DAVID.PERSONAS SET NOMBRE='" + nombre + "', APELLIDO ='" + apellido + "', SEXO = '" + sexo +"', CASADO = '"+auxcasado +"' WHERE EMAIL='"+email+"'";

		//ejecuto la sentencia
		try{
			int resultado = stm.executeUpdate(updatesql);

			if(resultado != 1)
				System.out.println("Error en la actualización " + resultado);
			else
				System.out.println("Persona actualizada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			if(codeErrorSQL.equals("ORA-00001") ){
				System.out.println("Ya existe una persona con  ese email!!");
				return 1;
			}
			else{
				System.out.println("Ha habido algún problema con  Oracle al hacer la insercion");
				return 2;
			}

		}

	}

	public ObservableList<Persona> ObtenerPersonas() throws SQLException{

		ObservableList<Persona> listapersonas = FXCollections.observableArrayList();

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		// Preparo la sentencia SQL CrearTablaPersonas
		String selectsql = "SELECT * FROM DAVID.PERSONAS";

		//ejecuto la sentencia
		try{
			ResultSet resultado = stm.executeQuery(selectsql);

			int contador = 0;
			while(resultado.next()){
				contador++;

				String nombre = resultado.getString(1);
				String apellido = resultado.getString(2);
				String email = resultado.getString(3);
				char sexo = resultado.getString(4).charAt(0);
				boolean casado = false;
				if(resultado.getString(5).charAt(0) == 'S')
					casado = true;

				Persona nueva = new Persona(nombre, apellido, email,sexo,casado);
				listapersonas.add(nueva);
			}

			if(contador==0)
				System.out.println("no data found");

		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println(codeErrorSQL);
		}

		return listapersonas;
	}


	public int BorrarPersona(String email) throws SQLException{

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		// Preparo la sentencia SQL
		String deletesql = "DELETE DAVID.PERSONAS WHERE EMAIL='"+email+"'";

		//ejecuto la sentencia
		try{
			int resultado = stm.executeUpdate(deletesql);

			if(resultado != 1)
				System.out.println("Error en el borrado " + resultado);
			else
				System.out.println("Persona borrada con éxito!!!");

			return 0;
		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println("Ha habido algún problema con  Oracle al hacer el borrado" + codeErrorSQL);
			return 2;
		}

	}



	public ObservableList<Persona> BuscarPersonas(String apellido) throws SQLException{

		ObservableList<Persona> listapersonas = FXCollections.observableArrayList();

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		// Preparo la sentencia SQL en función de lo que venga en apellido
		String selectsql = "";
		if(apellido.equals(""))
			selectsql = "SELECT * FROM DAVID.PERSONAS";
		else
			selectsql = "SELECT * FROM DAVID.PERSONAS WHERE APELLIDO LIKE '" + apellido +"%'";

		//ejecuto la sentencia
		try{
			ResultSet resultado = stm.executeQuery(selectsql);

			int contador = 0;
			while(resultado.next()){
				contador++;

				String nombre = resultado.getString(1);
				String apellidos = resultado.getString(2);
				String email = resultado.getString(3);
				char sexo = resultado.getString(4).charAt(0);
				boolean casado = false;
				if(resultado.getString(5).charAt(0) == 'S')
					casado = true;

				Persona nueva = new Persona(nombre, apellidos, email,sexo,casado);
				listapersonas.add(nueva);
			}

			if(contador==0)
				System.out.println("no data found");

		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println(codeErrorSQL);
		}

		return listapersonas;
	}

}

