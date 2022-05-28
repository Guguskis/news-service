package lt.liutikas.reddit.model.api;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.Min;

public class PaginationQuery {

    @Min(0)
    private Integer pageToken = 0;
    @Min(1)
    @ApiModelProperty(value = "Number of items per page", example = "10")
    private Integer pageSize = 10;

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
