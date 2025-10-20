package br.com.finman.web.servlet;

import br.com.finman.dao.CategoriaDAO;
import br.com.finman.model.Categoria;
import jakarta.json.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/categorias")
public class CategoriaServlet extends HttpServlet {
    private final CategoriaDAO dao = new CategoriaDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int uid = (int) req.getSession().getAttribute("uid");
        var list = dao.listarPorUsuario(uid);

        var arr = Json.createArrayBuilder();
        list.forEach(c -> arr.add(Json.createObjectBuilder()
                .add("id", c.getId())
                .add("nome", c.getNome())
                .add("tipo", c.getTipo())
        ));
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(arr.build().toString());
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int uid = (int) req.getSession().getAttribute("uid");
        var json = Json.createReader(req.getInputStream()).readObject();
        var c = new Categoria();
        c.setNome(json.getString("nome",""));
        c.setTipo(json.getString("tipo","DESPESA"));
        c.setUsuarioId(uid);
        int id = dao.criarCategoria(c);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write("{\"ok\":true,\"id\":"+id+"}");
    }
}
