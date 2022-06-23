package com.wlb.demo.user.filter;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字符串Trim过滤器
 *
 * @author Eric Wang
 * @since 2021/4/1
 */
@SuppressWarnings("unused")
public class TrimFilter extends OncePerRequestFilter {

    /**
     * do filter
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain filter chain
     * @throws ServletException servlet异常
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new TrimHttpServletRequestWrapper(request), response);
    }
}
