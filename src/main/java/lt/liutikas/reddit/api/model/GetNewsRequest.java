package lt.liutikas.reddit.api.model;

import java.util.ArrayList;
import java.util.List;

public class GetNewsRequest extends PaginationQuery {

    private List<String> subChannels = new ArrayList<>();

    public GetNewsRequest() {
    }

    public GetNewsRequest(List<String> subChannels, Integer pageToken, Integer pageSize) {
        super(pageToken, pageSize);
        this.subChannels = subChannels;
    }

    public List<String> getSubChannels() {
        return subChannels;
    }

    public void setSubChannels(List<String> subChannels) {
        this.subChannels = subChannels;
    }
}
