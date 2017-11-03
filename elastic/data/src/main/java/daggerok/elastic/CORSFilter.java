package daggerok.elastic;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@NoArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

  @Override
  @SneakyThrows
  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) {

    val response = (HttpServletResponse) res;

    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization");

    val request = (HttpServletRequest) req;

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

      response.setStatus(HttpServletResponse.SC_OK);

    } else {

      chain.doFilter(req, res);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void destroy() {}
}
