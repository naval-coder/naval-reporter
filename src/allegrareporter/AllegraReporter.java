/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allegrareporter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import org.json.*;

/**
 *
 * @author Naval Supply
 */
public class AllegraReporter {

    public static final String url = "jdbc:mysql://162.241.2.36:3306/navalsup_reporter";
    public static final String username = "navalsup_report";
    public static final String password = "reH*X+?H[fct";
    public static String consulta_id = "";
    public static ConectionDB db = new ConectionDB();
    public static ConectionHttps https = new ConectionHttps();
    public static int cicloN = 0;

    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        //System.out.println(password);
        java.security.Security.setProperty("jdk.tls.disabledAlgorithms","");
        Connection con = DriverManager.getConnection(url, username, password);

        db.connect(url, username, password);

        db.createReporte("2021-01-01", "2021-05-12");

        String Ingresos = https.consultaIngresos("2021-01-01", "2021-05-12");
        String Egresos = https.consultaEgresos("2021-01-01", "2021-05-12");
        InsertJson(Ingresos, "null");
        InsertJson(Egresos, "null");
    }

    //CREACION DE JSONS
    public static void InsertJson(String stringJson, String padre) {

        cicloN++;
        JSONObject object = new JSONObject(stringJson);
        String[] keys = JSONObject.getNames(object);
        System.out.println("");
        System.out.println(Arrays.toString(keys));

        // aqui llmar para insertar al padre en la tabla
        //guardar valor del id del padre y eniarlo a lo siguiente
        System.out.println("padre " + padre);
        if (keys[0].equals("categories")) {
            JSONArray array = new JSONArray(object.get("categories").toString());
            JSONObject element = array.getJSONObject(0);
            InsertJson(element.toString(), "null");
        } else {

            if (padre.equals("null")) {
                db.createConsulta(object);
                consulta_id = object.getInt("id") + "";

            } else {
                System.out.println(cicloN);
                if (cicloN <= 3) {
                    padre = "null";
                }
                db.createCategoria(object, padre, consulta_id);
                System.out.println("Categoria");
            }

            String idActual = Integer.toString(object.getInt("id"));
            System.out.println(idActual);
            if (object.get("children").toString().length() > 2) {
                JSONArray array = new JSONArray(object.get("children").toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject element = array.getJSONObject(i);
                    InsertJson(element.toString(), idActual);
                }
            }
        }
        cicloN--;
        System.out.println("");
    }
}
