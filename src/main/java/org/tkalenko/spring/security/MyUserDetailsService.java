package org.tkalenko.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tkalenko.hibernate.FacadeDAO;
import org.tkalenko.hibernate.entity.User;
import org.tkalenko.utils.password.FilePasswordKeeper;

import java.util.ArrayList;
import java.util.List;

public class MyUserDetailsService implements UserDetailsService {
    private final static Logger log = LoggerFactory.getLogger(MyUserDetailsService.class);

    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        log.debug(String.format("try find user"));
        User userDb = FacadeDAO.getInstance().getUserDao(FacadeDAO.SQLITE_TYPE).searchUser(login);
        if (userDb == null) {
            log.error(String.format("cant't find user by login=[%s]", login));
            throw new UsernameNotFoundException("Нет такого пользователя");
        }
        String password = FilePasswordKeeper.getInstance().getPassword(String.valueOf(userDb.getId()));
        if (password == null) {
            log.error(String.format("cant't find user password by id=[%s]", userDb.getId()));
            throw new UsernameNotFoundException("Неверный пароль");
        }
        log.debug(String.format("user find by login=[%s] and password=[%s]", login, password));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(login, password, authorities);
    }
}
