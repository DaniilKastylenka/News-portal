package by.it.academy.project.dao;

import by.it.academy.project.model.Article;
import by.it.academy.project.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class ArticleDaoImpl implements ArticleDao {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private static final ArticleDaoImpl INSTANCE = new ArticleDaoImpl();

    @Override
    public Long create(Article article) {
        Session session = sessionFactory.openSession();
        Long result = null;
        try {
            session.getTransaction().begin();
            result = (Long) session.save(article);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while creating article", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<Article> read(Long id) {
        Session session = sessionFactory.openSession();
        Optional<Article> result = Optional.empty();
        try (session) {
            result = Optional.ofNullable(session.get(Article.class, id));
        } catch (HibernateException e) {
            log.error("error while reading", e);
        }
        return result;
    }

    @Override
    public int update(Article article) {
        Optional<Article> optionalArticle = read(article.getId());
        Session session = sessionFactory.openSession();
        int result = 0;
        try {
            session.getTransaction().begin();
            if (optionalArticle.isPresent()) {
                session.update(article);
                session.getTransaction().commit();
                result = 1;
            }
        } catch (HibernateException e) {
            log.error("error while updating", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public int delete(Long id) {
        Optional<Article> optionalArticle = read(id);
        Session session = sessionFactory.openSession();
        int result = 0;
        try {
            session.getTransaction().begin();
            if (optionalArticle.isPresent()) {
                session.delete(new Article(id));
                session.getTransaction().commit();
                result = 1;
            }
        } catch (HibernateException e) {
            log.error("error while deleting", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Article> getAll() {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article ORDER BY publicationDate DESC", Article.class);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting all", e);
        }
        return result;
    }

    @Override
    public List<Article> getArticlesBySearchRequest(String request) throws SQLException {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article WHERE title LIKE :request ORDER BY publicationDate DESC", Article.class);
            query.setParameter("request", "%" + request + "%");
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting articles by search request", e);
        }
        return result;
    }

    @Override
    public List<Article> getArticlesBySearchRequestAndUserId(String request, Long id) {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article WHERE title LIKE :request AND author.id=:id", Article.class);
            query.setParameter("request", "%" + request + "%");
            query.setParameter("id", id);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting articles by search request and user id");
        }
        return result;
    }

    @Override
    public List<Article> getLimitedNumberOfArticles(int start, int total) throws SQLException {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article ORDER BY publicationDate DESC", Article.class);
            query.setFirstResult(start - 1);
            query.setMaxResults(total);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting limit articles", e);
        }
        return result;
    }

    @Override
    public int getCountOfArticles() throws SQLException {
        Session session = sessionFactory.openSession();
        int result = 0;
        try (session) {
            Query query = session.createQuery("SELECT count(a.id) FROM Article a");
            result = Integer.valueOf(query.getSingleResult().toString());
        } catch (HibernateException e) {
            log.error("error while getting count of articles", e);
        }
        return result;
    }

    @Override
    public List<Article> getLimitedNumberOfArticlesBySectionId(int start, int total, int sectionId) throws SQLException {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article WHERE section.id =: sectionId ORDER BY publicationDate DESC", Article.class);
            query.setParameter("sectionId", sectionId);
            query.setFirstResult(start - 1);
            query.setMaxResults(total);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting articles by section id", e);
        }
        return result;
    }

    @Override
    public int getCountOfArticlesBySectionId(int sectionId) throws SQLException {
        Session session = sessionFactory.openSession();
        int result = 0;
        try (session) {
            Query query = session.createQuery("SELECT count(a.id) FROM Article a WHERE section.id =: sectionId");
            query.setParameter("sectionId", sectionId);
            result = Integer.valueOf(query.getSingleResult().toString());
        } catch (HibernateException e) {
            log.error("error while getting count of articles by section id", e);
        }
        return result;
    }

    @Override
    public List<Article> getLimitedNumberOfArticlesByUserId(int start, int total, Long userId) throws SQLException {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article WHERE author.id =: userId ORDER BY publicationDate DESC", Article.class);
            query.setParameter("userId", userId);
            query.setFirstResult(start - 1);
            query.setMaxResults(total);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting articles by user id", e);
        }
        return result;
    }

    @Override
    public int getCountOfArticlesByUserId(Long userId) throws SQLException {
        Session session = sessionFactory.openSession();
        int result = 0;
        try (session) {
            Query query = session.createQuery("SELECT count(a.id) FROM Article a WHERE author.id =: userId");
            query.setParameter("userId", userId);
            result = Integer.valueOf(query.getSingleResult().toString());
        } catch (HibernateException e) {
            log.error("error while getting count of articles by user id", e);
        }
        return result;
    }

    @Override
    public List<Article> getLimitedNumberOfArticlesByUserIdAndSectionId(int start, int total, Long userId, int sectionId) throws SQLException {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            Query<Article> query = session.createQuery("FROM Article WHERE author.id =: userId AND section.id =: sectionId ORDER BY publicationDate DESC", Article.class);
            query.setParameter("userId", userId);
            query.setParameter("sectionId", sectionId);
            query.setFirstResult(start - 1);
            query.setMaxResults(total);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting articles by user id and section id", e);
        }
        return result;
    }

    @Override
    public int getCountOfArticlesByUserIdAndSectionId(Long userId, int sectionId) throws SQLException {
        Session session = sessionFactory.openSession();
        int result = 0;
        try (session) {
            Query query = session.createQuery("SELECT count(a.id) FROM Article a WHERE author.id =: userId AND section.id =: sectionId");
            query.setParameter("userId", userId);
            query.setParameter("sectionId", sectionId);
            result = Integer.valueOf(query.getSingleResult().toString());
        } catch (HibernateException e) {
            log.error("error while getting count of articles by user id and section id", e);
        }
        return result;
    }

    @Override
    public List<Article> getAllBySectionId(int sectionId) {
        Session session = sessionFactory.openSession();
        List<Article> result = new ArrayList<>();
        try (session) {
            NativeQuery<Article> query = session.createNativeQuery("SELECT * FROM article WHERE A_section_id = ? ORDER BY A_publication_date DESC;", Article.class);
            query.setParameter(1, sectionId);
            result = query.list();
        } catch (HibernateException e) {
            log.error("error while getting all articles by section", e);
        }
        return result;
    }

    @Override
    public void addLike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("INSERT INTO user_article_like VALUES (?,?)");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while adding like to article_user table", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteLike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("DELETE FROM user_article_like WHERE Article_A_id = ? AND User_U_id = ?");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while deleting like from article_user_like table", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateLikeInArticle(Long articleId, boolean isLiked) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            Long numberOfLikes = read(articleId).orElseThrow(() -> new RuntimeException("unknown article")).getLikes();
            Query query = session.createQuery("UPDATE Article SET likes =:likes WHERE id =: id");
            if (isLiked) {
                query.setParameter("likes", numberOfLikes - 1);
            } else {
                query.setParameter("likes", numberOfLikes + 1);
            }
            query.setParameter("id", articleId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while update like in article", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean findLike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try (session) {
            NativeQuery query = session.createNativeQuery("SELECT * FROM user_article_like WHERE Article_A_id = ? AND  User_U_id = ?;");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            if (query.uniqueResult() != null) {
                result = true;
            }
        } catch (HibernateException e) {
            log.error("error while finding like in article", e);
        }
        return result;
    }

    @Override
    public void addDislike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("INSERT INTO user_article_dislike VALUES (?,?)");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while adding dislike to article_user_dislike table", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteDislike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery query = session.createNativeQuery("DELETE FROM user_article_dislike WHERE Article_A_id = ? AND User_U_id = ?");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while deleting dislike from article_user_dislike table", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateDislikeInArticle(Long articleId, boolean isDisliked) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            Long dislikes = read(articleId).orElseThrow(() -> new RuntimeException("unknown article")).getDislikes();
            Query query = session.createQuery("UPDATE Article SET dislikes =:dislikes WHERE id =: id");
            if (isDisliked) {
                query.setParameter("dislikes", dislikes - 1);
            } else {
                query.setParameter("dislikes", dislikes + 1);
            }
            query.setParameter("id", articleId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("error while update dislike in article", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean findDislike(Long articleId, Long userId) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try (session) {
            NativeQuery query = session.createNativeQuery("SELECT * FROM user_article_dislike WHERE Article_A_id = ? AND  User_U_id = ?;");
            query.setParameter(1, articleId);
            query.setParameter(2, userId);
            if (query.uniqueResult() != null) {
                result = true;
            }
        } catch (HibernateException e) {
            log.error("error while finding dislike in article", e);
        }
        return result;
    }

    public static ArticleDao getINSTANCE() {
        return INSTANCE;
    }

}
