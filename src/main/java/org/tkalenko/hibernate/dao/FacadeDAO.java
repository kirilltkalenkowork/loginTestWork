package org.tkalenko.hibernate.dao;

public interface FacadeDAO {

    /**
     * Получнеие UserDAO, с соответствующим типом соединения
     * @param type тип соединения
     * @return UserDAO
     * @throws IllegalArgumentException в случае проблем с типом соединения
     */
    UserDAO getUserDao(final int type) throws IllegalArgumentException;

}
