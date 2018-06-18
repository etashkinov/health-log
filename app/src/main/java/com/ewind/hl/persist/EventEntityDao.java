package com.ewind.hl.persist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventEntityDao {
    @Query("SELECT * FROM evententity")
    List<EventEntity> getAll();

    @Query("SELECT * FROM evententity WHERE area = :area AND "
            + "date = :date")
    List<EventEntity> findByAreaAndDate(int area, String date);

    @Query("SELECT * FROM evententity WHERE area = :area AND "
            + "date = :date AND type = :type")
    List<EventEntity> findByAreaAndDateAndType(int area, String date, String type);

    @Insert
    void insert(EventEntity eventEntity);

    @Update
    void update(EventEntity eventEntity);
}
