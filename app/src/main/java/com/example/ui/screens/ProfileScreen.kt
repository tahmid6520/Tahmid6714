package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.TechMedhaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
  val savedItems by viewModel.savedItems.collectAsStateWithLifecycle()
  val savedComparisons by viewModel.savedComparisons.collectAsStateWithLifecycle()
  val syncInfo by viewModel.syncInfo.collectAsStateWithLifecycle()
  val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
  val syncMessage by viewModel.syncMessage.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var savedTab by remember { mutableIntStateOf(0) }
  var autoSync by remember { mutableStateOf(true) }
  var wifiOnly by remember { mutableStateOf(false) }

  val savedTabs = listOf(
    Strings.get("tab_saved_phones", language),
    Strings.get("tab_saved_comparisons", language),
    Strings.get("tab_saved_guides", language),
    Strings.get("tab_saved_videos", language)
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. User / Brand Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(56.dp)
              .clip(CircleShape)
              .background(TechBluePrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "TM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = Strings.get("app_name", language),
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "@techmedha.t • YouTube Hub",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.SemiBold
            )
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = PriceDropGreen.copy(alpha = 0.2f)
          ) {
            Text(
              text = "OFFLINE OK",
              color = PriceDropGreen,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // 2. Saved Items Section
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = Strings.get("saved_items", language),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))
          TabRow(selectedTabIndex = savedTab) {
            savedTabs.forEachIndexed { index, tabTitle ->
              Tab(
                selected = savedTab == index,
                onClick = { savedTab = index },
                text = { Text(tabTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
              )
            }
          }
          Spacer(modifier = Modifier.height(12.dp))

          when (savedTab) {
            0 -> { // Saved Phones
              val phoneItems = savedItems.filter { it.itemType == "PHONE" }
              if (phoneItems.isEmpty()) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "কোনো সংরক্ষিত স্মার্টফোন নেই" else "No saved smartphones yet",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              } else {
                phoneItems.forEach { item ->
                  SavedItemRow(
                    title = item.title,
                    subtitle = item.subtitle,
                    onOpen = { viewModel.selectPhone(item.targetId) },
                    onDelete = { viewModel.toggleSavePhone(viewModel.allSmartphones.value.first { it.id == item.targetId }, true) }
                  )
                }
              }
            }
            1 -> { // Saved Comparisons
              if (savedComparisons.isEmpty()) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "কোনো সংরক্ষিত তুলনা নেই" else "No saved comparisons yet",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              } else {
                savedComparisons.forEach { comp ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(
                      modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.openCompareScreen() }
                    ) {
                      Text(text = comp.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                      Text(text = "Tap to open comparison", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { viewModel.deleteSavedComparison(comp.id) }) {
                      Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(18.dp))
                    }
                  }
                  HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
              }
            }
            2 -> { // Saved Guides
              val guideItems = savedItems.filter { it.itemType == "GUIDE" }
              if (guideItems.isEmpty()) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "কোনো সংরক্ষিত গাইড নেই" else "No saved guides yet",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              } else {
                guideItems.forEach { item ->
                  SavedItemRow(
                    title = item.title,
                    subtitle = item.subtitle,
                    onOpen = { viewModel.selectGuide(item.targetId) },
                    onDelete = {
                      val guide = viewModel.allGuides.value.firstOrNull { it.id == item.targetId }
                      if (guide != null) viewModel.toggleSaveGuide(guide, true)
                    }
                  )
                }
              }
            }
            3 -> { // Saved Videos
              val videoItems = savedItems.filter { it.itemType == "VIDEO" }
              if (videoItems.isEmpty()) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "কোনো সংরক্ষিত ভিডিও নেই" else "No saved videos yet",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              } else {
                videoItems.forEach { item ->
                  SavedItemRow(
                    title = item.title,
                    subtitle = item.subtitle,
                    onOpen = {
                      val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@techmedha.t"))
                      try { context.startActivity(intent) } catch (_: Exception) {}
                    },
                    onDelete = {
                      val video = viewModel.allVideos.value.firstOrNull { it.id == item.targetId }
                      if (video != null) viewModel.toggleSaveVideo(video, true)
                    }
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Offline Sync Manager
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, tint = TechBluePrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = Strings.get("offline_manager", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }

            if (isSyncing) {
              CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          val formattedDate = syncInfo?.lastSyncTimestamp?.let {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(it))
          } ?: "Just now"

          Text(
            text = "${Strings.get("last_synced", language)} $formattedDate",
            style = MaterialTheme.typography.bodySmall
          )
          Text(
            text = "${Strings.get("database_size", language)} ${syncInfo?.totalItems ?: 26} items (~2.4 MB)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          if (syncMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = syncMessage ?: "",
              color = PriceDropGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = Strings.get("auto_sync", language), style = MaterialTheme.typography.bodyMedium)
            Switch(checked = autoSync, onCheckedChange = { autoSync = it })
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = Strings.get("wifi_only_sync", language), style = MaterialTheme.typography.bodyMedium)
            Switch(checked = wifiOnly, onCheckedChange = { wifiOnly = it })
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
              onClick = { viewModel.triggerSync() },
              enabled = !isSyncing,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isSyncing) Strings.get("syncing", language) else Strings.get("sync_now", language),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }

            OutlinedButton(
              onClick = { viewModel.clearCache() },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(text = Strings.get("clear_cache", language), fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 4. Language Settings
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = TechBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Strings.get("language", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { viewModel.setLanguage(AppLanguage.BENGALI) },
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = language == AppLanguage.BENGALI,
              onClick = { viewModel.setLanguage(AppLanguage.BENGALI) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "বাংলা (Bengali - প্রাথমিক ভাষা)", fontWeight = FontWeight.SemiBold)
          }

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { viewModel.setLanguage(AppLanguage.ENGLISH) },
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = language == AppLanguage.ENGLISH,
              onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "English (Secondary Language)", fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }

    // 5. Theme Settings (Light / Dark / System)
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = TechBluePrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Strings.get("theme_mode", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterChip(
              selected = themeMode == "SYSTEM",
              onClick = { viewModel.setThemeMode("SYSTEM") },
              label = { Text(Strings.get("theme_system", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            FilterChip(
              selected = themeMode == "LIGHT",
              onClick = { viewModel.setThemeMode("LIGHT") },
              label = { Text(Strings.get("theme_light", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            FilterChip(
              selected = themeMode == "DARK",
              onClick = { viewModel.setThemeMode("DARK") },
              label = { Text(Strings.get("theme_dark", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
          }
        }
      }
    }

    // 6. Admin Panel Button
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { viewModel.openAdminScreen() }
          .testTag("admin_panel_tile"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = TechBluePrimary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(text = Strings.get("admin_panel", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
              Text(text = Strings.get("admin_subtitle", language), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }

    // 7. About Tech Medha
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = Strings.get("about_tech_medha", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = Strings.get("about_desc", language),
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@techmedha.t"))
              try { context.startActivity(intent) } catch (_: Exception) {}
            },
            colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Subscriptions, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Tech Medha @ YouTube", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun SavedItemRow(
  title: String,
  subtitle: String,
  onOpen: () -> Unit,
  onDelete: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable { onOpen() }
    ) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
      Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
    }
    IconButton(onClick = onDelete) {
      Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(18.dp))
    }
  }
  HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
}
