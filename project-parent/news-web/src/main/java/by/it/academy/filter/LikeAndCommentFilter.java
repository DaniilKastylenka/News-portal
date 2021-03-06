package by.it.academy.filter;

import by.it.academy.project.model.User;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/likeArticle", "/dislikeArticle", "/likeComment", "/dislikeComment", "/writeComment", "/userPage"}, dispatcherTypes = DispatcherType.REQUEST)

public class LikeAndCommentFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (req.getServletPath().contains("/writeComment")) {
                res.sendRedirect("javascript:PopUpHide()");
            } else if (req.getServletPath().contains("/userPage")) {
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                res.sendRedirect(req.getHeader("Referer"));
            }
        } else {
            super.doFilter(req, res, chain);
        }
    }
}
