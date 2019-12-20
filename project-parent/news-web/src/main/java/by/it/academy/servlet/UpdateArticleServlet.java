package by.it.academy.servlet;

import by.it.academy.project.model.Article;
import by.it.academy.project.model.Comment;
import by.it.academy.project.model.Section;
import by.it.academy.project.model.User;
import by.it.academy.project.service.ArticleService;
import by.it.academy.project.service.ArticleServiceImpl;
import by.it.academy.project.service.SectionService;
import by.it.academy.project.service.SectionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/updateArticle")

public class UpdateArticleServlet extends HttpServlet {

    private ArticleService articleService = ArticleServiceImpl.getINSTANCE();
    private SectionService sectionService = SectionServiceImpl.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("sections", sectionService.getSections());

        req.getRequestDispatcher("/WEB-INF/jsp/updateArticle.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = 0L;

        String sectionId = req.getParameter("sectionId");
        String text = req.getParameter("text");
        String title = req.getParameter("title");

        User user = (User) req.getSession().getAttribute("user");

        Section section = sectionService.getSections().stream()
                .filter(section1 -> section1.getId().equals(Long.valueOf(sectionId)))
                .findFirst().get();

        Article oldArticle = articleService.findArticleById(id);

        Article newArticle = new Article(id, section, title, text, user,
                oldArticle.getLikes(), oldArticle.getDislikes(),
                (ArrayList<Comment>) oldArticle.getComments());

        articleService.update(newArticle);

        resp.sendRedirect("/myArticles");

    }
}