package controller;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet permettant de déconnecter l'utilisateur
 */
@WebServlet(name = "DisconnectServlet", urlPatterns = { "/admin/disconnect" })
public class DisconnectServlet extends HttpServlet {

	/**
	 * Méthode appelée pour déconnecter l'utilisateur
	 * Redirige vers la page de connexion
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Invalidate the session to disconnect the user
            session.invalidate();
        }

        // Redirect to the desired page after disconnection
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
