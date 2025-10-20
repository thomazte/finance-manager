package br.com.finman.web.servlet;

import br.com.finman.dao.TransacaoDAO;
import br.com.finman.model.Transacao;
import jakarta.json.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Endpoints REST para transações.
 * GET  /api/transacoes?ano=YYYY&mes=MM  -> lista do mês
 * POST /api/transacoes                   -> cria
 * DELETE /api/transacoes/{id}            -> remove
 */
@WebServlet("/api/transacoes/*")
public class TransacaoServlet extends HttpServlet {

    private final TransacaoDAO dao = new TransacaoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");

        int uid = (int) req.getSession().getAttribute("uid");
        int ano = Integer.parseInt(req.getParameter("ano"));
        int mes = Integer.parseInt(req.getParameter("mes"));

        List<Transacao> list = dao.listarPorMes(uid, YearMonth.of(ano, mes));

        var arr = Json.createArrayBuilder();
        list.forEach(t -> arr.add(
                Json.createObjectBuilder()
                        .add("id", t.getId())
                        .add("tipo", t.getTipo())
                        .add("valor", t.getValor())
                        .add("data", t.getData().toString())
                        .add("descricao", t.getDescricao() == null ? "" : t.getDescricao())
                        .add("categoriaId", t.getCategoriaId())
                        .add("categoriaNome", t.getCategoriaNome() == null ? "" : t.getCategoriaNome())
        ));
        resp.getWriter().write(arr.build().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");

        int uid = (int) req.getSession().getAttribute("uid");
        var json = Json.createReader(req.getInputStream()).readObject();

        Transacao t = new Transacao();
        t.setUsuarioId(uid);
        t.setCategoriaId(json.getInt("categoriaId"));
        t.setTipo(json.getString("tipo"));
        t.setValor(json.getJsonNumber("valor").doubleValue());
        t.setData(LocalDate.parse(json.getString("data")));
        t.setDescricao(json.getString("descricao", null));

        long id = dao.criar(t);
        resp.getWriter().write("{\"ok\":true,\"id\":" + id + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");

        int uid = (int) req.getSession().getAttribute("uid");
        String path = req.getPathInfo();  // "/{id}"
        long id = Long.parseLong(path.substring(1));

        boolean ok = dao.deletar(uid, id);
        resp.getWriter().write("{\"ok\":" + ok + "}");
    }
}
