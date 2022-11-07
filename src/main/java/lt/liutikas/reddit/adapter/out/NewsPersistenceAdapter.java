package lt.liutikas.reddit.adapter.out;

import lt.liutikas.reddit.api.model.GetNewsRequest;
import lt.liutikas.reddit.api.model.NewsPage;
import lt.liutikas.reddit.config.exception.NotFoundException;
import lt.liutikas.reddit.domain.entity.core.Channel;
import lt.liutikas.reddit.domain.entity.core.News;
import lt.liutikas.reddit.domain.port.out.persistence.QueryNewsPort;
import lt.liutikas.reddit.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsPersistenceAdapter implements QueryNewsPort {

    private static final Logger LOG = LoggerFactory.getLogger(NewsPersistenceAdapter.class);

    final NewsRepository newsRepository;

    public NewsPersistenceAdapter(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public News findOne(Long id) {
        Optional<News> optional = newsRepository.findById(id);

        if (optional.isEmpty())
            throw new NotFoundException(String.format("News not found { \"id\": %s }", id));

        return optional.get();
    }

    @Override
    public NewsPage findMany(GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();

        Page<News> page = findNews(pageRequest, subChannels);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page.nextPageable());

        LOG.info("Returning news { 'pageToken': {}, 'pageSize': {} }", pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }


    @Override
    public NewsPage findMany(Channel channel, GetNewsRequest request) {
        PageRequest pageRequest = getPageRequestByCreatedDesc(request);
        List<String> subChannels = request.getSubChannels();
        Page<News> page = findNews(channel, subChannels, pageRequest);

        NewsPage newsPage = new NewsPage();
        newsPage.setNews(page.getContent());
        newsPage.setNextToken(page.nextPageable());

        LOG.info("Returning news { 'channel': '{}''pageToken': {}, 'pageSize': {} }", channel, pageRequest.getPageNumber(), request.getPageSize());

        return newsPage;
    }


    Page<News> findNews(PageRequest pageRequest, List<String> subChannels) {
        if (subChannels.isEmpty()) {
            return newsRepository.findAll(pageRequest);
        } else {
            return newsRepository.findBySubChannelInIgnoreCase(subChannels, pageRequest);
        }
    }

    Page<News> findNews(Channel channel, List<String> subChannels, PageRequest pageRequest) {
        if (subChannels.isEmpty()) {
            return newsRepository.findByChannel(channel, pageRequest);
        } else {
            return newsRepository.findByChannelAndSubChannelInIgnoreCase(channel, subChannels, pageRequest);
        }
    }


    PageRequest getPageRequestByCreatedDesc(GetNewsRequest request) {
        PageRequest pageRequest = request.pageRequest();
        Sort sort = Sort.by("created").descending();
        return pageRequest.withSort(sort);
    }

}
