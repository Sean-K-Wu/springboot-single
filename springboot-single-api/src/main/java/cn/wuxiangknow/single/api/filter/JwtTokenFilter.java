package cn.wuxiangknow.single.api.filter;


import cn.wuxiangknow.single.common.dto.jwt.JwtUser;
import cn.wuxiangknow.single.common.jwt.JwtTokenManager;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Descirption jwt过滤器
 * @Author WuXiang
 * @Date 2018/6/19/
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("sysUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenManager jwtTokenManager;





    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNotBlank(authHeader)) {
            String userId = jwtTokenManager.getUserIdFromToken(authHeader);

            logger.info("checking authentication userId:" + userId);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(userId);

                if (jwtTokenManager.validateToken(authHeader, userDetails.getId(),userDetails.getLastLoginDate(),userDetails.getLastPasswordResetDate())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated userID: " + userId + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
