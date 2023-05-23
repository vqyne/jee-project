package filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/protected/*"})
public class AdminFilter implements Filter {

    private static final String ADMIN_CATEGORY = "admin";
    private static final String SESSION_HANDLER_CATEGORY = "session_handler";
    private static final String BETTER_HANDLER_CATEGORY = "better_handler";

    private static final String UNAUTHORIZED_PAGE = "/unauthorized.html";
    private static final String SESSION_HANDLER_PAGE = "/admin/protected/session.html";
    private static final String BETTER_HANDLER_PAGES = "/admin/protected/discipline.html,/admin/protected/site.html";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("category") != null) {
            String userCategory = session.getAttribute("category").toString();
            String requestURI = request.getRequestURI();

            if (ADMIN_CATEGORY.equals(userCategory)) {
                // L'utilisateur 'admin' a accès à toutes les pages HTML dans /admin/protected/
                filterChain.doFilter(request, response);
            } else if (SESSION_HANDLER_CATEGORY.equals(userCategory) && requestURI.contains(SESSION_HANDLER_PAGE)) {
                // L'utilisateur 'session_handler' a accès à admin/protected/session.html
                filterChain.doFilter(request, response);
            } else if (BETTER_HANDLER_CATEGORY.equals(userCategory) && isAuthorizedPageForBetterHandler(requestURI)) {
                // L'utilisateur 'better_handler' a accès aux pages spécifiques
                filterChain.doFilter(request, response);
            } else {
                // L'utilisateur ne dispose pas des droits d'accès requis, redirection vers la page d'accès non autorisé
                response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
            }
        } else {
            // Aucune session ou catégorie d'utilisateur trouvée, redirection vers la page d'accès non autorisé
            response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
        }
    }

    private boolean isAuthorizedPageForBetterHandler(String requestURI) {
        // Vérifier si la page demandée est autorisée pour l'utilisateur 'better_handler'
        String[] authorizedPages = BETTER_HANDLER_PAGES.split(",");
        for (String page : authorizedPages) {
            if (requestURI.contains(page)) {
                return true;
            }
        }
        return false;
    }
}
