package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ferran
 */
public class Animal {

    private IntegerProperty codi_animal;
    private StringProperty nom_animal;
    private Parcela estable;
    Statement stmt = null;
    Integer Disponible;

    public Animal(Integer codi_animal, String nom_animal, Parcela estable) {
        this.codi_animal = new SimpleIntegerProperty(codi_animal);
        this.nom_animal = new SimpleStringProperty(nom_animal);
        this.estable = estable;
    }

    public Integer getCodi_animal() {
        return codi_animal.get();
    }

    public void setCodi_animal(Integer codi_animal) {
        this.codi_animal = new SimpleIntegerProperty(codi_animal);
    }

    public String getNom_animal() {
        return nom_animal.get();
    }

    public void setNom_animal(String nom_animal) {
        this.nom_animal = new SimpleStringProperty(nom_animal);
    }

    public Parcela getEstable() {
        return estable;
    }

    public void setEstable(Parcela estable) {
        this.estable = estable;
    }

    public IntegerProperty codi_animalProperty() {
        return codi_animal;
    }

    public StringProperty nom_animalProperty() {
        return nom_animal;
    }

    public Integer getDisponible() {
        return Disponible;
    }

    public void setDisponible(Integer Disponible) {
        this.Disponible = Disponible;
    }
    
    

    public int guardarRegistro(Connection connection) {
        try {
            //Evitar inyeccion SQL.
            PreparedStatement instruccion
                    = connection.prepareStatement("INSERT INTO tbl_animal (codi_animal, nom_animal, codi_estable)  VALUES (?, ?, ?)");
            instruccion.setInt(1, codi_animal.get());
            instruccion.setString(2, nom_animal.get());
            instruccion.setInt(3, estable.getCodi_estable());

            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int actualizarRegistro(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_animal "
                            + " SET 	codi_animal= ?, nom_animal = ?,  "
                            + " codi_estable = ?,  "
                            + " WHERE codi_animal = ?"
                    );
            instruccion.setInt(1, codi_animal.get());
            instruccion.setString(2, nom_animal.get());
            instruccion.setInt(3, estable.getCodi_estable());
            instruccion.setInt(4, codi_animal.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM tbl_animal "
                    + "WHERE codi_animal = ?"
            );
            instruccion.setInt(1, codi_animal.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void llenarInformacionAnimal(Connection connection,
            ObservableList<Animal> listaAnimal) {
        try {
            Statement instruccion = connection.createStatement();
            ResultSet resultado = instruccion.executeQuery(
                    "SELECT A.codi_animal, A.nom_animal, A.codi_estable, C.nom_estable, C.quantitat, C.disponible FROM tbl_animal A INNER JOIN tbl_estable C ON (A.codi_estable = C.codi_estable);"
            );
            while (resultado.next()) {
                listaAnimal.add(
                        new Animal(
                                resultado.getInt("codi_animal"),
                                resultado.getString("nom_animal"),
                                new Parcela(resultado.getInt("codi_estable"),
                                        resultado.getString("nom_estable"),
                                        resultado.getInt("quantitat"),
                                        resultado.getInt("disponible")
                                )
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public int Disponibilitat(Connection connection) {
        try {
            /*PreparedStatement instruccion
                    = connection.prepareStatement( "UPDATE tbl_estable  SET 	disponible = ?,   WHERE codi_estable = ?" );
            */
              /*  instruccion.setInt(Disponible );*/
               Integer codi = estable.getCodi_estable();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select count("+estable.getCodi_estable()+") from tbl_animal where codi_estable = "+codi);
            while (rs.next()) 
						    { 
						        setDisponible( 100 - rs.getInt (1)) ;
						    }
            stmt = connection.createStatement();
            
             String sql = "update tbl_estable set disponible = "+Disponible+" where codi_estable = "+codi;
            return stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
