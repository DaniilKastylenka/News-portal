package by.it.academy.project.dto;

import by.it.academy.project.model.Article;
import by.it.academy.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CommentDto {

    private Long id;
    private User user;
    private String text;
    private Date date;
    private Long likes;
    private Long dislikes;
    private Article article;
    private boolean liked;
    private boolean disliked;

    public CommentDto(Long id, User user, String text, Date date, Long likes, Long dislikes, Article article) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
        this.article = article;
    }
}
