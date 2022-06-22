package lt.liutikas.reddit.openapi.api;

import lt.liutikas.reddit.openapi.model.NewsPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link NewsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T09:28:58.061353200+03:00[Europe/Vilnius]")
public interface NewsApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /news : List all news
     *
     * @param subChannels Sub channels (required)
     * @param pageToken   Page token (optional, default to 0)
     * @param pageSize    Page size (optional, default to 10)
     * @return A list of news (status code 200)
     * @see NewsApi#listNews
     */
    default ResponseEntity<NewsPage> listNews(List<String> subChannels,
                                              Integer pageToken,
                                              Integer pageSize) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"news\" : [ { \"sentiment\" : { \"scorePositive\" : 5.637376656633329, \"scoreNeutral\" : 2.3021358869347655, \"scoreNegative\" : 5.962133916683182, \"id\" : 1 }, \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"subChannel\" : \"subChannel\", \"id\" : 6, \"title\" : \"title\", \"url\" : \"url\" }, { \"sentiment\" : { \"scorePositive\" : 5.637376656633329, \"scoreNeutral\" : 2.3021358869347655, \"scoreNegative\" : 5.962133916683182, \"id\" : 1 }, \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"subChannel\" : \"subChannel\", \"id\" : 6, \"title\" : \"title\", \"url\" : \"url\" } ], \"nextPageToken\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
