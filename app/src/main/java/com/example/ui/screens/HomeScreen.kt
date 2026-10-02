package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.BuyingGuideEntity
import com.example.data.local.NewsEntity
import com.example.data.local.SmartphoneEntity
import com.example.data.local.VideoEntity
import com.example.ui.components.PhoneCard
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.TechGold
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.NavTab
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun HomeScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val trendingPhones by viewModel.trendingSmartphones.collectAsStateWithLifecycle()
  val latestPhones by viewModel.latestLaunches.collectAsStateWithLifecycle()
  val popularPhones by viewModel.popularSmartphones.collectAsStateWithLifecycle()
  val regularVideos by viewModel.regularVideos.collectAsStateWithLifecycle()
  val shorts by viewModel.shorts.collectAsStateWithLifecycle()
  val guides by viewModel.allGuides.collectAsStateWithLifecycle()
  val newsList by viewModel.allNews.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen"),
    contentPadding = PaddingValues(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Tech Medha Hero Channel Banner
    item {
      HeroBannerSection(
        language = language,
        onWatchClick = {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@techmedha.t"))
          try { context.startActivity(intent) } catch (_: Exception) {}
        },
        onExploreClick = { viewModel.setTab(NavTab.EXPLORE) }
      )
    }

    // 2. Quick Category Chips
    item {
      QuickCategoriesSection(
        language = language,
        onCategorySelect = { category ->
          viewModel.setTab(NavTab.PHONES)
          viewModel.setSearchQuery(category)
        }
      )
    }

    // 3. Trending Smartphones (Horizontal Carousel)
    if (trendingPhones.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("trending_phones", language),
          language = language,
          onViewAll = { viewModel.setTab(NavTab.PHONES) }
        )
      }
      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(trendingPhones) { phone ->
            TrendingPhoneMiniCard(
              phone = phone,
              language = language,
              onCardClick = { viewModel.selectPhone(phone.id) },
              onCompareClick = { viewModel.toggleComparePhone(phone) }
            )
          }
        }
      }
    }

    // 4. Latest Tech Medha Videos (YouTube Showcase)
    if (regularVideos.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("latest_videos", language),
          language = language,
          onViewAll = { viewModel.setTab(NavTab.VIDEOS) }
        )
      }
      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(regularVideos) { video ->
            VideoCard(
              video = video,
              language = language,
              onPlayClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
                try { context.startActivity(intent) } catch (_: Exception) {}
              }
            )
          }
        }
      }
    }

    // 5. Tech Medha Shorts Carousel
    if (shorts.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("latest_shorts", language),
          language = language,
          onViewAll = { viewModel.openShortsFeed() }
        )
      }
      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(shorts) { shortItem ->
            ShortCard(
              short = shortItem,
              language = language,
              onClick = { viewModel.openShortsFeed() }
            )
          }
        }
      }
    }

    // 6. Latest Smartphone Launches
    if (latestPhones.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("latest_launches", language),
          language = language,
          onViewAll = { viewModel.setTab(NavTab.PHONES) }
        )
      }
      items(latestPhones.take(3)) { phone ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          PhoneCard(
            phone = phone,
            language = language,
            isSaved = viewModel.isItemSaved("PHONE", phone.id),
            isInCompare = viewModel.isPhoneInCompare(phone.id),
            onCardClick = { viewModel.selectPhone(phone.id) },
            onSaveClick = {
              val current = viewModel.isItemSaved("PHONE", phone.id)
              viewModel.toggleSavePhone(phone, current)
            },
            onCompareClick = { viewModel.toggleComparePhone(phone) }
          )
        }
      }
    }

    // 7. Recommended Comparisons Action Banner
    item {
      RecommendedComparisonBanner(
        language = language,
        onCompareClick = {
          viewModel.openCompareScreen()
        }
      )
    }

    // 8. Offline Buying Guides
    if (guides.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("buying_guides", language),
          language = language,
          onViewAll = { viewModel.setTab(NavTab.EXPLORE) }
        )
      }
      items(guides.take(2)) { guide ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          BuyingGuideHomeCard(
            guide = guide,
            language = language,
            onClick = { viewModel.selectGuide(guide.id) }
          )
        }
      }
    }

    // 9. Latest Tech News
    if (newsList.isNotEmpty()) {
      item {
        SectionHeader(
          title = Strings.get("tech_news", language),
          language = language,
          onViewAll = null
        )
      }
      items(newsList) { news ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          NewsCard(news = news, language = language)
        }
      }
    }
  }
}

