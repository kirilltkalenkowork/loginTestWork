package org.tkalenko.hibernate.sqlite.dao;

import org.tkalenko.hibernate.dao.FactoryDAO;
import org.tkalenko.hibernate.dao.UserDAO;

public class SQLiteFactoryDAO implements FactoryDAO {
    private static final UserDAO USER_DAO = new SQLiteUserDAO();

    public UserDAO getUserDao() throws IllegalArgumentException {
        return USER_DAO;
    }
}
