package lt.liutikas.reddit.client;

import lt.liutikas.reddit.helper.CommentsParser;
import lt.liutikas.reddit.helper.PostParser;
import lt.liutikas.reddit.model.reddit.Comment;
import lt.liutikas.reddit.model.reddit.PageCategory;
import lt.liutikas.reddit.model.reddit.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RedditClient {

    private static final Logger LOG = LoggerFactory.getLogger(RedditClient.class);
    private static final String REDDIT_SUBREDDIT_TOP_PAST_HOUR_URL = "/r/%s/%s";
    private static final String GET_POST_PAGE_URL = "/r/%s/comments/%s/";
    private final PostParser postParser;
    private final CommentsParser commentsParser;
    private final RestTemplate restTemplate;

    public RedditClient(PostParser postParser,
                        CommentsParser commentsParser,
                        @Qualifier("reddit") RestTemplate restTemplate) {
        this.postParser = postParser;
        this.commentsParser = commentsParser;
        this.restTemplate = restTemplate;
    }

    public List<Post> getPosts(String subreddit, PageCategory category) {
        String pageHtml = getSubredditPostsHtmlPage(subreddit, category);
        Document document = Jsoup.parse(pageHtml);
        Elements postElements = document.getElementsByClass("link");

        List<Post> posts = postElements.stream()
                .map(this::convertElementToPost)
                .collect(Collectors.toList());

        LOG.info(String.format("Retrieved '%s' posts for subreddit '%s'", posts.size(), subreddit));

        return posts;
    }

    public List<Comment> getCommentsForPost(String subreddit, String postId) {
        String hotPostHtmlPage = getPostPage(subreddit, postId);
        return commentsParser.parseComments(hotPostHtmlPage);
    }

    public String getSubredditPostsHtmlPage(String subreddit, PageCategory category) {
        String url = String.format(REDDIT_SUBREDDIT_TOP_PAST_HOUR_URL, subreddit, category.toString().toLowerCase());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHttpEntityWithHeaders(), String.class);
        return response.getBody();
    }

    private String getPostPage(String subreddit, String postId) {
        String url = String.format(GET_POST_PAGE_URL, subreddit, postId);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHttpEntityWithHeaders(), String.class);
        return response.getBody();
    }

    private Post convertElementToPost(Element postElement) {
        String title = postElement.select("a.title").text();
        String scoreText = postElement.getElementsByClass("score unvoted").get(0).attr("title");
        String commentCountText = postElement.getElementsByClass("comments").text();
        String creationDateText = postElement.getElementsByClass("live-timestamp").get(0).attr("datetime");
        String link = postElement.getElementsByClass("comments").get(0).attr("href");

        Post post = new Post();
        post.setTitle(title);
        post.setScore(postParser.parseScore(scoreText));
        post.setCommentCount(postParser.parseCommentCount(commentCountText));
        post.setCreationDate(postParser.parseCreationDate(creationDateText));
        post.setLink(link);

        return post;
    }

    private HttpEntity<Object> getHttpEntityWithHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.USER_AGENT, "retardedStockBot 0.1"); // Reddit restricts API access for default agents
        return new HttpEntity<>(httpHeaders);
    }
}
