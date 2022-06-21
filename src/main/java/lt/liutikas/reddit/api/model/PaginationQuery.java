package lt.liutikas.reddit.api.model;

import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.Min;

public class PaginationQuery {

    @Min(0)
    private Integer pageToken = 0;
    @Min(1)
    private Integer pageSize = 10;

    public PaginationQuery() {
    }

    public PaginationQuery(Integer pageToken, Integer pageSize) {
        this.pageToken = pageToken;
        this.pageSize = pageSize;
    }

    public PageRequest pageRequest() {
        return PageRequest.of(pageToken, pageSize);
    }

    public Integer getPageToken() {
        return pageToken;
    }

    public void setPageToken(Integer pageToken) {
        this.pageToken = pageToken;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
