package br.com.finman.dao;

import br.com.finman.config.DB;
import br.com.finman.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public List<Categoria> listarPorUsuario(int usuarioId) {
        String sql = "select * from categoria where usuario_id = ? ORDER BY tipo, nome";
        List<Categoria> out = new ArrayList<>();
        try (var c = DB.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var cat = new Categoria(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getInt("usuario_id")
                    );
                    out.add(cat);
                }
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
        return out;
    }

    public int criarCategoria(Categoria cat) {
        String sql = "INSERT INTO categoria (nome, tipo, usuario_id) VALUES (?, ?, ?)";
        try (var c = DB.getConnection();
             var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cat.getNome());
            ps.setString(2, cat.getTipo());
            ps.setInt(3, cat.getUsuarioId());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
        return -1;
    }
}














