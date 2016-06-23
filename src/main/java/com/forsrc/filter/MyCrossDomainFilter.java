package com.forsrc.filter;


import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyCrossDomainFilter extends CharacterEncodingFilter {

    private static final ThreadLocal<Map<String, RateLimit>> threadLocal = new ThreadLocal<Map<String, RateLimit>>();

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI().toLowerCase();
        if (uri.indexOf("api") < 0) {
            super.doFilterInternal(request, response, filterChain);
            return;
        }
        Map<String, RateLimit> rateLimitMap = this.getRateLimit(uri);
        RateLimit rateLimit = rateLimitMap.get(uri);
        rateLimit.reset();
        int remaining = rateLimit.getRemaining();
        response.setHeader("X-Rate-Limit-Limit", "" + rateLimit.getLimit());
        response.setHeader("X-Rate-Limit-Reset", "" + rateLimit.getReset());
        response.setHeader("X-Rate-Limit-Remaining", "" + remaining);

        if (remaining <= 0) {
           /*
            400 Bad request when there was a problem with the request
            401 Unauthorized when you don't provide a valid key
            429 Too Many Requests when you've gone over your request rate limit
            500 Internal Server Error if we messed up -- please let us know!
            */
            response.setStatus(429);
            response.sendError(429, "Too Many Requests when you've gone over your request rate limit");
            response.getWriter().write("{\"status\";\"429\", \"message\":\"Too Many Requests when you've gone over your request rate limit\"}");
            response.getWriter().close();
            return;
        }
        /* cross-domain*/
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");


        super.doFilterInternal(request, response, filterChain);
    }

    public synchronized Map<String, RateLimit> getRateLimit(String uri) {
        Map<String, RateLimit> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, RateLimit>();
            map.put(uri, new RateLimit());
            threadLocal.set(map);
            return map;
        }
        if (!map.keySet().contains(uri)) {
            map.put(uri, new RateLimit());
        }
        return map;
    }

    public static class RateLimit {
        private int limit = 100;
        private long reset = 1L * 1000 * 60 * 10;
        private int remaining = 100;
        private long start = 0;

        public RateLimit() {
            this.start = System.currentTimeMillis();
        }

        public synchronized int getRemaining() {
            this.remaining--;
            if (this.remaining <= 0) {
                this.remaining = 0;
            }
            return this.remaining;
        }

        public synchronized void reset() {
            Long now = System.currentTimeMillis();
            if (now - start >= this.reset) {
                this.start = now;
                this.remaining = this.limit;
            }

        }

        public int getLimit() {
            return this.limit;
        }

        public long getReset() {
            return this.reset;
        }

        public long getStart() {
            return this.start;
        }
    }
}
