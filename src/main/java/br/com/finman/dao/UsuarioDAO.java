package br.com.finman.dao;

import br.com.finman.config.DB;
import br.com.finman.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UsuarioDAO {
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection c = DB.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("senha_hash"));
                    return u;
                }
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
        return null;
    }

    public boolean verificarSenha(Usuario u, String senhaPlana) {
        return u != null && BCrypt.checkpw(senhaPlana, u.getSenhaHash());
    }

    public int criar(String nome, String email, String senhaPlana) {
        String sql = "INSERT INTO usuarios (nome, senha_hash) VALUES (?, ?)";
        String hash = BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
        try (Connection c = DB.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, hash);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
        return -1;
    }
}