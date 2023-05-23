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

@WebServlet(name = "UserController", urlPatterns = { "/admin" })
public class UserController extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 1L;

    public UserController() {
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

        // Set a flag to indicate successful login
        session.setAttribute("loggedIn", true);

        // Redirect to the desired page after successful login
        response.sendRedirect(request.getContextPath() + "/admin/index.html");
    } else {
        // Invalid username or password, redirect to login page with an error message
        response.sendRedirect(request.getContextPath() + "/login.html?error=1");
    }
}



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
