package org.tkalenko.jersey;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkalenko.hibernate.FacadeDAO;
import org.tkalenko.hibernate.entity.User;
import org.tkalenko.utils.Token;
import org.tkalenko.utils.password.FilePasswordKeeper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

@Path("/login")
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final JSONParser JSON_PARSER = new JSONParser();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(final String data) {
        JSONObject res = null;
        int resCode = 0;
        Token token = null;
        log.debug(String.format("login service data=[%s]", data));
        try {
            JSONObject req = (JSONObject) JSON_PARSER.parse(data);
            JSONObject parameters = (JSONObject) req.get("parameters");

            String reqUsername = (String) parameters.get("username");
            String reqPassword = (String) parameters.get("password");

            if (StringUtils.isBlank(reqUsername) || StringUtils.isBlank(reqPassword)) {
                log.error(String.format("empty username=[%s] password=[%s]", reqUsername, reqPassword));
                throw new RuntimeException("Нет такого пользователя");
            }
            log.debug(String.format("validate username=[%s] password=[%s]", reqUsername, reqPassword));
            User user = FacadeDAO.getInstance().getUserDao(FacadeDAO.SQLITE_TYPE).searchUser(reqUsername);
            if (null == user) {
                log.error(String.format("can't find user by login=[%s]", reqUsername));
                throw new RuntimeException("Нет такого пользователя");
            }
            String md5Password = FilePasswordKeeper.getInstance().getPassword(String.valueOf(user.getId()));
            String md5ReqPassword = DigestUtils.md5Hex(reqPassword);
            if (!StringUtils.equals(md5ReqPassword, md5Password)) {
                log.error(String.format("reqPassword=[%s=%s] not eq for userId=[%s]", reqPassword, md5ReqPassword, user.getId()));
                throw new RuntimeException("Неверный пароль");
            }

            JSONObject name = new JSONObject();
            name.put("name", user.getName());

            res = buildResult(name);
            resCode = HttpURLConnection.HTTP_OK;
            token = new Token();
            token.setLogin(reqUsername);
            token.setPassword(md5Password);
        } catch (Throwable t) {
            log.error(String.format("can't login user"), t);
            JSONObject error = new JSONObject();
            error.put("error", t.getMessage());

            res = buildResult(error);
            resCode = HttpURLConnection.HTTP_BAD_REQUEST;
        }
        Response.ResponseBuilder responseBuilder = Response.status(resCode);
        responseBuilder.entity(res.toJSONString());
        switch (resCode) {
            case HttpURLConnection.HTTP_OK:
                responseBuilder.header(Token.HEADER, Token.encode(token));
                break;
        }
        return responseBuilder.build();
    }

    private JSONObject buildResult(final JSONObject mess) {
        JSONObject res = new JSONObject();
        res.put("result", mess);
        return res;
    }
}
