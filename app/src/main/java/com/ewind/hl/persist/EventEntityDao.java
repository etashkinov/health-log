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

    @Query("SELECT * FROM evententity WHERE area = :area AND owner = :owner "
            + " AND date = :date")
    List<EventEntity> findByAreaAndDate(String area, String date, String owner);

    @Query("SELECT * FROM evententity WHERE id = :id")
    EventEntity findById(long id);

    @Query("SELECT * FROM evententity WHERE area = :area AND "
            + "date >= :from AND date <= :till AND type = :type AND owner = :owner "
            + " ORDER BY date DESC")
    List<EventEntity> findByAreaAndDateRangeAndType(String area, String from, String till, String type, String owner);

    @Query("SELECT * FROM evententity WHERE area = :area AND type = :type AND owner = :owner"
            + " ORDER BY date DESC")
    List<EventEntity> findByAreaAndType(String area, String type, String owner);

    @Insert
    void insert(EventEntity eventEntity);

    @Update
    void update(EventEntity eventEntity);

    @Delete
    void delete(EventEntity eventEntity);

    @Query("SELECT e.* FROM evententity e " +
            " JOIN (SELECT type, area, max(date) as date FROM evententity WHERE owner = :owner GROUP BY type, area) le " +
            " WHERE e.type = le.type AND e.area = le.area AND e.date = le.date AND e.owner = :owner")
    List<EventEntity> findLatest(String owner);

    @Query("SELECT e.* FROM evententity e " +
            " JOIN (SELECT type, max(date) as date FROM evententity WHERE owner = :owner GROUP BY type) le " +
            " WHERE e.type = le.type AND e.date = le.date AND e.type = :type AND e.owner = :owner")
    EventEntity findLatest(String type, String owner);

    @Query("SELECT e.* FROM evententity e ORDER BY e.date ASC")
    List<EventEntity> findAll();

    @Query("UPDATE evententity SET owner = :owner WHERE owner is null")
    void updateEmptyOwner(String owner);

    @Query("UPDATE evententity SET reporter = :reporter WHERE reporter is null")
    void updateEmptyReporter(String reporter);
}
