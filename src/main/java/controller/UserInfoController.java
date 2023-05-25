package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;
import model.CategorieUser;

/**
 * Servlet permettant l'accès aux informations du User lorsqu'il est connecté
 */
@WebServlet(name = "UserInfoController", urlPatterns = { "/admin/getUserInfo" })
public class UserInfoController extends HttpServlet {
	
	/**
	 * Méthode permettant de retrouver certaines informations : nom de l'utilisateur, son rôle et le fait qu'il soit connecté ou non
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Retrieve user information from the session
	    HttpSession session = request.getSession(false);
	    
	    String username = null;
	    CategorieUser category = null;

	    boolean isLoggedIn = session != null && session.getAttribute("loggedIn") != null;

	    if(isLoggedIn){
	        username = (String) session.getAttribute("username");
	        category = (CategorieUser) session.getAttribute("category");
	    }

	    // Create a User object with user information
	    User user = new User();
	    user.setLogin(username);
	    user.setCategory(category);
	    user.setLoggedIn(isLoggedIn);

	    // Set the response content type to JSON
	    response.setContentType("application/json");

	    // Use Gson to serialize User object to JSON
	    Gson gson = new Gson();
	    String json = gson.toJson(user);

	    // Write the JSON to the response
	    response.getWriter().write(json);
	}

}
