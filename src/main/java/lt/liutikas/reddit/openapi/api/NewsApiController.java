package lt.liutikas.reddit.openapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-06-22T00:47:59.053065100+03:00[Europe/Vilnius]")
@Controller
@RequestMapping("${openapi.news.base-path:/api/v1}")
public class NewsApiController implements NewsApi {

    private final NewsApiDelegate delegate;

    public NewsApiController(@Autowired(required = false) NewsApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new NewsApiDelegate() {
        });
    }

    @Override
    public NewsApiDelegate getDelegate() {
        return delegate;
    }

}
