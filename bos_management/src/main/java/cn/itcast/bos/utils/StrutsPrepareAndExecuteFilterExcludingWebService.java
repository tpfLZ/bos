package cn.itcast.bos.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class StrutsPrepareAndExecuteFilterExcludingWebService extends StrutsPrepareAndExecuteFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getRequestURI().contains("service")) {
            // 如果是访问webservice,不进行过滤
            chain.doFilter(req, res);
        } else {
            super.doFilter(req, res, chain);
        }
    }
}
