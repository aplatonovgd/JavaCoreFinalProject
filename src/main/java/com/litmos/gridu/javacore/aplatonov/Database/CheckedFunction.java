package com.litmos.gridu.javacore.aplatonov.Database;

import java.sql.SQLException;

@FunctionalInterface
public interface CheckedFunction<T, R> {
        R apply(T t) throws SQLException;
}
