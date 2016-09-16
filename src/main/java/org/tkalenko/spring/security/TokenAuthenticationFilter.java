package org.tkalenko.spring.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.tkalenko.hibernate.FacadeDAO;
import org.tkalenko.utils.Token;
import org.tkalenko.utils.password.FilePasswordKeeper;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private final static JSONParser JSON_PARSER = new JSONParser();

    protected TokenAuthenticationFilter(final String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new NoOpAuthenticationManager());
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthenticationSuccessHandler());
        setAuthenticationFailureHandler(new TokenSimpleUrlAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException,
            IOException, ServletException {
        String stringToken = request.getHeader(Token.HEADER);
        if (StringUtils.isEmpty(stringToken)) {
            log.error(String.format("token not found"));
            throw new AuthenticationServiceException("auth error");
        }
        log.debug(String.format("token found=[%s]", stringToken));
        Token token = Token.decode(stringToken);
        if (token == null) {
            log.error(String.format("decode problem token=[%s]", stringToken));
            throw new AuthenticationServiceException("auth error");
        }
        log.debug("try valid token");
        valid(token.getLogin(), token.getPassword());
        log.debug("valid token");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(token.getLogin(), token.getPassword(), authorities);
        response.addHeader(Token.HEADER, Token.encode(token));
        return new UsernamePasswordAuthenticationToken(user, authorities);
    }

    private void valid(final String login, final String password) throws AuthenticationException {
        org.tkalenko.hibernate.entity.User user = FacadeDAO.getInstance().getUserDao(FacadeDAO.SQLITE_TYPE).searchUser(login);
        if (user == null) {
            log.error(String.format("cant find user by login=[%s]"));
            throw new AuthenticationServiceException("auth error");
        }
        if (!StringUtils.equals(password, FilePasswordKeeper.getInstance().getPassword(String.valueOf(user.getId())))) {
            log.error(String.format("passwords not equals"));
            throw new AuthenticationServiceException("auth error");
        }
    }
}
