package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.components.PhoneCard
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun BuyingGuideScreen(
  guideId: String,
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.selectGuide(null)
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val guides by viewModel.allGuides.collectAsStateWithLifecycle()
  val allPhones by viewModel.allSmartphones.collectAsStateWithLifecycle()

  val guide = guides.firstOrNull { it.id == guideId }

  if (guide == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Guide not found")
    }
    return
  }

  val isSaved = viewModel.isItemSaved("GUIDE", guide.id)
  val recommendedIds = guide.recommendedPhoneIds.split(",").map { it.trim() }
  val recommendedPhones = allPhones.filter { it.id in recommendedIds }

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("buying_guide_screen")
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      IconButton(onClick = { viewModel.selectGuide(null) }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
      }
      Text(
        text = Strings.get("buying_guides", language),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      IconButton(onClick = { viewModel.toggleSaveGuide(guide, isSaved) }) {
        Icon(
          imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
          contentDescription = "Save",
          tint = if (isSaved) TechBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Hero Header
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              AsyncImage(
                model = guide.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              Surface(
                color = TechBluePrimary,
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                modifier = Modifier.align(Alignment.TopStart)
              ) {
                Text(
                  text = guide.category,
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = if (language == AppLanguage.BENGALI) guide.titleBn else guide.titleEn,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${guide.readTimeMinutes} min read • By Tech Medha Lab",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(8.dp))
              Surface(
                color = PriceDropGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "Offline Available",
                  color = PriceDropGreen,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }

      // Guide Summary
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = if (language == AppLanguage.BENGALI) "সারসংক্ষেপ (Summary)" else "Executive Summary",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (language == AppLanguage.BENGALI) guide.summaryBn else guide.summaryEn,
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 22.sp
            )
          }
        }
      }

      // Detailed Content
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (language == AppLanguage.BENGALI) guide.contentBn else guide.contentEn,
              style = MaterialTheme.typography.bodyLarge,
              lineHeight = 26.sp
            )
          }
        }
      }

      // Recommended Phones in this Guide
      if (recommendedPhones.isNotEmpty()) {
        item {
          Text(
            text = if (language == AppLanguage.BENGALI) "এই গাইডের সুপারিশকৃত সেরা ফোনসমূহ" else "Recommended Smartphones in this Guide",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        items(recommendedPhones) { phone ->
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
  }
}
