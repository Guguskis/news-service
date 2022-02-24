package lt.liutikas.reddit.model.reddit;

import java.util.List;

public class PostComments {

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
