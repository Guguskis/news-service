package lt.liutikas.reddit.repository;

import lt.liutikas.reddit.model.News;
import lt.liutikas.reddit.model.ProcessingStatus;
import lt.liutikas.reddit.model.SentimentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentResultRepository extends JpaRepository<SentimentResult, Long> {

    Optional<SentimentResult> findByNews(News news);


    List<SentimentResult> findTop5ByStatus(ProcessingStatus notStarted);

}
