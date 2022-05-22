package lt.liutikas.reddit.repository;

import lt.liutikas.reddit.model.Channel;
import lt.liutikas.reddit.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findBySubChannelInIgnoreCase(List<String> subChannels, Pageable pageable);

    Page<News> findByChannelAndSubChannelInIgnoreCase(Channel channel, List<String> subChannels, Pageable pageable);

    Page<News> findByChannel(Channel channel, Pageable pageable);

}
