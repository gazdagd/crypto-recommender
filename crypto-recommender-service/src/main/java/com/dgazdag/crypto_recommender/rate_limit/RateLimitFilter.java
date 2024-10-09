package com.dgazdag.crypto_recommender.rate_limit;

import com.dgazdag.crypto_recommender.rest.dto.ApiErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {

    private final Supplier<BucketConfiguration> bucketConfiguration;


    private final ProxyManager<String> proxyManager;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String key = httpRequest.getRemoteAddr() + "||" + ((HttpServletRequest) servletRequest).getRequestURI();
        Bucket bucket = proxyManager.builder().build(key, bucketConfiguration);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        log.debug("Tokens remaining: {}: {}", key, probe.getRemainingTokens());
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        if (probe.isConsumed()) {
            httpResponse.setHeader("X-Rate-Limit-Remaining", probe.getRemainingTokens() + "");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.setContentType("application/json");
            httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", "" + TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
            httpResponse.setStatus(429);
            httpResponse.getWriter().append(objectMapper.writeValueAsString(new ApiErrorDto().status(429).message("Too many requests")));
        }
    }

}
