package lt.liutikas.reddit.assembler;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ScannedNewsEvent;
import org.springframework.stereotype.Component;

@Component
public class NewsAssembler {

    public News assembleNews(ScannedNewsEvent event) {
        News message = new News();

        message.setTitle(event.getTitle());
        message.setUrl(event.getUrl());
        message.setCreated(event.getCreated());

        return message;
    }

}
