package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun ShortsFeedScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.closeShortsFeed()
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val shorts by viewModel.shorts.collectAsStateWithLifecycle()
  val context = LocalContext.current

  var currentIndex by remember { mutableIntStateOf(0) }
  var isLiked by remember { mutableStateOf(false) }

  if (shorts.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color.Black),
      contentAlignment = Alignment.Center
    ) {
      Text("No shorts available offline", color = Color.White)
    }
    return
  }

  val activeShort = shorts[currentIndex.coerceIn(0, shorts.size - 1)]

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
      .testTag("shorts_feed_screen")
      .pointerInput(Unit) {
        detectVerticalDragGestures { _, dragAmount ->
          if (dragAmount < -50) {
            // Drag up -> next
            if (currentIndex < shorts.size - 1) {
              currentIndex++
              isLiked = false
            }
          } else if (dragAmount > 50) {
            // Drag down -> previous
            if (currentIndex > 0) {
              currentIndex--
              isLiked = false
            }
          }
        }
      }
  ) {
    // Background Video Thumbnail Image
    AsyncImage(
      model = activeShort.thumbnailUrl,
      contentDescription = activeShort.titleBn,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop
    )

    // Dark Gradient Overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0x99000000),
              Color.Transparent,
              Color(0xDD000000)
            )
          )
        )
    )

    // Top Controls
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .align(Alignment.TopStart),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = { viewModel.closeShortsFeed() },
        modifier = Modifier
          .clip(CircleShape)
          .background(Color(0x66000000))
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White
        )
      }

      Surface(
        color = YouTubeRed,
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "Tech Medha Shorts",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
      }
    }

    // Right Side Interaction Column (Like, Save, Share, Up, Down)
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 90.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Like Action
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
          onClick = { isLiked = !isLiked },
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Like",
            tint = if (isLiked) Color.Red else Color.White
          )
        }
        Text(
          text = if (isLiked) "Liked" else "Like",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      // Share Action
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
          onClick = {
            val shareIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, "Watch Tech Medha Short: ${activeShort.titleBn} \n${activeShort.youtubeUrl}")
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
          },
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
        Text("Share", color = Color.White, fontSize = 11.sp)
      }

      // Prev Short Button
      if (currentIndex > 0) {
        IconButton(
          onClick = { currentIndex--; isLiked = false },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
        ) {
          Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Previous", tint = Color.White)
        }
      }

      // Next Short Button
      if (currentIndex < shorts.size - 1) {
        IconButton(
          onClick = { currentIndex++; isLiked = false },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
        ) {
          Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Next", tint = Color.White)
        }
      }
    }

    // Bottom Info Overlay
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth(0.8f)
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(TechBluePrimary),
          contentAlignment = Alignment.Center
        ) {
          Text("TM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "@techmedha.t",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = if (language == AppLanguage.BENGALI) activeShort.titleBn else activeShort.titleEn,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeShort.youtubeUrl))
          try { context.startActivity(intent) } catch (_: Exception) {}
        },
        colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (language == AppLanguage.BENGALI) "ইউটিউবে দেখুন" else "Play on YouTube",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      }
    }
  }
}
