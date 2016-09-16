package org.tkalenko.hibernate.dao;

import org.tkalenko.hibernate.entity.User;

import java.util.List;

public interface UserDAO {
    /**
     * Получение пользователя из базы по его логину
     * @param login логин
     * @return пользователь
     */
    User searchUser(final String login);

    /**
     * Получение всех пользователей
     * @return
     */
    List<User> selectAll();

}
