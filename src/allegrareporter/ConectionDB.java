/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allegrareporter;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Naval Supply
 */
public class ConectionDB {

    private String url;
    private String username;
    private String password;

    public void connect(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // CREACION DE REPORTES
    /* Debo crear un reporte
        Debo recibir 
        Intervalo de tiepo del reporte
        
        Debo insertar
        Nueva ID o hacerla autoIncrement
        Fecha en que lo genere (Hoy o curdate())
        Fecha intervalo inferior
        Fecha intervalo superior
        Status Si esta activo o no para soft delete default activo
     */
    public void test() {
        try {

            Connection con = DriverManager.getConnection(url, username, password);
            con.close();
        } catch (SQLException e) {
            System.out.println("error" + e);
            throw new IllegalStateException("error", e);
        }
    }

    public void createReporte(String fecha1, String fecha2) {
        String insert = "insert into reporte(id, generation_date, start_date, end_date, status) select ifnull(MAX(id),0)+ 1,curdate(), '" + fecha1 + "', '" + fecha2 + "','active' from reporte";
        realizarInsert(insert);
    }

    /*  Tengo que recibir
        id
        Reporte_id
        Tipo_consulta
        Descripcion
        Status
        Balance
     */
    public void createConsulta(JSONObject json) {
        String insert = "insert into consulta values("+json.getInt("id")+",(select MAX(id) from reporte), " + json.getDouble("balance") + ", 'active','" + json.optString("description","") + "', '" + json.getString("type") + "')";
        realizarInsert(insert);
    }

    public void createCategoria(JSONObject json, String padre, String consulta) {
        /* recibir la categoria
        Subir la categoria
        Atributos a subir
        
        ID              int
        Consulta_id     int
        balance         double
        text            text
       
        Si tiene un padre debe agrerar 
        Categoria_id    int
         */
        String insert = "insert into categoria values("+json.getInt("id")+","+padre+","+consulta+","+json.getDouble("balance")+",'"+json.getString("text")+"')";
        realizarInsert(insert);
    }

    public void realizarInsert(String insert) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement state = con.createStatement();
            state.executeUpdate(insert);
            con.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Error Insert" + insert, e);
        }
    }
}
