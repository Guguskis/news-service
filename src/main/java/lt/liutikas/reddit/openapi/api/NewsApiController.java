package lt.liutikas.reddit.openapi.api;

import lt.liutikas.reddit.openapi.model.CreateNewsRequest;
import lt.liutikas.reddit.openapi.model.News;
import lt.liutikas.reddit.openapi.model.NewsPage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T18:45:20.919060700+03:00[Europe/Vilnius]")
@Controller
@RequestMapping("${openapi.news.base-path:/api/v1}")
public class NewsApiController implements NewsApi {

    private final NewsApiDelegate delegate;

    public NewsApiController(@Autowired(required = false) NewsApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new NewsApiDelegate() {});
    }

    @Override
    public NewsApiDelegate getDelegate() {
        return delegate;
    }

}
