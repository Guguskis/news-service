package lt.liutikas.reddit.helper;

import lt.liutikas.reddit.model.reddit.Comment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CommentsParser {

    public List<Comment> parseComments(String pageHtmlBody) {
        Document document = Jsoup.parse(pageHtmlBody);
        Elements commentElements = document.getElementsByClass("comment");

        return commentElements.stream()
                .map(this::convertElementToComment)
                .collect(Collectors.toList());
    }

    private Comment convertElementToComment(Element commentElement) {
        String commentText = commentElement.getElementsByClass("usertext-body").text();
        String scoreText = commentElement.getElementsByClass("score unvoted").text();
        String creationDateString = commentElement.getElementsByAttribute("datetime").get(0).attr("datetime");

        Comment comment = new Comment();
        comment.setCreationDate(parseCreationDate(creationDateString));
        comment.setText(commentText);
        comment.setScore(parseScore(scoreText));

        return comment;
    }

    private LocalDateTime parseCreationDate(String createdDateString) {
        return LocalDateTime.parse(createdDateString, DateTimeFormatter.ISO_DATE_TIME);
    }

    public Integer parseScore(String text) {
        Pattern pattern = Pattern.compile("(-?\\d+) point");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            return null;
        }

        String scoreString = matcher.group(1);
        int score;
        try {
            score = Integer.parseInt(scoreString);
        } catch (NumberFormatException e) {
            return null;
        }

        return score;
    }
}
