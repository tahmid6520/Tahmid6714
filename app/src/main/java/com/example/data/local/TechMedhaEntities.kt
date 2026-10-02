package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smartphones")
data class SmartphoneEntity(
  @PrimaryKey val id: String,
  val brand: String,
  val model: String,
  val imageUrl: String,
  val releaseDate: String,
  val currentPriceBdt: Int,
  val previousPriceBdt: Int,
  val lastPriceUpdated: String,
  val displaySize: String,
  val displayResolution: String,
  val refreshRate: String,
  val displayPanel: String,
  val processor: String,
  val gpu: String,
  val ram: String,
  val storage: String,
  val expandableStorage: Boolean,
  val os: String,
  val mainCamera: String,
  val ultrawideCamera: String,
  val telephotoCamera: String,
  val frontCamera: String,
  val videoRecording: String,
  val batteryMah: Int,
  val chargingSpeedWatt: Int,
  val wirelessCharging: Boolean,
  val network5g: Boolean,
  val simInfo: String,
  val wifi: String,
  val bluetooth: String,
  val nfc: Boolean,
  val usbType: String,
  val waterResistance: String,
  val weightGrams: Int,
  val dimensions: String,
  val colors: String,
  val pros: String, // Comma separated
  val cons: String, // Comma separated
  val reviewBn: String,
  val reviewEn: String,
  val relatedVideoId: String,
  val isTrending: Boolean,
  val isLatestLaunch: Boolean,
  val isPopular: Boolean,
  val rating: Float,
  val category: String
)

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val phoneId: String,
  val priceBdt: Int,
  val date: String,
  val note: String
)

@Entity(tableName = "videos")
data class VideoEntity(
  @PrimaryKey val id: String,
  val titleBn: String,
  val titleEn: String,
  val youtubeUrl: String,
  val videoId: String,
  val thumbnailUrl: String,
  val category: String,
  val duration: String,
  val views: String,
  val publishedDate: String,
  val isShort: Boolean
)

@Entity(tableName = "buying_guides")
data class BuyingGuideEntity(
  @PrimaryKey val id: String,
  val titleBn: String,
  val titleEn: String,
  val summaryBn: String,
  val summaryEn: String,
  val contentBn: String,
  val contentEn: String,
  val category: String,
  val recommendedPhoneIds: String, // comma separated IDs
  val readTimeMinutes: Int,
  val imageUrl: String
)

@Entity(tableName = "saved_items")
data class SavedItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val itemType: String, // PHONE, COMPARISON, GUIDE, VIDEO
  val targetId: String,
  val title: String,
  val subtitle: String,
  val savedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_comparisons")
data class SavedComparisonEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val phoneId1: String,
  val phoneId2: String,
  val phoneId3: String = "",
  val savedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "news")
data class NewsEntity(
  @PrimaryKey val id: String,
  val titleBn: String,
  val titleEn: String,
  val snippetBn: String,
  val snippetEn: String,
  val date: String,
  val source: String
)

@Entity(tableName = "sync_info")
data class SyncInfoEntity(
  @PrimaryKey val id: Int = 1,
  val lastSyncTimestamp: Long,
  val totalItems: Int,
  val dbVersion: String
)
