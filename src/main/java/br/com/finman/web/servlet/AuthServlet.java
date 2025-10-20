package br.com.finman.web.servlet;

import br.com.finman.dao.UsuarioDAO;
import br.com.finman.model.Usuario;
import jakarta.json.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");

        String path = req.getPathInfo(); // "/login" ou "/logout"
        if (path == null) {
            resp.setStatus(404);
            resp.getWriter().write("{\"ok\":false,\"erro\":\"rota_invalida\"}");
            return;
        }

        // POST /api/auth/login
        if ("/login".equals(path)) {
            var json = Json.createReader(req.getInputStream()).readObject();
            String email = json.getString("email", "");
            String senha = json.getString("senha", "");

            Usuario u = usuarioDAO.buscarPorEmail(email);
            if (usuarioDAO.verificarSenha(u, senha)) {
                HttpSession s = req.getSession(true);
                s.setAttribute("uid", u.getId());
                var out = Json.createObjectBuilder()
                        .add("ok", true)
                        .add("nome", u.getNome())
                        .build();
                resp.getWriter().write(out.toString());
            } else {
                resp.setStatus(401);
                resp.getWriter().write("{\"ok\":false,\"erro\":\"credenciais_invalidas\"}");
            }
            return;
        }

        // POST /api/auth/logout
        if ("/logout".equals(path)) {
            HttpSession s = req.getSession(false);
            if (s != null) s.invalidate();
            resp.getWriter().write("{\"ok\":true}");
            return;
        }

        resp.setStatus(404);
        resp.getWriter().write("{\"ok\":false,\"erro\":\"rota_inexistente\"}");
    }
}