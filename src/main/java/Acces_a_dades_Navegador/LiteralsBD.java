package Acces_a_dades_Navegador;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

class LiteralsBD {
    Connection connection;
    String language;
    LiteralsBD() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/accesadades", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getLanguageById(1);
    }

    String getLanguageById(int id){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from idioma where idi_id = "+ id +";");
            this.language = resultSet.getString(2);
            return resultSet.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    String getMessageByQuery(String query){
        try {
            Statement statement = connection.createStatement();
//            select * from literal where lit_clau = 'nopermisos' and idi_cod="cat";
            ResultSet resultSet = statement.executeQuery("select * from literal where lit_clau = "+ "'"+ query + "' and idi_cod="+ "'" + this.language + "';");
            System.out.println(resultSet.getString(3));
            return resultSet.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }
}

