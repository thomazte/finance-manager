package br.com.finman.web.servlet;

import br.com.finman.dao.TransacaoDAO;
import jakarta.json.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.YearMonth;

@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {
    private final TransacaoDAO dao = new TransacaoDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int uid = (int) req.getSession().getAttribute("uid");
        int ano = Integer.parseInt(req.getParameter("ano"));
        int mes = Integer.parseInt(req.getParameter("mes"));
        var ym = YearMonth.of(ano, mes);

        double saldo = dao.saldoDoMes(uid, ym);
        var porCat = dao.totalPorCategoria(uid, ym);

        var obj = Json.createObjectBuilder().add("saldo", saldo);
        var arr = Json.createArrayBuilder();
        porCat.forEach((k, v) -> arr.add(Json.createObjectBuilder().add("categoria", k).add("total", v)));
        obj.add("porCategoria", arr);

        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(obj.build().toString());
    }
}
