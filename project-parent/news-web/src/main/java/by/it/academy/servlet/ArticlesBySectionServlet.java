package by.it.academy.servlet;

import by.it.academy.project.model.Article;
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
import java.util.List;

@WebServlet(urlPatterns = "/articleListBySection")
public class ArticlesBySectionServlet extends HttpServlet {

    private ArticleService articleService = ArticleServiceImpl.getINSTANCE();
    private SectionService sectionService = SectionServiceImpl.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int sectionId = Integer.valueOf(req.getParameter("sectionId"));

        int page = Integer.valueOf(req.getParameter("page"));
        int total = 3;
        if (page > 1) {
            page = page - 1;
            page = page * total + 1;
        }

        List<Article> articleList = articleService.getLimitedNumberOfArticlesBySectionId(page, total, sectionId);
        req.setAttribute("articleList", articleList);

        int totalPages = articleService.getCountOfPagesWithArticlesBySectionId(total, sectionId);
        int[] pages = new int[totalPages];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i + 1;
        }
        req.setAttribute("pages", pages);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("sections", sectionService.getSections());
        req.getRequestDispatcher("/WEB-INF/jsp/articleList.jsp").forward(req, resp);

    }
}
