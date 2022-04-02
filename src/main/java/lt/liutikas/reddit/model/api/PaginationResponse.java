package lt.liutikas.reddit.model.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PaginationResponse {

    private Integer nextPageToken;

    public Integer getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(Integer nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setNextToken(Page page) {
        Pageable pageable = page.nextPageable();

        if (pageable.isPaged()) {
            this.nextPageToken = pageable.getPageNumber();
        }

    }
}
