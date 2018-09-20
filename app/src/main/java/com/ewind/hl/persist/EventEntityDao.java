package com.ewind.hl.persist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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
    List<EventEntity> findByAreaAndDate(String area, String date);

    @Query("SELECT * FROM evententity WHERE id = :id")
    EventEntity findById(long id);

    @Query("SELECT * FROM evententity WHERE area = :area AND "
            + "date >= :from AND date <= :till AND type = :type "
            + " ORDER BY date DESC")
    List<EventEntity> findByAreaAndDateRangeAndType(String area, String from, String till, String type);

    @Insert
    void insert(EventEntity eventEntity);

    @Update
    void update(EventEntity eventEntity);

    @Delete
    void delete(EventEntity eventEntity);

    @Query("SELECT e.* FROM evententity e " +
            " JOIN (SELECT type, area, max(date) as date FROM evententity GROUP BY type, area) le " +
            " WHERE e.type = le.type AND e.area = le.area AND e.date = le.date")
    List<EventEntity> findLatest();

    @Query("SELECT e.* FROM evententity e " +
            " JOIN (SELECT type, max(date) as date FROM evententity GROUP BY type) le " +
            " WHERE e.type = le.type AND e.date = le.date AND e.type = :type")
    EventEntity findLatest(String type);

    @Query("SELECT e.* FROM evententity e ORDER BY e.date ASC")
    List<EventEntity> findAll();
}
