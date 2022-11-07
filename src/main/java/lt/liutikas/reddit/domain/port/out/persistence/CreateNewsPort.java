package lt.liutikas.reddit.domain.port.out.persistence;

import lt.liutikas.reddit.api.model.SaveNewsRequest;
import lt.liutikas.reddit.domain.entity.core.News;

public interface CreateNewsPort {

    News createNews(SaveNewsRequest request);

}
