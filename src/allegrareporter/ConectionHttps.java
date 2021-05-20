/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allegrareporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naval Supply
 */
public class ConectionHttps {

    public String consulta(URL url) {
        try {
            //Abrir la coneccion
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            //Agregar parametros - Deberian ser los mismos para cualquier GET request
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            http.setRequestProperty("Authorization", "Basic am9yZ2UubWFnZGFsZW5vQG5hdmFsc3VwcGx5LmNvbS5teDpiYzVjYjI4N2FhNjcxOTI0M2Q5NQ==");

            //Imprimir el codigo de la respuesta y el mensaje
            //System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            //lectura del InputStream resultante
            //Deberia ser cambiado para almacenarse en un objeto JSON
            InputStreamReader isReader = new InputStreamReader(http.getInputStream());
            
            //desconectar el http cuando ya no hace falta
            BufferedReader reader = new BufferedReader(isReader);
            StringBuilder sb = new StringBuilder();
            String stringConsulta;
            while ((stringConsulta = reader.readLine()) != null) {
                sb.append(stringConsulta);
                
            }
            //imprimir el string
            //System.out.println(sb.toString());

            http.disconnect();

            String jsonFinal = sb.toString();
            //catch de errores
            return jsonFinal;
        } catch (MalformedURLException ex) {
            System.out.println("MALFORMACION");
            Logger.getLogger(AllegraReporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("MALFORMACION");
            Logger.getLogger(AllegraReporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // REALIZACION DE CONSULTAS
    public String consultaIngresos(String fecha1, String fecha2) {
        try {
            //formato requeirdo de la fecha yyyy-mm-dd
            //Coneccion
            //Direccion y parametros
            URL url = new URL("https://api.alegra.com/api/v1/reports/accounting/income?from=" + fecha1 + "&to=" + fecha2);
            System.out.println(url.toString());
            return consulta(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConectionHttps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String consultaEgresos(String fecha1, String fecha2) {
        try {
            //formato requeirdo de la fecha yyyy-mm-dd
            //Coneccion
            //Direccion y parametros
            URL url = new URL("https://api.alegra.com/api/v1/reports/accounting/expense?from=" + fecha1 + "&to=" + fecha2);
            return consulta(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConectionHttps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
