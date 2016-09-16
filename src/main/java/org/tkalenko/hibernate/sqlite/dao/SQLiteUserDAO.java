package org.tkalenko.hibernate.sqlite.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.tkalenko.hibernate.dao.UserDAO;
import org.tkalenko.hibernate.entity.User;
import org.tkalenko.hibernate.sqlite.utils.SQLiteSessionFactory;

import java.util.List;

public class SQLiteUserDAO implements UserDAO {
    public User searchUser(final String login) {
        Session session = SQLiteSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("from User where login = :login");
        query.setParameter("login", login);
        List<User> res = (List<User>) query.list();
        session.close();
        return CollectionUtils.isEmpty(res) ? null : res.iterator().next();
    }

    public List<User> selectAll() {
        Session session = SQLiteSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("from User");
        List<User> res = (List<User>) query.list();
        session.close();
        return res;
    }
}
