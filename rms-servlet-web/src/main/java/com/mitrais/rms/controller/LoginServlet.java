package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;
import com.mitrais.rms.service.LoginService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends AbstractController
{
    private String path;
    private LoginService loginService = LoginService.getInstance();
    private int timeout = 30;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        setPath(getTemplatePath(req.getServletPath()));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("buttonLogin", req.getParameter("buttonLogin"));
        requestParams.put("username", req.getParameter("username"));
        requestParams.put("userpass", req.getParameter("userpass"));
        if(loginService.doCheck(requestParams)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("username", req.getParameter("username"));
            session.setMaxInactiveInterval(timeout); // 30 seconds
            setPath("users/list");
            resp.sendRedirect(getPath());
        } else {
            // Make a form alert
            PrintWriter out = resp.getWriter();
            out.print("<script>alert('Username or Password is wrong')</script>");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(getTemplatePath(req.getServletPath()));
            requestDispatcher.include(req, resp);
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
