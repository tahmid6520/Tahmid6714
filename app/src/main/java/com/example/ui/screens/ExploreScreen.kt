package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.TechGold
import com.example.ui.viewmodel.NavTab
import com.example.ui.viewmodel.TechMedhaViewModel

data class ExploreItem(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val icon: ImageVector,
  val iconBg: Color
)

@Composable
fun ExploreScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val guides by viewModel.allGuides.collectAsStateWithLifecycle()
  val newsList by viewModel.allNews.collectAsStateWithLifecycle()

  val exploreItems = listOf(
    ExploreItem(
      id = "COMPARE",
      titleBn = Strings.get("compare_title", language),
      titleEn = Strings.get("compare_title", language),
      subtitleBn = "২ বা ৩টি ফোনের নিরপেক্ষ তুলনা",
      subtitleEn = "Factual side-by-side comparison",
      icon = Icons.Default.CompareArrows,
      iconBg = TechBluePrimary
    ),
    ExploreItem(
      id = "FINDER",
      titleBn = Strings.get("finder_title", language),
      titleEn = Strings.get("finder_title", language),
      subtitleBn = "বাজেট ও ব্যবহারে সেরা ফোন খুঁজুন",
      subtitleEn = "Tailored smartphone recommendations",
      icon = Icons.Default.SavedSearch,
      iconBg = Color(0xFF8B5CF6)
    ),
    ExploreItem(
      id = "TOOLKIT",
      titleBn = Strings.get("toolkit_title", language),
      titleEn = Strings.get("toolkit_title", language),
      subtitleBn = "স্টোরেজ ও চার্জিং ক্যালকুলেটর",
      subtitleEn = "Storage & charging calculators",
      icon = Icons.Default.Build,
      iconBg = Color(0xFF0284C7)
    ),
    ExploreItem(
      id = "SAVED",
      titleBn = Strings.get("saved_items", language),
      titleEn = Strings.get("saved_items", language),
      subtitleBn = "অফলাইনে সংরক্ষিত কনটেন্ট",
      subtitleEn = "Access saved offline content",
      icon = Icons.Default.Star,
      iconBg = TechGold
    )
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("explore_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header
    item {
      Text(
        text = Strings.get("nav_explore", language),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = if (language == AppLanguage.BENGALI) "প্রয়োজনীয় টুলস, বায়িং গাইড ও স্মার্টফোন সার্চ" else "Explore tools, buying guides, and comparisons",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Grid of Primary Features
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        exploreItems.chunked(2).forEach { rowItems ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            rowItems.forEach { item ->
              Card(
                modifier = Modifier
                  .weight(1f)
                  .clickable {
                    when (item.id) {
                      "COMPARE" -> viewModel.openCompareScreen()
                      "FINDER" -> viewModel.openPhoneFinder()
                      "TOOLKIT" -> viewModel.openToolkit()
                      "SAVED" -> viewModel.setTab(NavTab.PROFILE)
                    }
                  }
                  .testTag("explore_item_${item.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Box(
                    modifier = Modifier
                      .size(46.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .background(item.iconBg),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = item.icon,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(26.dp)
                    )
                  }
                  Spacer(modifier = Modifier.height(14.dp))
                  Text(
                    text = if (language == AppLanguage.BENGALI) item.titleBn else item.titleEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = if (language == AppLanguage.BENGALI) item.subtitleBn else item.subtitleEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                  )
                }
              }
            }
          }
        }
      }
    }

    // Buying Guides List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = Strings.get("buying_guides", language),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
    }

    items(guides) { guide ->
      BuyingGuideHomeCard(
        guide = guide,
        language = language,
        onClick = { viewModel.selectGuide(guide.id) }
      )
    }

    // Latest Tech News
    item {
      Text(
        text = Strings.get("tech_news", language),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    }

    items(newsList) { news ->
      NewsCard(news = news, language = language)
    }
  }
}
