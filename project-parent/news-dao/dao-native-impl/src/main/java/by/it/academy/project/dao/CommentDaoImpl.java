package by.it.academy.project.dao;

import by.it.academy.project.model.Comment;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDaoImpl extends AbstractDao implements CommentDao {

    private static CommentDaoImpl INSTANCE = new CommentDaoImpl();


    protected CommentDaoImpl() {
        super(LoggerFactory.getLogger(CommentDaoImpl.class));
    }

    private final static String INSERT_COMMENT =
            "INSERT INTO comment(C_user_id, C_text, C_date, C_number_of_likes, C_article_id) VALUE(?,?,?,?,?)";

    private final static String SELECT_COMMENT_BY_ID =
            "SELECT * FROM comment c " +
                    "JOIN user u ON c.C_user_id=u.U_id " +
                    "JOIN role r ON u.U_role_id = r.R_id " +
                    "JOIN article a ON c.C_article_id = a.A_id " +
                    "JOIN section s on a.A_section_id = s.S_id WHERE c.C_id = ?;";

    private final static String UPDATE_COMMENT =
            "UPDATE comment SET C_user_id = ?, C_text = ?, C_date = ?, C_number_of_likes = ?, C_article_id = ? WHERE C_id = ?;";

    private final static String DELETE_COMMENT =
            "DELETE FROM comment WHERE C_id = ?;";

    private final static String SELECT_ALL =
            "SELECT * FROM comment c " +
                    "JOIN user u ON c.C_user_id = u.U_id " +
                    "JOIN role r ON u.U_role_id = r.R_id " +
                    "JOIN article a on c.C_article_id = a.A_id " +
                    "JOIN section s on a.A_section_id = s.S_id ORDER BY c.C_id DESC;";


    private final static String INSERT_LIKE =
            "INSERT INTO comment_user VALUE(?,?);";

    private final static String DELETE_LIKE =
            "DELETE FROM comment_user WHERE CU_comment_id = ? AND CU_user_id = ?;";

    private final static String UPDATE_LIKE =
            "UPDATE comment SET C_number_of_likes = ? WHERE C_id = ?";

    private final static String SELECT_LIKE =
            "SELECT * FROM comment_user WHERE CU_comment_id = ? AND CU_user_id = ?;";

    @Override
    public Long create(Comment comment) throws SQLException {
        ResultSet resultSet = null;
        Long result = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, comment.getUser().getId());
            statement.setString(2, comment.getText());

            Timestamp timestamp = new Timestamp(comment.getDate().getTime());
            statement.setObject(3, timestamp);

            statement.setLong(4, comment.getNumberOfLikes());
            statement.setLong(5, comment.getArticle().getId());

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();

            while (resultSet.next()) {
                result = resultSet.getLong(1);
            }
        } finally {
            closeQuietly(resultSet);
        }
        return result;
    }

    @Override
    public Optional<Comment> read(Long id) throws SQLException {

        ResultSet resultSet = null;
        Optional<Comment> result = Optional.empty();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_COMMENT_BY_ID)) {

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = Optional.of(Mapper.mapComment(resultSet));
            }
        } finally {
            closeQuietly(resultSet);
        }
        return result;
    }

    @Override
    public int update(Comment comment) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_COMMENT)) {

            statement.setLong(1, comment.getUser().getId());
            statement.setString(2, comment.getText());

            Timestamp timestamp = new Timestamp(comment.getDate().getTime());
            statement.setObject(3, timestamp);

            statement.setLong(4, comment.getNumberOfLikes());

            statement.setLong(5, comment.getArticle().getId());

            statement.setLong(6, comment.getId());

            return statement.executeUpdate();
        }
    }

    @Override
    public int delete(Long id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_COMMENT)) {

            statement.setLong(1, id);

            return statement.executeUpdate();
        }
    }

    @Override
    public List<Comment> getAll() throws SQLException {
        ResultSet resultSet = null;
        List<Comment> result = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(Mapper.mapComment(resultSet));
            }

        } finally {
            closeQuietly(resultSet);
        }
        return result;
    }

    public static CommentDaoImpl getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void addLike(Long commentId, Long userId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_LIKE)) {

            statement.setLong(1, commentId);
            statement.setLong(2, userId);

            statement.executeUpdate();

        }
    }

    @Override
    public int deleteLike(Long commentId, Long userId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_LIKE)) {
            statement.setLong(1, commentId);
            statement.setLong(2, userId);
            return statement.executeUpdate();
        }
    }

    @Override
    public int updateLikeInComment(Long commentId, boolean isLiked) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LIKE)) {
            Comment comment = read(commentId)
                    .orElseThrow(() -> new RuntimeException("no comment with id " + commentId));
            if (isLiked) {
                statement.setLong(1, comment.getNumberOfLikes() - 1);
            } else {
                statement.setLong(1, comment.getNumberOfLikes() + 1);
            }
            statement.setLong(2, commentId);
            return statement.executeUpdate();
        }
    }

    @Override
    public boolean findLike(Long commentId, Long userId) throws SQLException {
        ResultSet resultSet = null;
        boolean result = false;
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(SELECT_LIKE)){

            statement.setLong(1, commentId);
            statement.setLong(2, userId);

            resultSet = statement.executeQuery();

            if (resultSet.next()){
                result = true;
            }
        } finally {
            closeQuietly(resultSet);
        }
        return result;
    }
}
