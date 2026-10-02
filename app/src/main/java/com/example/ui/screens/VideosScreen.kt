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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.VideoEntity
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun VideosScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()
  val shorts by viewModel.shorts.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var selectedCategory by remember { mutableStateOf("ALL") }

  val categories = listOf(
    "ALL" to Strings.get("tab_all_videos", language),
    "Reviews" to Strings.get("tab_reviews", language),
    "Comparisons" to Strings.get("tab_comparisons", language),
    "Guides" to Strings.get("buying_guides", language),
    "Shorts" to Strings.get("tab_shorts", language)
  )

  val displayVideos = remember(allVideos, selectedCategory) {
    if (selectedCategory == "ALL") {
      allVideos.filter { !it.isShort }
    } else if (selectedCategory == "Shorts") {
      allVideos.filter { it.isShort }
    } else {
      allVideos.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("videos_screen"),
    contentPadding = PaddingValues(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Channel Profile Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(TechBluePrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "TM",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = Strings.get("tech_medha_channel", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = Strings.get("channel_handle", language),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = Strings.get("subscriber_count", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@techmedha.t?sub_confirmation=1"))
                try { context.startActivity(intent) } catch (_: Exception) {}
              },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Default.Subscriptions, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Subscribe", fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { viewModel.openShortsFeed() },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TechBluePrimary),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text(Strings.get("tab_shorts", language), fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 2. Category Filter Chips
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { (id, name) ->
          val isSelected = selectedCategory == id
          FilterChip(
            selected = isSelected,
            onClick = { selectedCategory = id },
            label = { Text(name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TechBluePrimary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }
    }

    // Offline notice info
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ) {
        Text(
          text = "✓ " + Strings.get("play_offline_cached", language),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
      }
    }

    // 3. Videos Feed
    items(displayVideos, key = { it.id }) { video ->
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        FullVideoCard(
          video = video,
          language = language,
          onWatch = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
            try { context.startActivity(intent) } catch (_: Exception) {}
          }
        )
      }
    }
  }
}

@Composable
fun FullVideoCard(
  video: VideoEntity,
  language: AppLanguage,
  onWatch: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onWatch() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
          .background(Color.DarkGray)
      ) {
        AsyncImage(
          model = video.thumbnailUrl,
          contentDescription = video.titleBn,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0x66000000))
              )
            )
        )

        // Center play circle
        Box(
          modifier = Modifier
            .size(52.dp)
            .align(Alignment.Center)
            .clip(CircleShape)
            .background(YouTubeRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
          )
        }

        // Duration badge
        Surface(
          color = Color(0xCC000000),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)
        ) {
          Text(
            text = video.duration,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        // Category Tag
        Surface(
          color = TechBluePrimary,
          shape = RoundedCornerShape(bottomEnd = 8.dp),
          modifier = Modifier.align(Alignment.TopStart)
        ) {
          Text(
            text = video.category,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = if (language == AppLanguage.BENGALI) video.titleBn else video.titleEn,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "@techmedha.t • ${video.views} views • ${video.publishedDate}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Text(
            text = Strings.get("watch_now", language),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
