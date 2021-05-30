package Core;

import Annotations.MyColumn;
import Annotations.MyEntity;
import Annotations.MyID;
import Exceptions.ClassHasNoColumnsException;
import Exceptions.ClassHasNoIdException;
import Exceptions.ClassNotEntityException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ORM {
    protected static Connection connection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(Files.newBufferedReader(Path.of("src/main/resources/connection.config")));
        String url = properties.getProperty("app.url");
        String user = properties.getProperty("app.user");
        String password = properties.getProperty("app.password");
        return DriverManager.getConnection(url + "?user=" + user + "&password=" + password);
    }

    protected static void tables(Connection connection) throws IOException, ClassNotFoundException, ClassNotEntityException, ClassHasNoIdException, ClassHasNoColumnsException, SQLException {
        Properties properties = new Properties();
        properties.load(Files.newBufferedReader(Path.of("src/main/resources/connection.config")));
        List<String> tables = Arrays.asList(properties.getProperty("app.tables").split(";"));
        checkTableValid(tables);
        for (String table:tables) {
            String table_name =Class.forName(table).getAnnotation(MyEntity.class).value();
            Field id=getID(table);
            if(tableExistsSQL(connection,table_name)){
                updateTable(connection,table_name,table);
            }else {
                createTable(connection,table,table_name,id);
            }
        }
    }

    static boolean tableExistsSQL(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        if (tables.next()) {
            return true;
        }
        else {
            return false;
        }
    }

    static void checkTableValid(List<String> tables) throws ClassNotFoundException, ClassNotEntityException, ClassHasNoIdException, ClassHasNoColumnsException {
        List<Class> classes = new ArrayList<>();
        for (String table : tables) {
            classes.add(Class.forName(table));
        }
        List<Field> columns;
        for (Class cl : classes) {
            if (!cl.isAnnotationPresent(MyEntity.class)) {
                throw new ClassNotEntityException(cl.getName());
            }
            boolean id = Arrays.stream(cl.getDeclaredFields()).anyMatch(i -> i.isAnnotationPresent(MyID.class));
            if (!id) {
                throw new ClassHasNoIdException(cl.getName());
            }
            columns = getColumns(cl);
            if (columns.size() == 0) {
                throw new ClassHasNoColumnsException(cl.getName());
            }
        }
    }
    static void createTable(Connection connection,String table, String table_name,Field id) throws ClassNotFoundException, SQLException {
        Class table_class = Class.forName(table);
        List<Field> columns = getColumns(table_class);
        StringBuilder query;
         query = new StringBuilder("CREATE TABLE " + table_name + "("
                 + id.getName());
         if(id.getType()==int.class || id.getType()==Integer.class){
             query.append(" INT NOT NULL PRIMARY KEY ");
         }
         else if(id.getType()==String.class){
             query.append(" VARCHAR(255) NOT NULL PRIMARY KEY ");
         }
        for (Field column:columns) {
            query.append(",").append(column.getName());
            if(column.getType()==int.class || column.getType()==Integer.class){
                query.append(" INT ");
            }
            else if(column.getType()==String.class){
                query.append(" VARCHAR(255) ");
            }
        }
        query.append(")");
        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());
    }

    static void updateTable(Connection connection,String table_name,String table) throws SQLException, ClassNotFoundException {
        List<Field> columns = getColumns(Class.forName(table));
        DatabaseMetaData md = connection.getMetaData();
        for (Field f:columns) {
            ResultSet rs = md.getColumns(null, null, table_name, f.getName());
            if (!rs.next()) {
                Statement st = connection.createStatement();
                String sql = "ALTER TABLE "+table_name+" add "+f.getName();
                if(f.getType()==int.class || f.getType()==Integer.class){
                    sql+=" INT ";
                }
                else if(f.getType()==String.class){
                    sql+=" VARCHAR(255) ";
                }
                st.executeUpdate(sql);
            }
        }

    }

    static List<Field> getColumns(Class cl){
        return Arrays.stream(cl.getDeclaredFields()).filter(i -> i.isAnnotationPresent(MyColumn.class)).collect(Collectors.toList());
    }
    static Field getID(String table_model_name) throws ClassNotFoundException {
        return Arrays.stream(Class.forName(table_model_name).getDeclaredFields()).filter(i -> i.isAnnotationPresent(MyID.class)).findFirst().orElse(null);
    }
}
