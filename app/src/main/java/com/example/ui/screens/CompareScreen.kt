package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.SmartphoneEntity
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.viewmodel.TechMedhaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.closeCompareScreen()
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val comparisonPhones by viewModel.comparisonTray.collectAsStateWithLifecycle()
  val allPhones by viewModel.allSmartphones.collectAsStateWithLifecycle()

  var showAddPhoneDialog by remember { mutableStateOf(false) }
  var phoneSlotToReplace by remember { mutableStateOf<Int?>(null) }
  var savedNotice by remember { mutableStateOf(false) }

  // Default comparison if empty
  if (comparisonPhones.isEmpty() && allPhones.size >= 2) {
    viewModel.setCompareSlot(0, allPhones[0])
    viewModel.setCompareSlot(1, allPhones[1])
  }

  val phones = comparisonPhones.take(3)
  val colWidth = if (phones.size <= 2) 160.dp else 140.dp

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("compare_screen")
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { viewModel.closeCompareScreen() }) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
          text = Strings.get("compare_title", language),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }

      Button(
        onClick = {
          if (phones.size >= 2) {
            viewModel.saveCurrentComparison("${phones[0].model} vs ${phones[1].model}")
            savedNotice = true
          }
        },
        enabled = phones.size >= 2,
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = if (savedNotice) Strings.get("comparison_saved", language) else Strings.get("save_comparison", language), fontSize = 12.sp)
      }
    }

    // Factual Disclaimer Box
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      shape = RoundedCornerShape(10.dp),
      color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    ) {
      Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = Strings.get("spec_difference_notice", language),
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 8.dp)
    ) {
      // Comparison Table Header: Device Cards
      item {
        val scrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Label Column placeholder
          Box(modifier = Modifier.width(100.dp)) {
            Text(
              text = if (language == AppLanguage.BENGALI) "ফিচার" else "Feature",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(top = 10.dp)
            )
          }

          phones.forEachIndexed { index, phone ->
            Card(
              modifier = Modifier.width(colWidth),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
              Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                  AsyncImage(
                    model = phone.imageUrl,
                    contentDescription = phone.model,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                  )
                  IconButton(
                    onClick = { viewModel.removeComparePhone(phone.id) },
                    modifier = Modifier
                      .size(24.dp)
                      .align(Alignment.TopEnd)
                      .background(Color(0x99000000), CircleShape)
                  ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(14.dp))
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = phone.brand.uppercase(), fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(text = phone.model, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, textAlign = TextAlign.Center)
                Text(
                  text = "৳ ${String.format("%,d", phone.currentPriceBdt)}",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = Strings.get("change_phone", language),
                  fontSize = 11.sp,
                  color = TechBluePrimary,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier.clickable {
                    phoneSlotToReplace = index
                    showAddPhoneDialog = true
                  }
                )
              }
            }
          }

          // Add Device Slot if less than 3
          if (phones.size < 3) {
            Card(
              modifier = Modifier
                .width(colWidth)
                .height(180.dp)
                .clickable {
                  phoneSlotToReplace = phones.size
                  showAddPhoneDialog = true
                },
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add phone", tint = TechBluePrimary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = Strings.get("select_device", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(12.dp)) }

      // Specification Comparison Rows
      item {
        val scrollState = rememberScrollState()
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp)
        ) {
          CompareSpecRow("Price", phones.map { "৳ ${String.format("%,d", it.currentPriceBdt)}" }, colWidth)
          CompareSpecRow("Display", phones.map { "${it.displaySize}\n${it.refreshRate}" }, colWidth)
          CompareSpecRow("Resolution", phones.map { it.displayResolution }, colWidth)
          CompareSpecRow("Processor", phones.map { it.processor }, colWidth)
          CompareSpecRow("GPU", phones.map { it.gpu }, colWidth)
          CompareSpecRow("RAM", phones.map { it.ram }, colWidth)
          CompareSpecRow("Storage", phones.map { it.storage }, colWidth)
          CompareSpecRow("Rear Camera", phones.map { "${it.mainCamera}\n${it.ultrawideCamera}" }, colWidth)
          CompareSpecRow("Telephoto", phones.map { it.telephotoCamera }, colWidth)
          CompareSpecRow("Selfie", phones.map { it.frontCamera }, colWidth)
          CompareSpecRow("Battery", phones.map { "${it.batteryMah} mAh" }, colWidth)
          CompareSpecRow("Charging", phones.map { "${it.chargingSpeedWatt}W Fast Charging" }, colWidth)
          CompareSpecRow("Wireless", phones.map { if (it.wirelessCharging) "Yes" else "No" }, colWidth)
          CompareSpecRow("5G Network", phones.map { if (it.network5g) "5G Supported" else "4G LTE" }, colWidth)
          CompareSpecRow("OS", phones.map { it.os }, colWidth)
          CompareSpecRow("Water Proof", phones.map { it.waterResistance }, colWidth)
          CompareSpecRow("Weight", phones.map { "${it.weightGrams} grams" }, colWidth)
          CompareSpecRow("Dimensions", phones.map { it.dimensions }, colWidth)
          CompareSpecRow("Colors", phones.map { it.colors }, colWidth)
        }
      }
    }
  }

  // Device Picker Sheet
  if (showAddPhoneDialog) {
    ModalBottomSheet(
      onDismissRequest = { showAddPhoneDialog = false }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = Strings.get("select_device", language),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
          modifier = Modifier.height(350.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(allPhones) { candidate ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  phoneSlotToReplace?.let { slot ->
                    viewModel.setCompareSlot(slot, candidate)
                  }
                  showAddPhoneDialog = false
                },
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                ) {
                  AsyncImage(model = candidate.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(text = "${candidate.brand} ${candidate.model}", fontWeight = FontWeight.Bold)
                  Text(text = "৳ ${String.format("%,d", candidate.currentPriceBdt)} • ${candidate.processor}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CompareSpecRow(
  label: String,
  values: List<String>,
  colWidth: androidx.compose.ui.unit.Dp
) {
  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(modifier = Modifier.width(100.dp)) {
        Text(
          text = label,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
      }
      values.forEach { v ->
        Box(
          modifier = Modifier
            .width(colWidth)
            .padding(horizontal = 4.dp)
        ) {
          Text(
            text = v,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
          )
        }
      }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
  }
}
