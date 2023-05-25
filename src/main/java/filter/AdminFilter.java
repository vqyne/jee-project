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

    private static final String[] SESSION_HANDLER_BLOCKED_PAGES = {
            "/jee-project/admin/protected/discipline.html",
            "/jee-project/admin/protected/site.html",
            "/jee-project/admin/protected/page_discipline.html",
            "/jee-project/admin/protected/athlete.html"
    };

    private static final String[] BETTER_HANDLER_BLOCKED_PAGES = {
            "/jee-project/admin/protected/session.html",
            "/jee-project/admin/protected/modify_session.html",
            "/jee-project/admin/protected/athlete.html"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("category") != null) {
            String userCategory = session.getAttribute("category").toString();
            String requestURI = request.getRequestURI();
            System.out.println(requestURI);
            System.out.println(userCategory);
            
            if (ADMIN_CATEGORY.equals(userCategory)) {
                filterChain.doFilter(request, response);
            } else if (SESSION_HANDLER_CATEGORY.equals(userCategory)) {
                if (isBlockedPageForUser(SESSION_HANDLER_BLOCKED_PAGES, requestURI)) {
                    response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
                } else {
                    filterChain.doFilter(request, response);
                }
            } else if (BETTER_HANDLER_CATEGORY.equals(userCategory)) {
                if (isBlockedPageForUser(BETTER_HANDLER_BLOCKED_PAGES, requestURI)) {
                    response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
                } else {
                    filterChain.doFilter(request, response);
                }
            } else {
                response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
            }
        } else {
            response.sendRedirect(request.getContextPath() + UNAUTHORIZED_PAGE);
        }
    }

    private boolean isBlockedPageForUser(String[] blockedPages, String requestURI) {
        for (String page : blockedPages) {
            if (requestURI.equals(page)) {
                return true;
            }
        }
        return false;
    }
}
