package com.mitrais.rms.controller;

import com.mitrais.rms.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{
    private UserService userService = UserService.getInstance();
    private String path;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doOperation(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void doOperation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        try {
            // check if there is still a session for current user
            Optional.of((String) session.getAttribute("username"));
        } catch (NullPointerException e) {
            // session has been expired, redirect to login page
            session.invalidate();
            resp.sendRedirect("/login");
            return;
        }

        String action, pathInfo = "";
        if(req.getPathInfo().contains("users")) {
            pathInfo = req.getPathInfo().replace("/users/", "");
        } else {
            pathInfo = req.getPathInfo();
        }

        action = req.getServletPath() + pathInfo;

        try {

            String id = req.getParameter("userid");

            if("/users/new".equalsIgnoreCase(action)) {
                showNewForm(req, resp);
            } else if("/users/delete".equalsIgnoreCase(action)) {
                if(!id.equals(null) && !id.equals("")) {
                    delete(req, resp);
                } else {
                    throw new ServletException();
                }
            } else if("/users/edit".equalsIgnoreCase(action)) {
                if(!id.equals(null) && !id.equals("")) {
                    showEditForm(req, resp) ;
                } else {
                    throw new ServletException();
                }
            } else if("/users/data".equalsIgnoreCase(action)) {
                if(!id.equals(null) && !id.equals("")) {
                    update(req, resp);
                } else {
                    insert(req, resp);
                }
            } else {
                list(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            resp.sendRedirect("src/main/webapp/WEB-INF/html/500 error.html");
        }

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        req.setAttribute("users", userService.findAll());
        setPath(getTemplatePath("/users/list"));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
        requestDispatcher.forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        req.setAttribute("user", null);
        setPath(getTemplatePath("/users/form"));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
        requestDispatcher.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put("userid", req.getParameter("userid"));

        if(userService.doCheck(map)) {
            setPath(getTemplatePath("/users/form"));
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
            req.setAttribute("user", userService.find(Long.decode(req.getParameter("userid"))).get());
            requestDispatcher.forward(req, resp);
        } else {
            throw new SQLException();
        }
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put("username", req.getParameter("username"));
        map.put("userpass", req.getParameter("userpass"));
        if(userService.doInsert(map)) {
            resp.sendRedirect("/users/list");
        } else {
            throw new SQLException();
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put("userid", req.getParameter("userid"));
        map.put("username", req.getParameter("username"));
        map.put("userpass", req.getParameter("userpass"));
        if(userService.doUpdate(map)) {
            resp.sendRedirect("/users/list");
        } else {
            throw new SQLException();
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        if(userService.doDelete(Long.decode(req.getParameter("userid")))) {
            resp.sendRedirect("/users/list");
        } else {
            throw new SQLException();
        }
    }

    private String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }




}
