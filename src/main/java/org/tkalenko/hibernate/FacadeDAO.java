package org.tkalenko.hibernate;

import org.tkalenko.hibernate.dao.FactoryDAO;
import org.tkalenko.hibernate.dao.UserDAO;
import org.tkalenko.hibernate.sqlite.dao.SQLiteFactoryDAO;

import java.util.HashMap;
import java.util.Map;

public class FacadeDAO implements org.tkalenko.hibernate.dao.FacadeDAO {
    private static final FacadeDAO INSTANCE = new FacadeDAO();

    private Map<Integer, FactoryDAO> factoryDAOs = new HashMap<Integer, FactoryDAO>();

    //SUPPORTED TYPES
    public static final int SQLITE_TYPE = 1;

    private FacadeDAO() {
        factoryDAOs.put(SQLITE_TYPE, new SQLiteFactoryDAO());
    }

    public static FacadeDAO getInstance() {
        return INSTANCE;
    }

    public UserDAO getUserDao(final int type) throws IllegalArgumentException {
        return this.factoryDAOs.get(type).getUserDao();
    }
}
