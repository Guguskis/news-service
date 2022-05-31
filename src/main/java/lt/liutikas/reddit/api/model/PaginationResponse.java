package lt.liutikas.reddit.api.model;

import org.springframework.data.domain.Pageable;

public class PaginationResponse {

    private Integer nextPageToken;

    public Integer getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(Integer nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setNextToken(Pageable pageable) {
        if (pageable.isPaged())
            this.nextPageToken = pageable.getPageNumber();
    }
}
