package lt.liutikas.reddit.opm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class OpmScanner {

    private static final Logger LOG = LoggerFactory.getLogger(OpmScanner.class);

    private final RestTemplate restTemplate;
    private final ChapterRepository chapterRepository;
    private final DiscordBot discordBot;

    public OpmScanner(@Qualifier("opm") RestTemplate restTemplate, ChapterRepository chapterRepository, DiscordBot discordBot) {
        this.restTemplate = restTemplate;
        this.chapterRepository = chapterRepository;
        this.discordBot = discordBot;
    }

    private static Chapter assembleChapter(String string) {
        Chapter chapter = new Chapter();
        chapter.setTitle(string);
        return chapter;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void scan() {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/", String.class);
        String html = responseEntity.getBody();
        Document document = Jsoup.parse(html);
        List<Chapter> chapters = parseChapterElements(document).stream()
                .map(OpmScanner::assembleChapter)
                .collect(Collectors.toList());
        List<Chapter> savedChapters = chapterRepository.findAll();

        List<Chapter> newChapters = chapters.stream()
                .filter(chapter -> isNewChapter(chapter, savedChapters))
                .collect(Collectors.toList());

        if (newChapters.isEmpty())
            return;

        LOG.info("New chapters found!");
        chapterRepository.saveAll(newChapters);
        discordBot.sendMessage("New one-punch-man manga chapter released!");
    }

    private List<String> parseChapterElements(Document document) {
        return document.select("div#Chapters_List ul li a").stream().map(Element::text).collect(Collectors.toList());
    }

    private boolean isNewChapter(Chapter chapter, List<Chapter> savedChapters) {
        return savedChapters.stream()
                .filter(savedChapter -> savedChapter.getTitle().equals(chapter.getTitle()))
                .findAny()
                .isEmpty();
    }
}
