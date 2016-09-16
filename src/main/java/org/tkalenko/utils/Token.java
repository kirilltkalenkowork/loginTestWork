package org.tkalenko.utils;

import org.apache.commons.lang3.StringUtils;

public class Token {
    public static final String HEADER = "AUTH_TOKEN";

    private String login;
    private String password;

    public static Token decode(final String token) {
        if (StringUtils.isBlank(token))
            return null;
        String[] strings = token.split(":");
        if (strings.length != 2 || StringUtils.isBlank(strings[0]) || StringUtils.isBlank(strings[1]))
            return null;
        Token res = new Token();
        res.setLogin(strings[0]);
        res.setPassword(strings[1]);
        return res;
    }

    public static String encode(final Token token) {
        if (token == null)
            return null;
        return String.format("%s:%s", token.getLogin(), token.getPassword());
    }

    public Token() {
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Token{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
