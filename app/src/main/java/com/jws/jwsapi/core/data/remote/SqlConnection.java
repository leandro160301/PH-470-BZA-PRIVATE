package com.jws.jwsapi.core.data.remote;

import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * IMPLEMENTACION:
 * connection= SQLConnection.connect(ip,instance,db,user,pass);
 * if(connection!=null){
 * runOnUiThread(new Runnable() {
 *
 * @Override public void run() {
 * // Código que se ejecutará en el hilo de la interfaz de user
 * Mensaje("Conexion exitosa", R.layout.customtoastok);
 * }
 * });
 */

public class SqlConnection {
    private static final String LOG = "DEBUG";
    // private static String instance = "SQLSERVER";
    private static final String port = "1433";
    private static final String classs = "net.sourceforge.jtds.jdbc.Driver";

    public static Connection connect(@NonNull String IpAddress, String Instance, String Database, String Username, String Password) {
        Connection conn = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs).newInstance();
            String ConnURL = "jdbc:jtds:sqlserver://" + IpAddress + ":" + port + ";" + "instance=" + Instance + ";" + "databaseName=" + Database + ";";
            conn = DriverManager.getConnection(ConnURL, Username, Password);
            System.out.println("Connection successful!");
        } catch (SQLException | ClassNotFoundException e) {
            Log.d(LOG, e.getMessage());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return conn;
    }
}