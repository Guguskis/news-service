package lt.liutikas.reddit.model.api;

import java.util.ArrayList;
import java.util.List;

public class GetNewsRequest extends PaginationQuery {

    public List<String> subChannels = new ArrayList<>();

    public List<String> getSubChannels() {
        return subChannels;
    }

    public void setSubChannels(List<String> subChannels) {
        this.subChannels = subChannels;
    }
}
