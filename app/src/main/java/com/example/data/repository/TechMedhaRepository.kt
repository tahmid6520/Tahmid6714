package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.BuyingGuideEntity
import com.example.data.local.NewsEntity
import com.example.data.local.PriceHistoryEntity
import com.example.data.local.SavedComparisonEntity
import com.example.data.local.SavedItemEntity
import com.example.data.local.SeedData
import com.example.data.local.SmartphoneEntity
import com.example.data.local.SyncInfoEntity
import com.example.data.local.TechMedhaDao
import com.example.data.local.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TechMedhaRepository(private val dao: TechMedhaDao) {

  val allSmartphones: Flow<List<SmartphoneEntity>> = dao.getAllSmartphones()
  val trendingSmartphones: Flow<List<SmartphoneEntity>> = dao.getTrendingSmartphones()
  val latestLaunches: Flow<List<SmartphoneEntity>> = dao.getLatestLaunches()
  val popularSmartphones: Flow<List<SmartphoneEntity>> = dao.getPopularSmartphones()

  val allVideos: Flow<List<VideoEntity>> = dao.getAllVideos()
  val shorts: Flow<List<VideoEntity>> = dao.getShorts()
  val regularVideos: Flow<List<VideoEntity>> = dao.getRegularVideos()

  val allGuides: Flow<List<BuyingGuideEntity>> = dao.getAllGuides()
  val savedItems: Flow<List<SavedItemEntity>> = dao.getAllSavedItems()
  val savedComparisons: Flow<List<SavedComparisonEntity>> = dao.getAllSavedComparisons()
  val allNews: Flow<List<NewsEntity>> = dao.getAllNews()
  val syncInfo: Flow<SyncInfoEntity?> = dao.getSyncInfo()

  suspend fun ensureDatabasePopulated() {
    withContext(Dispatchers.IO) {
      val existing = dao.getAllSmartphones().firstOrNull()
      if (existing.isNullOrEmpty()) {
        dao.insertSmartphones(SeedData.initialPhones)
        dao.insertPriceHistories(SeedData.initialPriceHistories)
        dao.insertVideos(SeedData.initialVideos)
        dao.insertGuides(SeedData.initialGuides)
        dao.insertNewsList(SeedData.initialNews)
        dao.updateSyncInfo(SeedData.initialSyncInfo)
      }
    }
  }

  fun getSmartphoneById(id: String): Flow<SmartphoneEntity?> = dao.getSmartphoneById(id)

  fun getSmartphonesByIds(ids: List<String>): Flow<List<SmartphoneEntity>> = dao.getSmartphonesByIds(ids)

  fun searchSmartphones(query: String): Flow<List<SmartphoneEntity>> = dao.searchSmartphones(query)

  fun getPriceHistory(phoneId: String): Flow<List<PriceHistoryEntity>> = dao.getPriceHistory(phoneId)

  fun getGuideById(id: String): Flow<BuyingGuideEntity?> = dao.getGuideById(id)

  fun isItemSaved(type: String, targetId: String): Flow<SavedItemEntity?> = dao.getSavedItem(type, targetId)

  suspend fun toggleSaveItem(
    type: String,
    targetId: String,
    title: String,
    subtitle: String,
    currentlySaved: Boolean
  ) {
    withContext(Dispatchers.IO) {
      if (currentlySaved) {
        dao.deleteSavedItem(type, targetId)
      } else {
        dao.insertSavedItem(
          SavedItemEntity(
            itemType = type,
            targetId = targetId,
            title = title,
            subtitle = subtitle,
            savedAtMillis = System.currentTimeMillis()
          )
        )
      }
    }
  }

  suspend fun saveComparison(title: String, phoneId1: String, phoneId2: String, phoneId3: String = "") {
    withContext(Dispatchers.IO) {
      dao.insertSavedComparison(
        SavedComparisonEntity(
          title = title,
          phoneId1 = phoneId1,
          phoneId2 = phoneId2,
          phoneId3 = phoneId3,
          savedAtMillis = System.currentTimeMillis()
        )
      )
    }
  }

  suspend fun deleteSavedComparison(id: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteSavedComparison(id)
    }
  }

  suspend fun insertSmartphone(phone: SmartphoneEntity) {
    withContext(Dispatchers.IO) {
      dao.insertSmartphone(phone)
    }
  }

  suspend fun updatePhonePrice(phoneId: String, newPrice: Int, currentPhone: SmartphoneEntity, note: String) {
    withContext(Dispatchers.IO) {
      val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val today = dateFormat.format(Date())
      dao.updatePhonePrice(phoneId, newPrice, currentPhone.currentPriceBdt, today)
      dao.insertPriceHistory(
        PriceHistoryEntity(
          phoneId = phoneId,
          priceBdt = newPrice,
          date = today,
          note = if (note.isNotBlank()) note else "অফিসিয়াল প্রাইস আপডেট"
        )
      )
    }
  }

  suspend fun deleteSmartphone(id: String) {
    withContext(Dispatchers.IO) {
      dao.deleteSmartphoneById(id)
    }
  }

  suspend fun insertVideo(video: VideoEntity) {
    withContext(Dispatchers.IO) {
      dao.insertVideo(video)
    }
  }

  suspend fun deleteVideo(id: String) {
    withContext(Dispatchers.IO) {
      dao.deleteVideoById(id)
    }
  }

  suspend fun insertGuide(guide: BuyingGuideEntity) {
    withContext(Dispatchers.IO) {
      dao.insertGuide(guide)
    }
  }

  suspend fun deleteGuide(id: String) {
    withContext(Dispatchers.IO) {
      dao.deleteGuideById(id)
    }
  }

  suspend fun syncOfflineData(): SyncInfoEntity {
    return withContext(Dispatchers.IO) {
      // Simulate intelligent delta-sync check with server
      val now = System.currentTimeMillis()
      val count = (dao.getAllSmartphones().firstOrNull()?.size ?: 0) +
                  (dao.getAllVideos().firstOrNull()?.size ?: 0) +
                  (dao.getAllGuides().firstOrNull()?.size ?: 0)
      val newSyncInfo = SyncInfoEntity(
        id = 1,
        lastSyncTimestamp = now,
        totalItems = count,
        dbVersion = "v2.5.1-live-sync"
      )
      dao.updateSyncInfo(newSyncInfo)
      newSyncInfo
    }
  }

  suspend fun clearCache() {
    withContext(Dispatchers.IO) {
      dao.clearSavedItems()
    }
  }
}
