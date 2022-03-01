package lt.liutikas.reddit.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

    private final WebProperties webProperties;

    public CORSFilter(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, webProperties.getAccessControlAllowOrigin());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, webProperties.getAccessControlAllowHeaders());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, webProperties.getAccessControlAllowMethods());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
