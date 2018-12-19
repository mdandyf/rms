package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends AbstractController
{
    private String path;
    private static UserDao userDao = UserDaoImpl.getInstance();

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
        PrintWriter out = resp.getWriter();
        if(doOperation(req)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("username", req.getParameter("username"));
            session.setMaxInactiveInterval(30); // 30 seconds
            setPath("users/list");
            resp.sendRedirect(getPath());
        } else {
            out.println("<font color=red>Either user name or password is wrong.</font>");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
            requestDispatcher.forward(req, resp);
        }


    }

    private boolean doOperation(HttpServletRequest req) {
        boolean loginState = false;
        if(req.getParameter("buttonLogin") != null) {
            String userName = req.getParameter("username");
            String userPass = req.getParameter("userpass");
            if((userName != null) && (userPass != null)) {
                Optional<User> user = userDao.findByUserName(userName);
                if((user.isPresent()) && (user.get().getPassword().equals(userPass))) {
                   loginState = true;
                }
            }
        }

       return loginState;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
