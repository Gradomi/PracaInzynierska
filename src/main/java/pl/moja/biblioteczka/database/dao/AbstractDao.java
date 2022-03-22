package pl.moja.biblioteczka.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface AbstractDao<T> {

    static void printSQLException(SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    List<T> get() throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getByIds(List<Integer> ids) throws SQLException;

    int save(T t) throws SQLException;

    List<Integer> save(List<T> listT) throws SQLException;

    T delete(T t) throws SQLException;

    List<T> delete(List<T> listT) throws  SQLException;

    T update(T t) throws SQLException;

    List<T> update(List<T> listT) throws SQLException;

}





