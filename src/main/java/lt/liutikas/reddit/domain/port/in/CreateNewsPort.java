package lt.liutikas.reddit.domain.port.in;

import lt.liutikas.reddit.domain.entity.core.News;

public interface CreateNewsPort {

    News create(News news);

}
