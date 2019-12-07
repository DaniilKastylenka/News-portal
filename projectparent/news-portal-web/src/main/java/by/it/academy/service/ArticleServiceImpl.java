package by.it.academy.service;

import by.it.academy.type.Article;
import by.it.academy.type.Author;

import java.util.ArrayList;
import java.util.List;

public class ArticleServiceImpl implements ArticleService {

    private static final ArticleService INSTANCE = new ArticleServiceImpl();

    private final List<Article> articles;

    private Long id;
    private ArticleServiceImpl() {
        id = 0L;
        articles = new ArrayList<>();
    }

    @Override
    public List<Article> getAllArticles() {
        return articles;
    }

    @Override
    public void addNewArticle(Article article) {
        article.setId(++id);
        articles.add(article);
    }

    @Override
    public void deleteArticle(Long id) {
        for (Article a : articles) {
            if (a.getId().equals(id)) {
                articles.remove(a);
            }
        }
    }

    @Override
    public void update(Article article) {
        for (Article a : articles) {
            if (a.getId().equals(article.getId())){
                a.setTitle(article.getTitle());
                a.setText(article.getText());
                a.setAuthor(article.getAuthor());
                a.setPublicationDate(article.getPublicationDate());
                a.setLikes(article.getLikes());
                a.setDislikes(article.getDislikes());
                a.setComments(article.getComments());
            }
        }
    }

    public synchronized static ArticleService getService() {
        return INSTANCE;
    }
}