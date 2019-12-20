package by.it.academy.project.service;

import by.it.academy.project.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleServiceImpl implements ArticleService {

    private static final ArticleService INSTANCE = new ArticleServiceImpl();

    private final List<Article> articles;

    private AtomicLong id = new AtomicLong();

    private ArticleServiceImpl() {
        articles = new ArrayList<>();
    }

    @Override
    public List<Article> getAllArticles() {
        return new ArrayList<>(articles);
    }

    @Override
    public void addNewArticle(Article article) {
        article.setId(id.incrementAndGet());
        articles.add(article);
    }

    @Override
    public void deleteArticle(Long id) {
        for (Article a : articles) {
            if (a.getId().equals(id)) {
                articles.remove(a);
                break;
            }
        }
    }

    @Override
    public void update(Article article) {
        for (Article a : articles) {
            if (a.getId().equals(article.getId())) {
                a.setSection(article.getSection());
                a.setTitle(article.getTitle());
                a.setText(article.getText());
                a.setAuthor_id(article.getAuthor_id());
                a.setDate(article.getDate());
                a.setLikes(article.getLikes());
                a.setDislikes(article.getDislikes());
                a.setComments(article.getComments());
                break;
            }
        }
    }

    @Override
    public Article findArticleById(Long id) {
        for (Article a : articles) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    public static ArticleService getINSTANCE() {
        return INSTANCE;
    }
}