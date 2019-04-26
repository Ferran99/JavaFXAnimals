package application;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Carrera;
import modelo.CentroEstudio;
import modelo.Conexion;
import modelo.*;

public class FormularioAlumnosController implements Initializable {
    //Columnas
    //@FXML private TableColumn<Alumno,String> clmnNombre;

    /*@FXML private TableColumn<Alumno,String> clmnApellido;
	@FXML private TableColumn<Alumno,Number> clmnEdad;
	@FXML private TableColumn<Alumno,String> clmnGenero;
	@FXML private TableColumn<Alumno,Date> clmnFechaIngreso;
	@FXML private TableColumn<Alumno,CentroEstudio> clmnCentroEstudio;
	@FXML private TableColumn<Alumno,Carrera> clmnCarrera;*/
    @FXML
    private TableColumn<Animal, String> clmnNombre;
    @FXML
    private TableColumn<Animal, Parcela> clmnCentroEstudio;

    //Componentes GUI
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtFilter;
    @FXML
    private TextField txtEdad;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnActualizar;
     @FXML
    private Button btnParcela;

    @FXML
    private ComboBox<Parcela> cmbCentroEstudio;

    /*@FXML private ComboBox<Carrera> cmbCarrera;
	@FXML private ComboBox<CentroEstudio> cmbCentroEstudio;*/
    @FXML
    private TableView<Animal> tblViewAlumnos;
    //@FXML
    //private TableView<Animal> tblViewAnimal;
    //Colecciones
    // private ObservableList<Carrera> listaCarreras;
    // private ObservableList<CentroEstudio> listaCentrosEstudios;
    //private ObservableList<Alumno> listaAlumnos;
    // private ObservableList<Animal> listaAlumnos;
    private ObservableList<Animal> listaAnimal;
    private ObservableList<Parcela> lista;

    private Conexion conexion;

