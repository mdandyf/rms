package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{

    private static UserDao userDao = UserDaoImpl.getInstance();
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
        String action, pathInfo = "";
        if(req.getPathInfo().contains("users")) {
            pathInfo = req.getPathInfo().replace("/users/", "");
        } else {
            pathInfo = req.getPathInfo();
        }

        action = req.getServletPath() + pathInfo;

        try {
            if("/users/new".equalsIgnoreCase(action)) {
                showNewForm(req, resp);
            } else if("/users/delete".equalsIgnoreCase(action)) {
                delete(req, resp);
            } else if("/users/edit".equalsIgnoreCase(action)) {
                showEditForm(req, resp) ;
            } else if("/users/data".equalsIgnoreCase(action)) {
                String id = req.getParameter("userid");
                if(id != null && id != "") {
                    update(req, resp);
                } else {
                    insert(req, resp);
                }
            } else {
                list(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        List<User> users = userDao.findAll();
        req.setAttribute("users", users);

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
        String id = req.getParameter("userid");
        if(id != null && id != "") {
            Optional<User> user = userDao.find(Long.decode(id));
            if(user.isPresent()) {
                setPath(getTemplatePath("/users/form"));
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(getPath());
                req.setAttribute("user", user.get());
                requestDispatcher.forward(req, resp);
            } else {
                throw new SQLException();
            }
        } else {
            throw new ServletException();
        }
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
            String userName = req.getParameter("username");
            String userPass = req.getParameter("userpass");

            List<User> listUser = userDao.findAll().stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
            Long newId = listUser.get(0).getId() + 1;

            User user = new User(newId, userName, userPass);
            userDao.save(user);
            resp.sendRedirect("/users/list");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String id = req.getParameter("userid");
        if(id != null && id != "") {
            String userName = req.getParameter("username");
            String userPass = req.getParameter("userpass");
            Optional<User> user = userDao.find(Long.decode(id));
            if(user.isPresent()) {
                userDao.update(new User(Long.decode(id), userName, userPass));
                resp.sendRedirect("/users/list");
            } else {
                throw new SQLException();
            }

        } else {
            throw new ServletException();
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String id = req.getParameter("userid");
        if(id != null && id != "") {
            Optional<User> user = userDao.find(Long.decode(id));
            if(user.isPresent()) {
                userDao.delete(user.get());
                resp.sendRedirect("/users/list");
            } else {
                throw new SQLException();
            }
        } else {
            throw new ServletException();
        }
    }

    private String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }




}
