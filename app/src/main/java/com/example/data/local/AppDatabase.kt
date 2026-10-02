package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    SmartphoneEntity::class,
    PriceHistoryEntity::class,
    VideoEntity::class,
    BuyingGuideEntity::class,
    SavedItemEntity::class,
    SavedComparisonEntity::class,
    NewsEntity::class,
    SyncInfoEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun techMedhaDao(): TechMedhaDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "tech_medha_db"
        )
          .addCallback(DatabaseCallback(scope))
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateDatabase(database.techMedhaDao())
          }
        }
      }

      suspend fun populateDatabase(dao: TechMedhaDao) {
        dao.insertSmartphones(SeedData.initialPhones)
        dao.insertPriceHistories(SeedData.initialPriceHistories)
        dao.insertVideos(SeedData.initialVideos)
        dao.insertGuides(SeedData.initialGuides)
        dao.insertNewsList(SeedData.initialNews)
        dao.updateSyncInfo(SeedData.initialSyncInfo)
      }
    }
  }
}
