package com.ewind.hl.persist;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {EventEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "health-log-db";

    private static AppDatabase instance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `EventEntity` ADD `owner` TEXT");
            database.execSQL("ALTER TABLE `EventEntity` ADD `reporter` TEXT");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


    public abstract EventEntityDao eventEntityDao();
}
