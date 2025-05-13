package com.polmirvid.dao.Categoria;

import com.polmirvid.model.Categoria;
import com.polmirvid.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private static final CategoriaDaoImpl instance = new CategoriaDaoImpl();

    private CategoriaDaoImpl() {}

    public static CategoriaDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Categoria categoria) throws SQLException {
        String sql = """
                INSERT INTO categoria (nombre)
                VALUES (?);
            """;

        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, categoria.getNombre());

            result = pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    categoria.setIdCategoria(idGenerado); // asigna el id generado a la instancia
                }
            }
        }

        return result;
    }

    @Override
    public Categoria getById(int id) throws SQLException {
        Categoria result = null;

        String sql = """
                    SELECT * FROM categoria WHERE id = ?;
                """;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    result = new Categoria();
                    result.setIdCategoria(rs.getInt("id"));
                    result.setNombre(rs.getString("nombre"));
                }
            }
        }

        return result;
    }

    @Override
    public List<Categoria> getAll() throws SQLException {
        String sql = """
                    SELECT * FROM categoria;
                """;

        List<Categoria> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                result.add(categoria);
            }
        }

        return result;
    }

    @Override
    public int update(Categoria categoria) throws SQLException {
        String sql = """
                    UPDATE categoria
                    SET nombre = ?
                    WHERE id = ?;
                """;

        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, categoria.getNombre());
            pstm.setInt(2, categoria.getIdCategoria());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }
}
