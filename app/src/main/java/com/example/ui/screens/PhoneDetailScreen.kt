package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.PriceHistoryEntity
import com.example.data.local.SmartphoneEntity
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.PriceRiseRed
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.theme.TechGold
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun PhoneDetailScreen(
  phoneId: String,
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.selectPhone(null)
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val allPhones by viewModel.allSmartphones.collectAsStateWithLifecycle()
  val phone = allPhones.firstOrNull { it.id == phoneId }
  val priceHistory by viewModel.getPriceHistory(phoneId).collectAsStateWithLifecycle()
  val context = LocalContext.current

  if (phone == null) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text(Strings.get("no_phones_found", language))
    }
    return
  }

  val isSaved = viewModel.isItemSaved("PHONE", phone.id)
  val isInCompare = viewModel.isPhoneInCompare(phone.id)
  val hasPriceDrop = phone.previousPriceBdt > phone.currentPriceBdt

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("phone_detail_screen")
  ) {
    // Top Bar with Back, Share, and Save
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      IconButton(
        onClick = { viewModel.selectPhone(null) },
        modifier = Modifier.testTag("detail_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back"
        )
      }

      Text(
        text = "${phone.brand} ${phone.model}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 1
      )

      Row {
        IconButton(
          onClick = {
            val sendIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, "Check out ${phone.brand} ${phone.model} specs & price on Tech Medha! Current Price: ৳${phone.currentPriceBdt}")
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
          }
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
        }

        IconButton(
          onClick = { viewModel.toggleSavePhone(phone, isSaved) }
        ) {
          Icon(
            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Save",
            tint = if (isSaved) TechBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Phone Hero Card & Photos
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
              contentAlignment = Alignment.Center
            ) {
              AsyncImage(
                model = phone.imageUrl,
                contentDescription = phone.model,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
              )
              if (phone.network5g) {
                Surface(
                  color = TechBluePrimary,
                  shape = RoundedCornerShape(bottomEnd = 10.dp),
                  modifier = Modifier.align(Alignment.TopStart)
                ) {
                  Text(
                    text = "5G NETWORK",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = phone.brand.uppercase(),
                  style = MaterialTheme.typography.labelLarge,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = phone.model,
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Official Release: ${phone.releaseDate}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = TechGold,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${phone.rating} / 5",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Pricing Details
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = Strings.get("spec_price", language),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "৳ ${String.format("%,d", phone.currentPriceBdt)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  if (hasPriceDrop) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = "৳ ${String.format("%,d", phone.previousPriceBdt)}",
                      style = MaterialTheme.typography.titleMedium,
                      textDecoration = TextDecoration.LineThrough,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                if (hasPriceDrop) {
                  Text(
                    text = "↓ ${Strings.get("spec_price_drop", language)} ৳${phone.previousPriceBdt - phone.currentPriceBdt} (${phone.lastPriceUpdated})",
                    fontSize = 12.sp,
                    color = PriceDropGreen,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Button(
                onClick = {
                  viewModel.toggleComparePhone(phone)
                  viewModel.openCompareScreen()
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isInCompare) TechCyanAccent else TechBluePrimary
                ),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.CompareArrows,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isInCompare) Strings.get("compare", language) else Strings.get("add_to_compare", language),
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // 2. Tech Medha Official Review Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(TechBluePrimary),
                contentAlignment = Alignment.Center
              ) {
                Text("TM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = Strings.get("tech_medha_review", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (language == AppLanguage.BENGALI) phone.reviewBn else phone.reviewEn,
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 22.sp
            )
          }
        }
      }

      // 3. Pros and Cons Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = Strings.get("spec_pros", language),
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = PriceDropGreen
            )
            Spacer(modifier = Modifier.height(8.dp))
            phone.pros.split(",").filter { it.isNotBlank() }.forEach { pro ->
              Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 3.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = PriceDropGreen,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = pro.trim(), style = MaterialTheme.typography.bodySmall)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = Strings.get("spec_cons", language),
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = PriceRiseRed
            )
            Spacer(modifier = Modifier.height(8.dp))
            phone.cons.split(",").filter { it.isNotBlank() }.forEach { con ->
              Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 3.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Error,
                  contentDescription = null,
                  tint = PriceRiseRed,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = con.trim(), style = MaterialTheme.typography.bodySmall)
              }
            }
          }
        }
      }

      // 4. Price History Section
      if (priceHistory.isNotEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.History,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = Strings.get("price_history", language),
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(10.dp))
              priceHistory.forEach { history ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text(
                      text = history.date,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                      text = history.note,
                      style = MaterialTheme.typography.bodySmall,
                      fontWeight = FontWeight.Medium
                    )
                  }
                  Text(
                    text = "৳ ${String.format("%,d", history.priceBdt)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
              }
            }
          }
        }
      }

      // 5. Complete Technical Specifications Table
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (language == AppLanguage.BENGALI) "সম্পূর্ণ কারিগরি স্পেসিফিকেশন" else "Full Technical Specifications",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            SpecRow(label = Strings.get("spec_display", language), value = "${phone.displaySize} • ${phone.displayPanel} • ${phone.displayResolution} • ${phone.refreshRate}")
            SpecRow(label = Strings.get("spec_processor", language), value = "${phone.processor} | GPU: ${phone.gpu}")
            SpecRow(label = Strings.get("spec_ram_rom", language), value = "${phone.ram} RAM • ${phone.storage} Storage • Expandable: ${if (phone.expandableStorage) "Yes" else "No"}")
            SpecRow(label = Strings.get("spec_camera", language), value = "Main: ${phone.mainCamera}\nUltrawide: ${phone.ultrawideCamera}\nTelephoto: ${phone.telephotoCamera}\nVideo: ${phone.videoRecording}")
            SpecRow(label = Strings.get("spec_front_cam", language), value = phone.frontCamera)
            SpecRow(label = Strings.get("spec_battery", language), value = "${phone.batteryMah} mAh • ${phone.chargingSpeedWatt}W Fast Charging • Wireless: ${if (phone.wirelessCharging) "Yes" else "No"}")
            SpecRow(label = Strings.get("spec_os", language), value = phone.os)
            SpecRow(label = Strings.get("spec_network", language), value = "5G: ${if (phone.network5g) "Supported" else "No"} • SIM: ${phone.simInfo} • ${phone.wifi} • Bluetooth: ${phone.bluetooth} • NFC: ${if (phone.nfc) "Yes" else "No"}")
            SpecRow(label = Strings.get("spec_water_resistance", language), value = phone.waterResistance)
            SpecRow(label = Strings.get("spec_weight", language), value = "${phone.weightGrams}g • ${phone.dimensions}")
            SpecRow(label = Strings.get("spec_colors", language), value = phone.colors)
          }
        }
      }

      // 6. Related Tech Medha Video
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = Strings.get("related_videos", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Surface(color = YouTubeRed, shape = RoundedCornerShape(6.dp)) {
                Text(
                  text = "Tech Medha",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@techmedha.t/search?query=${Uri.encode(phone.brand + " " + phone.model)}"))
                try { context.startActivity(intent) } catch (_: Exception) {}
              },
              colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (language == AppLanguage.BENGALI) "এই ফোনের রিভিউ ভিডিও ইউটিউবে দেখুন" else "Watch Tech Medha Review Video",
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // 7. Similar Smartphones Carousel
      item {
        val similarPhones = allPhones.filter { it.id != phone.id && (it.brand == phone.brand || it.category == phone.category) }.take(3)
        if (similarPhones.isNotEmpty()) {
          Text(
            text = if (language == AppLanguage.BENGALI) "অনুরূপ বাজেটের অন্যান্য ফোন" else "Similar Smartphones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          similarPhones.forEach { simPhone ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { viewModel.selectPhone(simPhone.id) },
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column {
                  Text(text = "${simPhone.brand} ${simPhone.model}", fontWeight = FontWeight.Bold)
                  Text(text = "৳ ${String.format("%,d", simPhone.currentPriceBdt)}", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = Strings.get("details", language), color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun SpecRow(label: String, value: String) {
  Column(modifier = Modifier.padding(vertical = 6.dp)) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface,
      lineHeight = 20.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
  }
}
