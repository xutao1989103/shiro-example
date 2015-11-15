package com.github.zhangkaitao.shiro.spring;

import com.github.zhangkaitao.shiro.chapter19.entity.HttpMethod;
import com.github.zhangkaitao.shiro.chapter19.entity.UrlFilter;
import com.github.zhangkaitao.shiro.chapter19.service.UrlFilterService;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;
/**

 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-25
 * <p>Version: 1.0
 */
public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    private CustomDefaultFilterChainManager customDefaultFilterChainManager;

    @Autowired
    private UrlFilterService urlFilterService;

    public void setCustomDefaultFilterChainManager(CustomDefaultFilterChainManager customDefaultFilterChainManager) {
        this.customDefaultFilterChainManager = customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }

    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);

        List<String> chainNames = new ArrayList<String>();
        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        for (String pathPattern : filterChainManager.getChainNames()) {

            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathMatches(pathPattern, requestURI)) {
                chainNames.add(pathPattern);
            }
        }

        if(chainNames.size() == 0) {
            return null;
        }

        return customDefaultFilterChainManager.proxy(originalChain, chainNames);
    }

    private boolean pathMatcher(String pathPattern, String requestURI, ServletRequest request){
        if(pathPattern.equals(requestURI) || pathPattern.equals(requestURI + "/**")){
            return checkRequestMethod(requestURI, request);
        }else {
            return pathMatches(pathPattern, requestURI);
        }
    }

    private boolean checkRequestMethod(String requestURI, ServletRequest request){
        UrlFilter urlFilter = urlFilterService.findOneByUrl(requestURI);
        if(urlFilter!=null){
            return ((ShiroHttpServletRequest)request).getMethod().equals(HttpMethod.getMethodByValue(urlFilter.getMethod()).getName());
        }
        return false;
    }
}
