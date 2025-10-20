package br.com.finman.dao;

import br.com.finman.config.DB;
import br.com.finman.model.Transacao;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * Acesso a dados de Transacao usando JDBC.
 */
public class TransacaoDAO {

    public long criar(Transacao t) {
        String sql = """
            INSERT INTO transacao (usuario_id, categoria_id, tipo, valor, data, descricao)
            VALUES (?,?,?,?,?,?)
            """;
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, t.getUsuarioId());
            ps.setInt(2, t.getCategoriaId());
            ps.setString(3, t.getTipo());
            ps.setDouble(4, t.getValor());
            ps.setDate(5, Date.valueOf(t.getData()));
            ps.setString(6, t.getDescricao());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir transacao", e);
        }
        return -1;
    }

    public List<Transacao> listarPorMes(int usuarioId, YearMonth ym) {
        String sql = """
            SELECT t.*, c.nome AS categoria_nome
            FROM transacao t
            JOIN categoria c ON c.id = t.categoria_id
            WHERE t.usuario_id = ? AND t.data BETWEEN ? AND ?
            ORDER BY t.data DESC, t.id DESC
            """;
        List<Transacao> out = new ArrayList<>();
        LocalDate ini = ym.atDay(1), fim = ym.atEndOfMonth();

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(ini));
            ps.setDate(3, Date.valueOf(fim));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transacao t = new Transacao();
                    t.setId(rs.getLong("id"));
                    t.setUsuarioId(usuarioId);
                    t.setCategoriaId(rs.getInt("categoria_id"));
                    t.setTipo(rs.getString("tipo"));
                    t.setValor(rs.getDouble("valor"));
                    t.setData(rs.getDate("data").toLocalDate());
                    t.setDescricao(rs.getString("descricao"));
                    t.setCategoriaNome(rs.getString("categoria_nome"));
                    out.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar transacoes por mes", e);
        }
        return out;
    }

    public boolean deletar(int usuarioId, long id) {
        String sql = "DELETE FROM transacao WHERE id = ? AND usuario_id = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setInt(2, usuarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar transacao", e);
        }
    }

    public double saldoDoMes(int usuarioId, YearMonth ym) {
        String sql = """
            SELECT COALESCE(SUM(CASE WHEN tipo='RECEITA' THEN valor ELSE -valor END), 0)
            FROM transacao
            WHERE usuario_id = ? AND data BETWEEN ? AND ?
            """;
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(ym.atDay(1)));
            ps.setDate(3, Date.valueOf(ym.atEndOfMonth()));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular saldo do mes", e);
        }
    }

    public Map<String, Double> totalPorCategoria(int usuarioId, YearMonth ym) {
        String sql = """
            SELECT c.nome,
                   COALESCE(SUM(CASE WHEN t.tipo='RECEITA' THEN t.valor ELSE -t.valor END), 0) AS total
            FROM transacao t
            JOIN categoria c ON c.id = t.categoria_id
            WHERE t.usuario_id = ? AND t.data BETWEEN ? AND ?
            GROUP BY c.nome
            ORDER BY total ASC
            """;
        Map<String, Double> map = new LinkedHashMap<>();
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(ym.atDay(1)));
            ps.setDate(3, Date.valueOf(ym.atEndOfMonth()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString(1), rs.getDouble(2));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao totalizar por categoria", e);
        }
        return map;
    }
}