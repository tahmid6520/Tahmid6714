package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.PhoneCard
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.TechBluePrimary
import com.example.ui.viewmodel.TechMedhaViewModel

data class UsageOption(
  val id: String,
  val labelBn: String,
  val labelEn: String,
  val icon: ImageVector
)

@Composable
fun PhoneFinderScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.closePhoneFinder()
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val allPhones by viewModel.allSmartphones.collectAsStateWithLifecycle()
  val budget by viewModel.finderBudget.collectAsStateWithLifecycle()
  val selectedUsage by viewModel.finderUsage.collectAsStateWithLifecycle()
  val require5g by viewModel.finderRequire5g.collectAsStateWithLifecycle()
  val isSubmitted by viewModel.finderSubmitted.collectAsStateWithLifecycle()

  val usages = listOf(
    UsageOption("GAMING", Strings.get("usage_gaming", language), Strings.get("usage_gaming", language), Icons.Default.SportsEsports),
    UsageOption("CAMERA", Strings.get("usage_camera", language), Strings.get("usage_camera", language), Icons.Default.CameraAlt),
    UsageOption("BATTERY", Strings.get("usage_battery", language), Strings.get("usage_battery", language), Icons.Default.BatteryFull),
    UsageOption("STUDENT", Strings.get("usage_student", language), Strings.get("usage_student", language), Icons.Default.School),
    UsageOption("EVERYDAY", Strings.get("usage_everyday", language), Strings.get("usage_everyday", language), Icons.Default.Smartphone)
  )

  // Filter matching phones
  val matchedPhones = remember(allPhones, budget, selectedUsage, require5g, isSubmitted) {
    if (!isSubmitted) emptyList()
    else {
      allPhones.filter { phone ->
        val priceMatch = phone.currentPriceBdt <= budget + 5000 // slight threshold margin
        val networkMatch = !require5g || phone.network5g
        val usageMatch = when (selectedUsage) {
          "GAMING" -> phone.processor.contains("Snapdragon 8", ignoreCase = true) ||
                      phone.processor.contains("Dimensity 8", ignoreCase = true) ||
                      phone.category.equals("Gaming", ignoreCase = true) ||
                      phone.rating >= 4.7f
          "CAMERA" -> phone.mainCamera.contains("200", ignoreCase = true) ||
                      phone.mainCamera.contains("Leica", ignoreCase = true) ||
                      phone.mainCamera.contains("ZEISS", ignoreCase = true) ||
                      phone.telephotoCamera.contains("periscope", ignoreCase = true) ||
                      phone.category.equals("Camera", ignoreCase = true)
          "BATTERY" -> phone.batteryMah >= 5500 || phone.chargingSpeedWatt >= 68
          "STUDENT" -> phone.currentPriceBdt <= 35000
          else -> true
        }
        priceMatch && networkMatch && usageMatch
      }.sortedByDescending { it.rating }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("phone_finder_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = { viewModel.closePhoneFinder() }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
      }
      Column {
        Text(
          text = Strings.get("finder_title", language),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = Strings.get("finder_subtitle", language),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Budget Question
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = Strings.get("select_budget", language),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "৳ ${String.format("%,d", budget)}",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
            Slider(
              value = budget.toFloat(),
              onValueChange = { viewModel.finderBudget.value = it.toInt() },
              valueRange = 15000f..200000f,
              steps = 36,
              modifier = Modifier.fillMaxWidth()
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("৳15,000", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("৳1,00,000", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("৳2,00,000", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }

      // 2. Primary Usage Options
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = Strings.get("primary_usage", language),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            usages.forEach { usage ->
              val isSelected = selectedUsage == usage.id
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clickable { viewModel.finderUsage.value = usage.id },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = usage.icon,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = if (language == AppLanguage.BENGALI) usage.labelBn else usage.labelEn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                  )
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 3. 5G Requirement Switch
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = Strings.get("need_5g", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (language == AppLanguage.BENGALI) "ভবিষ্যতের জন্য ৫জি ব্যান্ড সাপোর্ট ফিল্টার করুন" else "Ensures high-speed network compatibility",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = require5g,
              onCheckedChange = { viewModel.finderRequire5g.value = it }
            )
          }
        }
      }

      // Search Action Button
      item {
        Button(
          onClick = { viewModel.finderSubmitted.value = true },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TechBluePrimary)
        ) {
          Icon(imageVector = Icons.Default.Search, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = Strings.get("find_phones_button", language), fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }

      // Results Section
      if (isSubmitted) {
        item {
          Text(
            text = "${Strings.get("matching_devices", language)} (${matchedPhones.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        if (matchedPhones.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "এই নির্দিষ্ট বাজেটে কোনো ডিভাইস মেলেনি। বাজেট কিছুটা বাড়িয়ে আবার চেষ্টা করুন।" else "No exact match found under this budget. Try adjusting the budget slider.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        } else {
          items(matchedPhones) { phone ->
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
}
