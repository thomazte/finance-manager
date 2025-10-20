package br.com.finman.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/*")
public class AuthFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.endsWith("/api/auth/login") || path.endsWith("/api/auth/logout")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("uid") == null) {
           resp.setStatus(401);
           resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"ok\":false,\"erro\":\"nao_autenticado\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
