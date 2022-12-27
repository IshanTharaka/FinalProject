package com.seekercloud.pos.dao;

import com.seekercloud.pos.entity.SuperEntity;

import java.sql.SQLException;

public interface CrudDao<T extends SuperEntity,ID> {
    public boolean save(T t) throws SQLException, ClassNotFoundException;
    public boolean delete(ID id) throws SQLException, ClassNotFoundException;
    public boolean update(T t) throws SQLException, ClassNotFoundException;
}
