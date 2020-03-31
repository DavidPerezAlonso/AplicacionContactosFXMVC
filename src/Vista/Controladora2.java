package Vista;

import java.util.Optional;

import Modelo.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controladora2 {

	@FXML
	private Button btnGrabar;

	@FXML
	private Button btnBorrar;

	@FXML
	private Button btnEliminar;

	@FXML
	private CheckBox chkcasado;

	@FXML
	RadioButton hombre;

	@FXML
	RadioButton mujer;

	@FXML
	ToggleGroup sexo;

	@FXML
	private TableView<Persona> tabla;

	@FXML
	private TableColumn<Persona,String> col_nombre;

	@FXML
	private TableColumn<Persona,String> col_apellido;

	@FXML
	private TableColumn<Persona,String> col_email;

	@FXML
	private TableColumn<Persona,Character> col_sexo;

	@FXML
	private TableColumn<Persona,Boolean> col_casado;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField email;



	ObservableList<Persona> datos = FXCollections.observableArrayList(

			new Persona("David", "Perez", "hola@hola.com",'H',true),
			new Persona("Cris", "Perez", "hola2@hola.com",'M',false)
			);

	// Atributos necesarios para codificar la edicion
	private boolean edicion;
	private int indiceedicion;


	public void initialize(){
		tabla.setItems(this.datos);

		col_nombre.setCellValueFactory(new PropertyValueFactory<Persona,String>("Nombre"));
		col_apellido.setCellValueFactory(new PropertyValueFactory<Persona,String>("Apellido"));
		col_email.setCellValueFactory(new PropertyValueFactory<Persona,String>("email"));
		col_sexo.setCellValueFactory(new PropertyValueFactory<Persona,Character>("sexo"));
		col_casado.setCellValueFactory(new PropertyValueFactory<Persona,Boolean>("casado"));

		// Al arrancar la vista se pone edicion a false
		edicion = false;
		indiceedicion = 0;

	}

	public void Guardar(){

		boolean casado = chkcasado.isSelected();
		char sexo;

		if(hombre.isSelected())
			sexo = 'H';
		else
			sexo = 'M';

		// Añadir un chequeo de campos vacíos o de validación de formato como el email
		if(txtNombre.getText().equals("") || txtApellido.getText().equals("") || email.getText().equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("Observa que hayas introducido todos lso datos");
			alert.setContentText("¡No se pueden grabar campos vacíos!");
			alert.showAndWait();
		}
		else{

			if(edicion == true){

				Persona editada = datos.get(indiceedicion);
				editada.setNombre(txtNombre.getText());
				editada.setApellido(txtApellido.getText());
				editada.setEmail(email.getText());
				editada.setCasado(casado);
				editada.setSexo(sexo);
				datos.set(indiceedicion, editada);

			}
			else{
				Persona nuevo = new Persona(txtNombre.getText(),txtApellido.getText(),email.getText(),sexo,casado);
				datos.add(nuevo);
			}

		}

	}

	public void Eliminar(){
		int index = tabla.getSelectionModel().getSelectedIndex();
		if( index >= 0){

			Persona seleccionada = tabla.getSelectionModel().getSelectedItem();

			// Se abre un dialog box de confirmacion de eliminar
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Conformación!!!");
			alert.setHeaderText("Por favor confirme el borrado");
			alert.setContentText("Dese borrar al usuario "+ seleccionada.getNombre() + " " +seleccionada.getApellido() +" ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    // ... user chose OK
				datos.remove(seleccionada);
				tabla.setItems(this.datos);

				Borrar();
			}

		}else{

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error en selección de datos");
			alert.setContentText("NO HAY NINGUN ELEMENTO SELECCIONADO!");
			alert.showAndWait();

		}
	}

	public void Borrar(){
		txtNombre.setText("");
		txtApellido.setText("");
		email.setText("");
		chkcasado.setSelected(false);

		edicion = false;
		indiceedicion = 0;
	}

	public void Editar(){


		int index = tabla.getSelectionModel().getSelectedIndex();


		if( index >= 0){

			// Activo la "funcionalidad" de editar para luego que el botón guardar sepa a qué PErsona estoy "editando"
			edicion = true;
			indiceedicion = index;


			Persona seleccionada = tabla.getSelectionModel().getSelectedItem();

			txtNombre.setText(seleccionada.getNombre());
			txtApellido.setText(seleccionada.getApellido());
			email.setText(seleccionada.getEmail());
			chkcasado.setSelected(seleccionada.isCasado());
			if(seleccionada.getSexo() == 'H')
				hombre.setSelected(true);
			else
				mujer.setSelected(true);


		}
	}




}
