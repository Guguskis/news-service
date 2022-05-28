package lt.liutikas.reddit.model.api;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class GetNewsRequest extends PaginationQuery {

    @ApiModelProperty(value = "List of subChannels to get news from", required = true, example = "funny,pics")
    public List<String> subChannels = new ArrayList<>();

    public List<String> getSubChannels() {
        return subChannels;
    }

    public void setSubChannels(List<String> subChannels) {
        this.subChannels = subChannels;
    }
}
