package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BuyingGuideEntity
import com.example.data.local.NewsEntity
import com.example.data.local.PriceHistoryEntity
import com.example.data.local.SavedComparisonEntity
import com.example.data.local.SavedItemEntity
import com.example.data.local.SmartphoneEntity
import com.example.data.local.SyncInfoEntity
import com.example.data.local.VideoEntity
import com.example.data.repository.TechMedhaRepository
import com.example.ui.language.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavTab {
  HOME, PHONES, VIDEOS, EXPLORE, PROFILE
}

class TechMedhaViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: TechMedhaRepository

  init {
    val database = AppDatabase.getDatabase(application, viewModelScope)
    repository = TechMedhaRepository(database.techMedhaDao())
    viewModelScope.launch {
      repository.ensureDatabasePopulated()
    }
  }

  // Language & Theme State
  private val _language = MutableStateFlow(AppLanguage.BENGALI)
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  private val _themeMode = MutableStateFlow("SYSTEM") // SYSTEM, LIGHT, DARK
  val themeMode: StateFlow<String> = _themeMode.asStateFlow()

  // Navigation Sub-Screen State
  private val _currentTab = MutableStateFlow(NavTab.HOME)
  val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

  private val _selectedPhoneId = MutableStateFlow<String?>(null)
  val selectedPhoneId: StateFlow<String?> = _selectedPhoneId.asStateFlow()

  private val _selectedGuideId = MutableStateFlow<String?>(null)
  val selectedGuideId: StateFlow<String?> = _selectedGuideId.asStateFlow()

  private val _showCompareScreen = MutableStateFlow(false)
  val showCompareScreen: StateFlow<Boolean> = _showCompareScreen.asStateFlow()

  private val _showPhoneFinderScreen = MutableStateFlow(false)
  val showPhoneFinderScreen: StateFlow<Boolean> = _showPhoneFinderScreen.asStateFlow()

  private val _showToolkitScreen = MutableStateFlow(false)
  val showToolkitScreen: StateFlow<Boolean> = _showToolkitScreen.asStateFlow()

  private val _activeTool = MutableStateFlow<String?>("STORAGE")
  val activeTool: StateFlow<String?> = _activeTool.asStateFlow()

  private val _showAdminScreen = MutableStateFlow(false)
  val showAdminScreen: StateFlow<Boolean> = _showAdminScreen.asStateFlow()

  private val _showShortsFeed = MutableStateFlow(false)
  val showShortsFeed: StateFlow<Boolean> = _showShortsFeed.asStateFlow()

  // Comparison Tray (holds up to 3 phones)
  private val _comparisonTray = MutableStateFlow<List<SmartphoneEntity>>(emptyList())
  val comparisonTray: StateFlow<List<SmartphoneEntity>> = _comparisonTray.asStateFlow()

  // Search & Filters State
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedBrand = MutableStateFlow("ALL")
  val selectedBrand: StateFlow<String> = _selectedBrand.asStateFlow()

  private val _priceRange = MutableStateFlow(0 to 250000)
  val priceRange: StateFlow<Pair<Int, Int>> = _priceRange.asStateFlow()

  private val _filter5gOnly = MutableStateFlow(false)
  val filter5gOnly: StateFlow<Boolean> = _filter5gOnly.asStateFlow()

  private val _sortBy = MutableStateFlow("RATING") // RATING, PRICE_ASC, PRICE_DESC, NEWEST
  val sortBy: StateFlow<String> = _sortBy.asStateFlow()

  // Repository Flows
  val allSmartphones: StateFlow<List<SmartphoneEntity>> = repository.allSmartphones
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val trendingSmartphones: StateFlow<List<SmartphoneEntity>> = repository.trendingSmartphones
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val latestLaunches: StateFlow<List<SmartphoneEntity>> = repository.latestLaunches
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val popularSmartphones: StateFlow<List<SmartphoneEntity>> = repository.popularSmartphones
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allVideos: StateFlow<List<VideoEntity>> = repository.allVideos
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val shorts: StateFlow<List<VideoEntity>> = repository.shorts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val regularVideos: StateFlow<List<VideoEntity>> = repository.regularVideos
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allGuides: StateFlow<List<BuyingGuideEntity>> = repository.allGuides
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val savedItems: StateFlow<List<SavedItemEntity>> = repository.savedItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val savedComparisons: StateFlow<List<SavedComparisonEntity>> = repository.savedComparisons
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allNews: StateFlow<List<NewsEntity>> = repository.allNews
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val syncInfo: StateFlow<SyncInfoEntity?> = repository.syncInfo
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Filtered Smartphones List (Offline computation)
  val filteredSmartphones: StateFlow<List<SmartphoneEntity>> = combine(
    combine(allSmartphones, searchQuery, selectedBrand) { phones, query, brand ->
      Triple(phones, query, brand)
    },
    combine(priceRange, filter5gOnly, sortBy) { range, only5g, sort ->
      Triple(range, only5g, sort)
    }
  ) { (phones, query, brand), (range, only5g, sort) ->
    phones.filter { phone ->
      val matchesQuery = query.isBlank() ||
          phone.brand.contains(query, ignoreCase = true) ||
          phone.model.contains(query, ignoreCase = true) ||
          phone.processor.contains(query, ignoreCase = true) ||
          phone.category.contains(query, ignoreCase = true)

      val matchesBrand = brand == "ALL" || phone.brand.equals(brand, ignoreCase = true)
      val matchesPrice = phone.currentPriceBdt in range.first..range.second
      val matches5g = !only5g || phone.network5g

      matchesQuery && matchesBrand && matchesPrice && matches5g
    }.let { list ->
      when (sort) {
        "PRICE_ASC" -> list.sortedBy { it.currentPriceBdt }
        "PRICE_DESC" -> list.sortedByDescending { it.currentPriceBdt }
        "NEWEST" -> list.sortedByDescending { it.releaseDate }
        else -> list.sortedByDescending { it.rating }
      }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Phone Finder State
  val finderBudget = MutableStateFlow(35000)
  val finderUsage = MutableStateFlow("GAMING") // GAMING, CAMERA, BATTERY, STUDENT, EVERYDAY
  val finderRequire5g = MutableStateFlow(true)
  val finderSubmitted = MutableStateFlow(false)

  // Sync state
  private val _isSyncing = MutableStateFlow(false)
  val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

  private val _syncMessage = MutableStateFlow<String?>(null)
  val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

  // Notification toggles
  val notifVideos = MutableStateFlow(true)
  val notifPrice = MutableStateFlow(true)
  val notifGuides = MutableStateFlow(true)
  val notifNews = MutableStateFlow(true)

  // Actions
  fun setLanguage(lang: AppLanguage) {
    _language.value = lang
  }

  fun setThemeMode(mode: String) {
    _themeMode.value = mode
  }

  fun setTab(tab: NavTab) {
    _currentTab.value = tab
    // Close secondary screens on primary tab switch
    _selectedPhoneId.value = null
    _selectedGuideId.value = null
    _showCompareScreen.value = false
    _showPhoneFinderScreen.value = false
    _showToolkitScreen.value = false
    _showAdminScreen.value = false
    _showShortsFeed.value = false
  }

  fun selectPhone(id: String?) {
    _selectedPhoneId.value = id
  }

  fun selectGuide(id: String?) {
    _selectedGuideId.value = id
  }

  fun openCompareScreen() {
    _showCompareScreen.value = true
  }

  fun closeCompareScreen() {
    _showCompareScreen.value = false
  }

  fun openPhoneFinder() {
    _showPhoneFinderScreen.value = true
    finderSubmitted.value = false
  }

  fun closePhoneFinder() {
    _showPhoneFinderScreen.value = false
  }

  fun openToolkit(toolName: String = "STORAGE") {
    _activeTool.value = toolName
    _showToolkitScreen.value = true
  }

  fun closeToolkit() {
    _showToolkitScreen.value = false
  }

  fun setActiveTool(toolName: String) {
    _activeTool.value = toolName
  }

  fun openAdminScreen() {
    _showAdminScreen.value = true
  }

  fun closeAdminScreen() {
    _showAdminScreen.value = false
  }

  fun openShortsFeed() {
    _showShortsFeed.value = true
  }

  fun closeShortsFeed() {
    _showShortsFeed.value = false
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setBrandFilter(brand: String) {
    _selectedBrand.value = brand
  }

  fun setPriceFilter(range: Pair<Int, Int>) {
    _priceRange.value = range
  }

  fun toggle5gFilter() {
    _filter5gOnly.value = !_filter5gOnly.value
  }

  fun setSortBy(sort: String) {
    _sortBy.value = sort
  }

  fun resetFilters() {
    _searchQuery.value = ""
    _selectedBrand.value = "ALL"
    _priceRange.value = 0 to 250000
    _filter5gOnly.value = false
    _sortBy.value = "RATING"
  }

  // Comparison tray actions
  fun toggleComparePhone(phone: SmartphoneEntity) {
    val current = _comparisonTray.value.toMutableList()
    val exists = current.any { it.id == phone.id }
    if (exists) {
      current.removeAll { it.id == phone.id }
    } else {
      if (current.size < 3) {
        current.add(phone)
      } else {
        current.removeAt(0)
        current.add(phone)
      }
    }
    _comparisonTray.value = current
  }

  fun removeComparePhone(id: String) {
    _comparisonTray.value = _comparisonTray.value.filter { it.id != id }
  }

  fun setCompareSlot(index: Int, phone: SmartphoneEntity) {
    val current = _comparisonTray.value.toMutableList()
    if (index < current.size) {
      current[index] = phone
    } else {
      current.add(phone)
    }
    _comparisonTray.value = current
  }

  fun isPhoneInCompare(id: String): Boolean {
    return _comparisonTray.value.any { it.id == id }
  }

  fun saveCurrentComparison(title: String) {
    val phones = _comparisonTray.value
    if (phones.size >= 2) {
      viewModelScope.launch {
        repository.saveComparison(
          title = if (title.isNotBlank()) title else "${phones[0].model} vs ${phones[1].model}",
          phoneId1 = phones[0].id,
          phoneId2 = phones[1].id,
          phoneId3 = if (phones.size > 2) phones[2].id else ""
        )
      }
    }
  }

  fun deleteSavedComparison(id: Long) {
    viewModelScope.launch {
      repository.deleteSavedComparison(id)
    }
  }

  // Bookmarking / Saving
  fun toggleSavePhone(phone: SmartphoneEntity, currentlySaved: Boolean) {
    viewModelScope.launch {
      repository.toggleSaveItem(
        type = "PHONE",
        targetId = phone.id,
        title = "${phone.brand} ${phone.model}",
        subtitle = "৳${phone.currentPriceBdt} | ${phone.processor}",
        currentlySaved = currentlySaved
      )
    }
  }

  fun toggleSaveGuide(guide: BuyingGuideEntity, currentlySaved: Boolean) {
    viewModelScope.launch {
      repository.toggleSaveItem(
        type = "GUIDE",
        targetId = guide.id,
        title = guide.titleBn,
        subtitle = "${guide.readTimeMinutes} min read | ${guide.category}",
        currentlySaved = currentlySaved
      )
    }
  }

  fun toggleSaveVideo(video: VideoEntity, currentlySaved: Boolean) {
    viewModelScope.launch {
      repository.toggleSaveItem(
        type = "VIDEO",
        targetId = video.id,
        title = video.titleBn,
        subtitle = "${video.category} • ${video.duration}",
        currentlySaved = currentlySaved
      )
    }
  }

  fun isItemSaved(type: String, targetId: String): Boolean {
    return savedItems.value.any { it.itemType == type && it.targetId == targetId }
  }

  fun getPriceHistory(phoneId: String): StateFlow<List<PriceHistoryEntity>> {
    return repository.getPriceHistory(phoneId)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  }

  // Admin Mutations
  fun addSmartphone(phone: SmartphoneEntity) {
    viewModelScope.launch {
      repository.insertSmartphone(phone)
    }
  }

  fun updateSmartphonePrice(phoneId: String, newPrice: Int, currentPhone: SmartphoneEntity, note: String) {
    viewModelScope.launch {
      repository.updatePhonePrice(phoneId, newPrice, currentPhone, note)
    }
  }

  fun deleteSmartphone(id: String) {
    viewModelScope.launch {
      repository.deleteSmartphone(id)
      if (_selectedPhoneId.value == id) {
        _selectedPhoneId.value = null
      }
      _comparisonTray.value = _comparisonTray.value.filter { it.id != id }
    }
  }

  fun addVideo(video: VideoEntity) {
    viewModelScope.launch {
      repository.insertVideo(video)
    }
  }

  fun deleteVideo(id: String) {
    viewModelScope.launch {
      repository.deleteVideo(id)
    }
  }

  fun addGuide(guide: BuyingGuideEntity) {
    viewModelScope.launch {
      repository.insertGuide(guide)
    }
  }

  fun deleteGuide(id: String) {
    viewModelScope.launch {
      repository.deleteGuide(id)
    }
  }

  // Sync Manager
  fun triggerSync() {
    viewModelScope.launch {
      _isSyncing.value = true
      _syncMessage.value = null
      kotlinx.coroutines.delay(1200) // Realistic network handshake simulation
      repository.syncOfflineData()
      _isSyncing.value = false
      _syncMessage.value = if (_language.value == AppLanguage.BENGALI) {
        "অফলাইন ডেটাবেস সফলভাবে হালনাগাদ করা হয়েছে!"
      } else {
        "Offline database synchronized successfully!"
      }
    }
  }

  fun clearCache() {
    viewModelScope.launch {
      repository.clearCache()
    }
  }
}
