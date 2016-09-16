package org.tkalenko.hibernate.dao;

public interface FactoryDAO {

    /**
     * Получение реализации UserDAO
     * @return UserDAO
     */
    UserDAO getUserDao();
}
