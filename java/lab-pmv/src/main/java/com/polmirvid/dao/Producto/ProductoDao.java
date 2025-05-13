package com.polmirvid.dao.Producto;

import com.polmirvid.model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDao {
    public int add(Producto producto) throws SQLException;

    public Producto getById(int id) throws SQLException;

    public List<Producto> getAll() throws SQLException;

    public int update(Producto producto) throws SQLException;

    public int delete(int id) throws SQLException;
}