@Composable
fun HeroBannerSection(
  language: AppLanguage,
  onWatchClick: () -> Unit,
  onExploreClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(Color(0xFF071B38), Color(0xFF0F62FE), Color(0xFF003884))
        )
      )
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Surface(
          shape = CircleShape,
          color = Color(0x33FFFFFF)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(TechCyanAccent)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "@techmedha.t",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = YouTubeRed
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "YouTube",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = if (language == AppLanguage.BENGALI) "টেক মেধা স্মার্টফোন হাব" else "Tech Medha Smartphone Hub",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = Strings.get("app_tagline", language),
        fontSize = 13.sp,
        color = Color(0xFFDCE8FF),
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
          onClick = onWatchClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color(0xFF0F62FE),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = Strings.get("watch_now", language),
            color = Color(0xFF0F62FE),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        OutlinedButton(
          onClick = onExploreClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
          Text(
            text = Strings.get("nav_explore", language),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}

@Composable
fun QuickCategoriesSection(
  language: AppLanguage,
  onCategorySelect: (String) -> Unit
) {
  val categories = listOf(
    "Flagship" to Strings.get("cat_flagship", language),
    "Midrange" to Strings.get("cat_midrange", language),
    "Budget" to Strings.get("cat_budget", language),
    "Gaming" to Strings.get("cat_gaming", language),
    "Camera" to Strings.get("cat_camera", language),
    "5G" to Strings.get("cat_5g", language)
  )

  Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    Text(
      text = Strings.get("quick_categories", language),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(categories) { (id, name) ->
        FilterChip(
          selected = false,
          onClick = { onCategorySelect(id) },
          label = { Text(name, fontWeight = FontWeight.SemiBold) },
          colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
          )
        )
      }
    }
  }
}

@Composable
fun TrendingPhoneMiniCard(
  phone: SmartphoneEntity,
  language: AppLanguage,
  onCardClick: () -> Unit,
  onCompareClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(170.dp)
      .clickable { onCardClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        AsyncImage(
          model = phone.imageUrl,
          contentDescription = phone.model,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
        Surface(
          color = TechBluePrimary,
          shape = RoundedCornerShape(bottomEnd = 8.dp),
          modifier = Modifier.align(Alignment.TopStart)
        ) {
          Text(
            text = "★ ${phone.rating}",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = phone.brand.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = phone.model,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "৳ ${String.format("%,d", phone.currentPriceBdt)}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = phone.processor.take(20),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1
      )
    }
  }
}

@Composable
fun VideoCard(
  video: VideoEntity,
  language: AppLanguage,
  onPlayClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(260.dp)
      .clickable { onPlayClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(135.dp)
          .background(Color.DarkGray)
      ) {
        AsyncImage(
          model = video.thumbnailUrl,
          contentDescription = video.titleBn,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Duration badge
        Surface(
          color = Color(0xCC000000),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
        ) {
          Text(
            text = video.duration,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Play Button Center
        Box(
          modifier = Modifier
            .size(42.dp)
            .align(Alignment.Center)
            .clip(CircleShape)
            .background(YouTubeRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = if (language == AppLanguage.BENGALI) video.titleBn else video.titleEn,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Tech Medha • ${video.views}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = video.publishedDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun ShortCard(
  short: VideoEntity,
  language: AppLanguage,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(130.dp)
      .height(200.dp)
      .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AsyncImage(
        model = short.thumbnailUrl,
        contentDescription = short.titleBn,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color(0xDD000000))
            )
          )
      )
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(8.dp)
      ) {
        Surface(
          color = YouTubeRed,
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = "SHORTS",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (language == AppLanguage.BENGALI) short.titleBn else short.titleEn,
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@Composable
fun RecommendedComparisonBanner(
  language: AppLanguage,
  onCompareClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clickable { onCompareClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(TechBluePrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CompareArrows,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = Strings.get("recommended_comparisons", language),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = if (language == AppLanguage.BENGALI) "যেকোনো ২টি ফোনের নিখুঁত কারিগরি পার্থক্য দেখুন" else "Compare any 2 smartphones side by side",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
      )
    }
  }
}

@Composable
fun BuyingGuideHomeCard(
  guide: BuyingGuideEntity,
  language: AppLanguage,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        AsyncImage(
          model = guide.imageUrl,
          contentDescription = null,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = guide.category,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (language == AppLanguage.BENGALI) guide.titleBn else guide.titleEn,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${guide.readTimeMinutes} min read • Tech Medha",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun NewsCard(
  news: NewsEntity,
  language: AppLanguage
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = news.source,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = news.date,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = if (language == AppLanguage.BENGALI) news.titleBn else news.titleEn,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = if (language == AppLanguage.BENGALI) news.snippetBn else news.snippetEn,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  language: AppLanguage,
  onViewAll: (() -> Unit)?
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )
    if (onViewAll != null) {
      Text(
        text = Strings.get("view_all", language),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onViewAll() }
      )
    }
  }
}
