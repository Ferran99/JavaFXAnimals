/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import modelo.Animal;

/**
 *
 * @author ferran
 */
public class Parcela {

    private IntegerProperty codi_estable;
    private StringProperty nom_estable;
    private IntegerProperty quantitat;
    private IntegerProperty disponible;
    Integer numDisponible;
    Integer newQuantitat;
    Statement stmt = null;
    Integer newnumAnmials;

    public Parcela(Integer codi_estable, String nom_estable, Integer quantitat, Integer disponible) {
        this.codi_estable = new SimpleIntegerProperty(codi_estable);
        this.nom_estable = new SimpleStringProperty(nom_estable);
        this.quantitat = new SimpleIntegerProperty(quantitat);
        this.disponible = new SimpleIntegerProperty(disponible);

    }

    public Integer getCodi_estable() {
        return codi_estable.get();
    }

    public void setCodi_estable(Integer codi_estable) {
        this.codi_estable = new SimpleIntegerProperty(codi_estable);
    }

    public String getNom_estable() {
        return nom_estable.get();
    }

    public void setNom_estable(String nom_estable) {
        this.nom_estable = new SimpleStringProperty(nom_estable);
    }

    public Integer getQuantitat() {
        return quantitat.get();
    }

    public void setQuantitat(Integer quantitat) {
        this.quantitat = new SimpleIntegerProperty(quantitat);
    }

    public Integer getDisponibilitat() {
        return disponible.get();
    }

    public void setDisponibilitat(Integer disponibilitat) {
        this.disponible = new SimpleIntegerProperty(disponibilitat);
    }

    public int guardarRegistro(Connection connection) {
        try {
            //Evitar inyeccion SQL.
            PreparedStatement instruccion
                    = connection.prepareStatement("INSERT INTO tbl_estable (codi_estable, nom_estable, quantitat, disponible)  VALUES (?, ?, ?, ?)");
            instruccion.setInt(1, codi_estable.get());
            instruccion.setString(2, nom_estable.get());
            instruccion.setInt(3, quantitat.get());
            instruccion.setInt(4, disponible.get());

            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int actualizarRegistro(Connection connection) {
        try {
             stmt = connection.createStatement();
           System.out.println("Arriba a la funcio actualizarRegistro");
            String sql = "update tbl_estable set codi_estable= " + codi_estable.get() + ", nom_estable = '" + nom_estable.get() + "', quantitat = " + quantitat.get() + " where codi_estable = " + codi_estable.get();
             System.out.println("Executa a la funcio actualizarRegistro");
            return stmt.executeUpdate(sql);
            /* PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_estable "
                            + " SET 	codi_estable= ?, nom_estable = ?,  "
                            + " quantitat = ?,  "
                            + " disponible = ?,  "
                            + " WHERE codi_estable = ?"
                    );
            instruccion.setInt(1, codi_estable.get());
            instruccion.setString(2, nom_estable.get());
            instruccion.setInt(3, quantitat.get());
            instruccion.setInt(4, disponible.get());

            instruccion.setInt(1, codi_estable.get());

            return instruccion.executeUpdate();*/

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM tbl_estable "
                    + "WHERE codi_estable = ?"
            );
            instruccion.setInt(1, codi_estable.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void llenarInformacion(Connection connection, ObservableList<Parcela> lista) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(
                    "SELECT codi_estable, nom_estable, quantitat, disponible FROM tbl_estable"
            );
            while (resultado.next()) {
                lista.add(
                        new Parcela(
                                resultado.getInt("codi_estable"),
                                resultado.getString("nom_estable"),
                                resultado.getInt("quantitat"),
                                resultado.getInt("disponible")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        modelo.Conexion conexion;
        conexion = new modelo.Conexion();
        conexion.establecerConexion();
        Actualitza(conexion.getConnection());
        conexion.cerrarConexion();
        return nom_estable.get() + " Disponible: " + disponible.get();
    }

    public int Actualitza(Connection connection) {

       
        Integer codi = codi_estable.get();
        

        try {

            stmt = connection.createStatement();

            ResultSet RS = stmt.executeQuery("select count(codi_estable) from tbl_animal where codi_estable = " + codi);
            while (RS.next()) {
                newnumAnmials = RS.getInt(1);
            }
            System.out.println("Count codi_estable: "+newnumAnmials);
            System.out.println("Quantitat: "+quantitat.get());

            numDisponible = quantitat.get() - newnumAnmials;
             String sql = "update tbl_estable set disponible = "+numDisponible+"  where codi_estable = " + codi_estable.get();
            
             stmt.executeUpdate(sql);
           setDisponibilitat(numDisponible);

            return numDisponible;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

}
