package com.polmirvid.dao.Producto;

import com.polmirvid.model.Categoria;
import com.polmirvid.model.Producto;
import com.polmirvid.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static final ProductoDaoImpl instance = new ProductoDaoImpl();

    private ProductoDaoImpl() {}

    public static ProductoDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Producto producto) throws SQLException {
        String sql = """
                    INSERT INTO producto (nombre, precio, stock, id_categoria)
                    VALUES (?, ?, ?, ?);
                """;

        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, producto.getNombre());
            pstm.setDouble(2, producto.getPrecio());
            pstm.setInt(3, producto.getStock());
            pstm.setInt(4, producto.getCategoria().getIdCategoria());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public Producto getById(int id) throws SQLException {

        Producto result = null;

        String sql = """
                    SELECT p.id, p.nombre, p.precio, p.stock, c.id AS cid, c.nombre AS cnombre
                    FROM producto p
                    JOIN categoria c ON p.id_categoria = c.id
                    WHERE p.id = ?;
                """;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria(rs.getInt("cid"), rs.getString("cnombre"));
                    result = new Producto();
                    result.setIdProducto(rs.getInt("id"));
                    result.setNombre(rs.getString("nombre"));
                    result.setPrecio(rs.getDouble("precio"));
                    result.setStock(rs.getInt("stock"));
                    result.setCategoria(categoria);
                }
            }
        }

        return result;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        String sql = """
                    SELECT p.id, p.nombre, p.precio, p.stock, c.id AS cid, c.nombre AS cnombre
                    FROM producto p
                    JOIN categoria c ON p.id_categoria = c.id;
                """;

        List<Producto> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            Producto prod;

            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getInt("cid"), rs.getString("cnombre"));
                prod = new Producto();
                prod.setIdProducto(rs.getInt("id"));
                prod.setNombre(rs.getString("nombre"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setStock(rs.getInt("stock"));
                prod.setCategoria(categoria);
                result.add(prod);
            }
        }

        return result;
    }

    @Override
    public int update(Producto producto) throws SQLException {
        String sql = """
                    UPDATE producto
                    SET nombre = ?, precio = ?, stock = ?, id_categoria = ?
                    WHERE id = ?;
                """;

        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, producto.getNombre());
            pstm.setDouble(2, producto.getPrecio());
            pstm.setInt(3, producto.getStock());
            pstm.setInt(4, producto.getCategoria().getIdCategoria());
            pstm.setInt(5, producto.getIdProducto());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        }
    }
}
