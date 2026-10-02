package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.SmartphoneEntity
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.TechGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhoneCard(
  phone: SmartphoneEntity,
  language: AppLanguage,
  isSaved: Boolean,
  isInCompare: Boolean,
  onCardClick: () -> Unit,
  onSaveClick: () -> Unit,
  onCompareClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val hasPriceDrop = phone.previousPriceBdt > phone.currentPriceBdt

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("phone_card_${phone.id}")
      .clickable { onCardClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // Thumbnail Image
        Box(
          modifier = Modifier
            .size(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          AsyncImage(
            model = phone.imageUrl,
            contentDescription = "${phone.brand} ${phone.model}",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
          if (phone.network5g) {
            Surface(
              color = TechBluePrimary,
              shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
              modifier = Modifier.align(Alignment.TopStart)
            ) {
              Text(
                text = "5G",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Basic Info
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = phone.brand.uppercase(),
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = TechGold,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = phone.rating.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Text(
            text = phone.model,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(4.dp))

          // Pricing
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "৳ ${String.format("%,d", phone.currentPriceBdt)}",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.ExtraBold
            )

            if (hasPriceDrop) {
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "৳ ${String.format("%,d", phone.previousPriceBdt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
              )
            }
          }

          if (hasPriceDrop) {
            Text(
              text = "↓ ${Strings.get("spec_price_drop", language)} ৳${phone.previousPriceBdt - phone.currentPriceBdt}",
              style = MaterialTheme.typography.labelSmall,
              color = PriceDropGreen,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Key Specs Badges
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        SpecBadge(phone.processor.take(22), Icons.Default.Memory)
        SpecBadge(phone.ram.split("/").firstOrNull()?.trim() ?: phone.ram, null)
        SpecBadge(phone.batteryMah.toString() + " mAh", null)
        SpecBadge(phone.mainCamera.split(",").firstOrNull()?.trim() ?: "Camera", Icons.Default.PhotoCamera)
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Bottom Action Bar (Save & Compare buttons)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Quick spec highlight (Display)
        Text(
          text = "${phone.displaySize} • ${phone.refreshRate.take(6)}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Compare Button
          IconButton(
            onClick = onCompareClick,
            modifier = Modifier
              .size(36.dp)
              .testTag("compare_btn_${phone.id}")
          ) {
            Icon(
              imageVector = Icons.Default.CompareArrows,
              contentDescription = Strings.get("compare", language),
              tint = if (isInCompare) TechCyanAccent else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Bookmark Button
          IconButton(
            onClick = onSaveClick,
            modifier = Modifier
              .size(36.dp)
              .testTag("save_btn_${phone.id}")
          ) {
            Icon(
              imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = Strings.get("save_to_offline", language),
              tint = if (isSaved) TechBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun SpecBadge(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector?) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          modifier = Modifier.size(11.dp),
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(3.dp))
      }
      Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1
      )
    }
  }
}
