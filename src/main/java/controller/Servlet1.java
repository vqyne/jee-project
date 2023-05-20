package controller;

import java.io.IOException;

import database.UserDAO;
import model.User;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "servlet1", urlPatterns = { "/servlet1" })
public class Servlet1 extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 1L;

    public Servlet1() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);

        if (user != null && user.checkPassword(password)) {
            // Store the username and category in the session
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getLogin());
            session.setAttribute("category", user.getCategory());

            response.getWriter().println("Login successful");
            response.getWriter().println(user.getCategory());
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
