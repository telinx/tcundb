package com.tcun.util;

import java.sql.SQLException;

public class ClassUtil {

    public static <T> T newInstance(Class<T> c) throws SQLException {

        try {

            return c.newInstance();

        } catch (InstantiationException e) {

            throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {

            throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());

        }

    }

}