  @FXML
    public void filtar(KeyEvent key) {
        FilteredList <Animal> filterData = new FilteredList <>(listaAnimal, p -> true);
        //FilteredList<Animal> filterData = new FilteredList<>(listaAnimal, p -> true);

        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Animal -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String typedText = newValue.toLowerCase();

                if (Animal.getEstable().getNom_estable().toLowerCase().contains(typedText)) {
                    return true;
                }

                return false;
            });
            SortedList<Animal> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(tblViewAlumnos.comparatorProperty());
            tblViewAlumnos.setItems(sortedList);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexion = new Conexion();
        conexion.establecerConexion();

        //Inicializar listas
        //listaCentrosEstudios = FXCollections.observableArrayList();
        //listaAlumnos = FXCollections.observableArrayList();
        listaAnimal = FXCollections.observableArrayList();
        lista = FXCollections.observableArrayList();

        //Llenar listas
        //Carrera.llenarInformacion(conexion.getConnection(), listaCarreras);
        Parcela.llenarInformacion(conexion.getConnection(), lista);

        //Alumno.llenarInformacionAlumnos(conexion.getConnection(), listaAlumnos);
        Animal.llenarInformacionAnimal(conexion.getConnection(), listaAnimal);

        //Enlazar listas con ComboBox y TableView
        // cmbCarrera.setItems(listaCarreras);
        // cmbCentroEstudio.setItems(listaCentrosEstudios);
        //tblViewAlumnos.setItems(listaAlumnos);
        tblViewAlumnos.setItems(listaAnimal);
        cmbCentroEstudio.setItems(lista);

        //Enlazar columnas con atributos
        // txtCodigo.setCellValueFactory(new PropertyValueFactory<Animal, Integer>("codigo"));
        clmnNombre.setCellValueFactory(new PropertyValueFactory<Animal, String>("nom_animal"));
        // clmnApellido.setCellValueFactory(new PropertyValueFactory<Alumno, String>("apellido"));
        //clmnGenero.setCellValueFactory(new PropertyValueFactory<Alumno, String>("genero"));
        // clmnFechaIngreso.setCellValueFactory(new PropertyValueFactory<Alumno, Date>("fechaIngreso"));
        clmnCentroEstudio.setCellValueFactory(new PropertyValueFactory<Animal, Parcela>("estable"));
        //clmnCarrera.setCellValueFactory(new PropertyValueFactory<Alumno, Carrera>("carrera"));
        gestionarEventos();

        conexion.cerrarConexion();
    }

    public void gestionarEventos() {
        /*tblViewAlumnos.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Alumno>() {
            @Override
            public void changed(ObservableValue<? extends Alumno> arg0,
                    Alumno valorAnterior, Alumno valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCodigo.setText(String.valueOf(valorSeleccionado.getCodigoAlumno()));
                    txtNombre.setText(valorSeleccionado.getNombre());
                    txtApellido.setText(valorSeleccionado.getApellido());
                    txtEdad.setText(String.valueOf(valorSeleccionado.getEdad()));
                    if (valorSeleccionado.getGenero().equals("F")) {
                        rbtFemenino.setSelected(true);
                        rbtMasculino.setSelected(false);
                    } else if (valorSeleccionado.getGenero().equals("M")) {
                        rbtFemenino.setSelected(false);
                        rbtMasculino.setSelected(true);
                    }
                    dtpkrFecha.setValue(valorSeleccionado.getFechaIngreso().toLocalDate());
                    cmbCarrera.setValue(valorSeleccionado.getCarrera());
                    cmbCentroEstudio.setValue(valorSeleccionado.getCentroEstudio());

                    btnGuardar.setDisable(true);
                    btnEliminar.setDisable(false);
                    btnActualizar.setDisable(false);
                }
            }

        }
        );*/
        tblViewAlumnos.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Animal>() {
            @Override
            public void changed(ObservableValue<? extends Animal> arg0,
                    Animal valorAnterior, Animal valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCodigo.setText(String.valueOf(valorSeleccionado.getCodi_animal()));
                    txtNombre.setText(valorSeleccionado.getNom_animal());
                    /*  txtApellido.setText(valorSeleccionado.getApellido());*/
                    //txtEdad.setText(String.valueOf(valorSeleccionado.getEstable().getCodi_estable()));
                    /* if (valorSeleccionado.getGenero().equals("F")) {
                        rbtFemenino.setSelected(true);
                        rbtMasculino.setSelected(false);
                    } else if (valorSeleccionado.getGenero().equals("M")) {
                        rbtFemenino.setSelected(false);
                        rbtMasculino.setSelected(true);
                    }*/
 /*dtpkrFecha.setValue(valorSeleccionado.getFechaIngreso().toLocalDate());
                    cmbCarrera.setValue(valorSeleccionado.getCarrera());*/
                    cmbCentroEstudio.setValue(valorSeleccionado.getEstable());

                    btnGuardar.setDisable(true);
                    btnEliminar.setDisable(false);
                    btnActualizar.setDisable(false);
                }
            }

        }
        );
    }

    @FXML
    public void guardarRegistro() {
        //Crear una nueva instancia del tipo Alumno
        Animal a = new Animal(
                Integer.valueOf(txtCodigo.getText()),
                txtNombre.getText(),
                // Integer.valueOf(txtEdad.getText())
                cmbCentroEstudio.getSelectionModel().getSelectedItem()
        //Integer.valueOf(txtEdad.getText())
        );
        
        
        //
        //Integer.valueOf(txtEdad.getText())
        /* rbtFemenino.isSelected() ? "F" : "M",//Condicion?ValorVerdadero:ValorFalso
                Date.valueOf(dtpkrFecha.getValue()),
                cmbCentroEstudio.getSelectionModel().getSelectedItem(),
                cmbCarrera.getSelectionModel().getSelectedItem()*/

        //Llamar al metodo guardarRegistro de la clase Alumno
        conexion.establecerConexion();
        Integer animals =  Integer.valueOf(txtCodigo.getText());
        
       
        int resultado = a.guardarRegistro(conexion.getConnection());
        int Resultado = a.Disponibilitat(conexion.getConnection());
        conexion.cerrarConexion();
        
        

        if (resultado == 1) {
            listaAnimal.add(a);
            //JDK 8u>40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sido agregado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }
    }

    @FXML
    public void actualizarRegistro() {
        Animal a = new Animal(
                Integer.valueOf(txtCodigo.getText()),
                txtNombre.getText(),
                //Integer.valueOf(textEdad.getText())

                cmbCentroEstudio.getSelectionModel().getSelectedItem()
        );

        //txtApellido.getText(),
        //Integer.valueOf(txtEdad.getText()),
        //rbtFemenino.isSelected() ? "F" : "M",//Condicion?ValorVerdadero:ValorFalso
        // Date.valueOf(dtpkrFecha.getValue()),
        //cmbCentroEstudio.getSelectionModel().getSelectedItem());
        //cmbCarrera.getSelectionModel().getSelectedItem());
        conexion.establecerConexion();
        int resultado = a.actualizarRegistro(conexion.getConnection());
        conexion.cerrarConexion();

        if (resultado == 1) {
            listaAnimal.set(tblViewAlumnos.getSelectionModel().getSelectedIndex(), a);
            //JDK 8u>40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro actualizado");
            mensaje.setContentText("El registro ha sido actualizado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }
    }

    @FXML
    public void eliminarRegistro() {
        conexion.establecerConexion();
        int resultado = tblViewAlumnos.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
        conexion.cerrarConexion();

        if (resultado == 1) {
            listaAnimal.remove(tblViewAlumnos.getSelectionModel().getSelectedIndex());
            //JDK 8u>40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("El registro ha sido eliminado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }
    }

    @FXML
    public void limpiarComponentes() {
        txtCodigo.setText(null);
        txtNombre.setText(null);

        //txtEdad.setText(null);

        //cmbCarrera.setValue(null);
        cmbCentroEstudio.setValue(null);

        btnGuardar.setDisable(false);
        btnEliminar.setDisable(true);
        btnActualizar.setDisable(true);
    }
    
    public void cambiarScenaParcela(javafx.event.ActionEvent actionEvent)throws IOException {
        AnchorPane estables = (AnchorPane)FXMLLoader.load(getClass().getResource("Parceles.fxml"));
        Scene sceneEstables = new Scene(estables);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(sceneEstables);
        window.show();
    }

}
