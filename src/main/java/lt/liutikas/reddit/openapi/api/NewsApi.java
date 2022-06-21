/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.0.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package lt.liutikas.reddit.openapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.liutikas.reddit.openapi.model.NewsPage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T01:07:54.033581300+03:00[Europe/Vilnius]")
@Validated
@Tag(name = "news", description = "the news API")
public interface NewsApi {

    default NewsApiDelegate getDelegate() {
        return new NewsApiDelegate() {
        };
    }

    /**
     * GET /news : List all news
     *
     * @param subChannels Sub channels (required)
     * @param pageToken   Page token (optional, default to 0)
     * @param pageSize    Page size (optional, default to 10)
     * @return A list of news (status code 200)
     */
    @Operation(
            operationId = "listNews",
            summary = "List all news",
            tags = {"news"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "A list of news", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = NewsPage.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/news",
            produces = {"application/json"}
    )
    default ResponseEntity<NewsPage> listNews(
            @NotNull @Parameter(name = "subChannels", description = "Sub channels", required = true) @Valid @RequestParam(value = "subChannels", required = true, defaultValue = "ukraine,lithuania") List<String> subChannels,
            @Parameter(name = "pageToken", description = "Page token") @Valid @RequestParam(value = "pageToken", required = false, defaultValue = "0") Integer pageToken,
            @Parameter(name = "pageSize", description = "Page size") @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        return getDelegate().listNews(subChannels, pageToken, pageSize);
    }

}
