package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TechMedhaDao {

  // Smartphones
  @Query("SELECT * FROM smartphones ORDER BY isTrending DESC, rating DESC")
  fun getAllSmartphones(): Flow<List<SmartphoneEntity>>

  @Query("SELECT * FROM smartphones WHERE id = :id LIMIT 1")
  fun getSmartphoneById(id: String): Flow<SmartphoneEntity?>

  @Query("SELECT * FROM smartphones WHERE id IN (:ids)")
  fun getSmartphonesByIds(ids: List<String>): Flow<List<SmartphoneEntity>>

  @Query("SELECT * FROM smartphones WHERE isTrending = 1 ORDER BY rating DESC LIMIT 10")
  fun getTrendingSmartphones(): Flow<List<SmartphoneEntity>>

  @Query("SELECT * FROM smartphones WHERE isLatestLaunch = 1 ORDER BY releaseDate DESC LIMIT 10")
  fun getLatestLaunches(): Flow<List<SmartphoneEntity>>

  @Query("SELECT * FROM smartphones WHERE isPopular = 1 ORDER BY rating DESC LIMIT 10")
  fun getPopularSmartphones(): Flow<List<SmartphoneEntity>>

  @Query("""
    SELECT * FROM smartphones 
    WHERE (brand LIKE '%' || :query || '%' 
       OR model LIKE '%' || :query || '%' 
       OR processor LIKE '%' || :query || '%')
    ORDER BY rating DESC
  """)
  fun searchSmartphones(query: String): Flow<List<SmartphoneEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSmartphone(phone: SmartphoneEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSmartphones(phones: List<SmartphoneEntity>)

  @Delete
  suspend fun deleteSmartphone(phone: SmartphoneEntity)

  @Query("DELETE FROM smartphones WHERE id = :id")
  suspend fun deleteSmartphoneById(id: String)

  @Query("UPDATE smartphones SET currentPriceBdt = :newPrice, previousPriceBdt = :prevPrice, lastPriceUpdated = :date WHERE id = :id")
  suspend fun updatePhonePrice(id: String, newPrice: Int, prevPrice: Int, date: String)

  // Price History
  @Query("SELECT * FROM price_history WHERE phoneId = :phoneId ORDER BY id DESC")
  fun getPriceHistory(phoneId: String): Flow<List<PriceHistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPriceHistory(item: PriceHistoryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPriceHistories(items: List<PriceHistoryEntity>)

  // Videos
  @Query("SELECT * FROM videos ORDER BY id ASC")
  fun getAllVideos(): Flow<List<VideoEntity>>

  @Query("SELECT * FROM videos WHERE isShort = 1")
  fun getShorts(): Flow<List<VideoEntity>>

  @Query("SELECT * FROM videos WHERE isShort = 0")
  fun getRegularVideos(): Flow<List<VideoEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideos(videos: List<VideoEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVideo(video: VideoEntity)

  @Query("DELETE FROM videos WHERE id = :id")
  suspend fun deleteVideoById(id: String)

  // Buying Guides
  @Query("SELECT * FROM buying_guides")
  fun getAllGuides(): Flow<List<BuyingGuideEntity>>

  @Query("SELECT * FROM buying_guides WHERE id = :id LIMIT 1")
  fun getGuideById(id: String): Flow<BuyingGuideEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGuides(guides: List<BuyingGuideEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGuide(guide: BuyingGuideEntity)

  @Query("DELETE FROM buying_guides WHERE id = :id")
  suspend fun deleteGuideById(id: String)

  // Saved Items (Bookmarks)
  @Query("SELECT * FROM saved_items ORDER BY savedAtMillis DESC")
  fun getAllSavedItems(): Flow<List<SavedItemEntity>>

  @Query("SELECT * FROM saved_items WHERE itemType = :type AND targetId = :targetId LIMIT 1")
  fun getSavedItem(type: String, targetId: String): Flow<SavedItemEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavedItem(item: SavedItemEntity)

  @Query("DELETE FROM saved_items WHERE itemType = :type AND targetId = :targetId")
  suspend fun deleteSavedItem(type: String, targetId: String)

  // Saved Comparisons
  @Query("SELECT * FROM saved_comparisons ORDER BY savedAtMillis DESC")
  fun getAllSavedComparisons(): Flow<List<SavedComparisonEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavedComparison(comparison: SavedComparisonEntity)

  @Query("DELETE FROM saved_comparisons WHERE id = :id")
  suspend fun deleteSavedComparison(id: Long)

  // News
  @Query("SELECT * FROM news ORDER BY id ASC")
  fun getAllNews(): Flow<List<NewsEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNewsList(news: List<NewsEntity>)

  // Sync Info
  @Query("SELECT * FROM sync_info WHERE id = 1 LIMIT 1")
  fun getSyncInfo(): Flow<SyncInfoEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateSyncInfo(syncInfo: SyncInfoEntity)

  @Query("DELETE FROM saved_items")
  suspend fun clearSavedItems()
}
