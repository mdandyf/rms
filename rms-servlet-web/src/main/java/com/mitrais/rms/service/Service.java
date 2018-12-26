package com.mitrais.rms.service;

import java.sql.SQLException;

public interface Service<T, ID> {
    boolean doCheck(T t);
    boolean doInsert(T t) throws SQLException;
    boolean doUpdate(T t) throws SQLException;
    boolean doDelete(ID id) throws SQLException;
}
