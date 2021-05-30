package Core;

import Annotations.MyEntity;
import Exceptions.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    public Connection connection;

    public Manager() throws SQLException, IOException, ORMExceptions, ClassNotFoundException {
        this.connection = ORM.connection();
        ORM.tables(this.connection);
    }

    public <T> void create(T object) throws ClassNotFoundException, PrimaryKeyExistException {
        String table_name = object.getClass().getAnnotation(MyEntity.class).value();
        List<Field> columns = ORM.getColumns(object.getClass());
        Field id = ORM.getID(object.getClass().getName());
        id.setAccessible(true);
        StringBuilder sql = new StringBuilder("INSERT INTO " + table_name + "(" + id.getName());
        for (Field column : columns) {
            sql.append(", ").append(column.getName());
        }
        sql.append(")");
        Object o_id = null;
        try {
            o_id = id.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (o_id instanceof String) {
            sql.append(" VALUES(" + "'").append((String) o_id).append("'");
        } else if (o_id instanceof Integer) {
            sql.append((" VALUES(")).append((int) o_id);

        }
        for (Field column : columns) {
            column.setAccessible(true);
            Object f = null;
            try {
                f = column.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (f instanceof String) {
                sql.append(", " + "'").append((String) f).append("'");

            } else if (f instanceof Integer) {
                sql.append(", ").append((int) f);
            }
        }
        sql.append(")");
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(sql.toString());
        } catch (SQLException throwable) {
            throw new PrimaryKeyExistException();
        }
    }

    public <T> void delete(T object) throws ClassNotFoundException, SQLException {
        String table_name = object.getClass().getAnnotation(MyEntity.class).value();
        Field id = ORM.getID(object.getClass().getName());
        id.setAccessible(true);
        Object o_id = null;
        try {
            o_id = id.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        StringBuilder sql = new StringBuilder("DELETE FROM " + table_name + " WHERE " + id.getName() + "=");
        if (o_id instanceof String) {
            sql.append("'").append((String) o_id).append("'");
        } else if (o_id instanceof Integer) {
            sql.append((int) o_id);
        }
        Statement st = connection.createStatement();
        st.executeUpdate(sql.toString());
    }

    public <T> void update(T object) throws ClassNotFoundException, SQLException {
        String table_name = object.getClass().getAnnotation(MyEntity.class).value();
        Field id = ORM.getID(object.getClass().getName());
        List<Field> columns = ORM.getColumns(object.getClass());
        id.setAccessible(true);

        StringBuilder sql = new StringBuilder("UPDATE " + table_name + " SET ");
        boolean first = true;
        for (Field column : columns) {
            column.setAccessible(true);
            Object f = null;
            try {
                f = column.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!first) {
                sql.append(",");
            } else {
                first = false;
            }
            if (f instanceof String) {
                sql.append(column.getName()).append("='").append((String) f).append("'");

            } else if (f instanceof Integer) {
                sql.append(column.getName()).append("=").append((int) f);
            }

        }


        sql.append(" WHERE ").append(id.getName()).append("=");
        Object o_id = null;
        try {
            o_id = id.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (o_id instanceof String) {
            sql.append("'").append((String) o_id).append("'");
        } else if (o_id instanceof Integer) {
            sql.append((int) o_id);
        }
        Statement st = connection.createStatement();
        st.executeUpdate(sql.toString());
    }

    public <T,Y> Y findById(Class<Y> table_class, T id) throws ClassNotFoundException, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String table_name = table_class.getAnnotation(MyEntity.class).value();
        Field id_name = ORM.getID(table_class.getName());
        id_name.setAccessible(true);
        String sql = "SELECT * FROM " + table_name + " WHERE " + id_name.getName() + "=";
        if (id instanceof String) {
            sql += "'" + (String) id + "'";
        } else if (id instanceof Integer) {
            sql += (int) id;
        }

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return toObj(table_class,rs).get(0);
    }

    public <T,Y> List<Y> getAll(Class<Y> table_class) throws ClassNotFoundException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String table_name = table_class.getAnnotation(MyEntity.class).value();
        Field id_name = ORM.getID(table_class.getName());
        id_name.setAccessible(true);
        String sql = "SELECT * FROM " + table_name;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return toObj(table_class,rs);
    }
    private <Y> List<Y> toObj(Class<Y> table_class,ResultSet rs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        List<Y> list= new ArrayList<>();
        while (rs.next()) {
            Constructor<Y> constructor = table_class.getConstructor();
            constructor.setAccessible(true);
            Y obj=constructor.newInstance();
            for (Field f : table_class.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType().isAssignableFrom(String.class)) {
                    f.set(obj, rs.getString(f.getName()));
                } else if (f.getType().isAssignableFrom(int.class)|| f.getType().isAssignableFrom(Integer.class)) {
                    f.set(obj,rs.getInt(f.getName()));
                }
            }
            list.add(obj);
        }
        return list;
    }

}
