package application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import modelo.*;
/**
 *
 * @author ferran
 */
public class FolularioParcelesController implements Initializable {
    
    
  
  //Columnas
    
     @FXML
    private TableColumn<Parcela, String> clmnNom;
    @FXML
    private TableColumn<Parcela, Number> quantitat;
    
     @FXML
    private TableColumn<Parcela, Number> disponible;
    
    //Componentes GUI
    
     @FXML
    private TextField txtCodi;
    @FXML
    private TextField txtNom;
  
    @FXML
    private TextField txtQuantitat;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnActualizar;
     @FXML
    private Button btnAnimals;

    
     @FXML
    private TableView<Parcela> tblViewParcela;
     
      private ObservableList<Parcela> lista;
      private modelo.Conexion conexion;
      
       @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexion = new modelo.Conexion();
        conexion.establecerConexion();

        //Inicializar listas
        //listaCentrosEstudios = FXCollections.observableArrayList();
        //listaAlumnos = FXCollections.observableArrayList();
        lista = FXCollections.observableArrayList();

        //Llenar listas
        Parcela.llenarInformacion(conexion.getConnection(), lista);

        

        //Enlazar listas con ComboBox y TableView
        tblViewParcela.setItems(lista);

        //Enlazar columnas con atributos
        clmnNom.setCellValueFactory(new PropertyValueFactory<Parcela, String>("nom_estable"));
        quantitat.setCellValueFactory(new PropertyValueFactory<Parcela, Number>("quantitat"));
        disponible.setCellValueFactory(new PropertyValueFactory<Parcela, Number>("disponibilitat"));
        gestionarEventos();

        conexion.cerrarConexion();
    }
    
    public void gestionarEventos() {
       
        tblViewParcela.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Parcela>() {
            @Override
            public void changed(ObservableValue<? extends Parcela> arg0,
                    Parcela valorAnterior, Parcela valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCodi.setText(String.valueOf(valorSeleccionado.getCodi_estable()));
                    txtNom.setText(valorSeleccionado.getNom_estable());
                
                   txtQuantitat.setText(String.valueOf(valorSeleccionado.getQuantitat()));
                   

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
        Parcela a = new Parcela(
                Integer.valueOf(txtCodi.getText()),
                txtNom.getText(),
                // Integer.valueOf(txtEdad.getText())
                 Integer.valueOf(txtQuantitat.getText()),
                null
                
                
                
        
        );
        

        //Llamar al metodo guardarRegistro de la clase Alumno
        conexion.establecerConexion();
        int resultado = a.guardarRegistro(conexion.getConnection());
        conexion.cerrarConexion();

        if (resultado == 1) {
            lista.add(a);
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
        Parcela a = new Parcela(
                Integer.valueOf(txtCodi.getText()),
                txtNom.getText(),
                //Integer.valueOf(textEdad.getText())

                Integer.valueOf(txtQuantitat.getText()),
                null
               
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
            lista.set(tblViewParcela.getSelectionModel().getSelectedIndex(), a);
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
        int resultado = tblViewParcela.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
        conexion.cerrarConexion();

        if (resultado == 1) {
            lista.remove(tblViewParcela.getSelectionModel().getSelectedIndex());
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
        txtCodi.setText(null);
        txtNom.setText(null);

        txtQuantitat.setText(null);

        //cmbCarrera.setValue(null);

        btnGuardar.setDisable(false);
        btnEliminar.setDisable(true);
        btnActualizar.setDisable(true);
    }
    public void cambiarScenaAnimals(javafx.event.ActionEvent actionEvent)throws IOException {
        AnchorPane estables = (AnchorPane)FXMLLoader.load(getClass().getResource("FormularioAlumnos.fxml"));
        Scene sceneEstables = new Scene(estables);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(sceneEstables);
        window.show();
    }
    
}
