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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.viewmodel.TechMedhaViewModel
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class ToolTab(val id: String, val titleBn: String, val titleEn: String, val icon: ImageVector)

@Composable
fun ToolkitScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.closeToolkit()
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val activeTool by viewModel.activeTool.collectAsStateWithLifecycle()

  val tools = listOf(
    ToolTab("STORAGE", Strings.get("tool_storage_calc", language), Strings.get("tool_storage_calc", language), Icons.Default.Storage),
    ToolTab("CHARGING", Strings.get("tool_charging_calc", language), Strings.get("tool_charging_calc", language), Icons.Default.FlashOn),
    ToolTab("PPI", Strings.get("tool_ppi_calc", language), Strings.get("tool_ppi_calc", language), Icons.Default.Monitor),
    ToolTab("CONVERTER", Strings.get("tool_unit_calc", language), Strings.get("tool_unit_calc", language), Icons.Default.DataUsage),
    ToolTab("CURRENCY", Strings.get("tool_currency_calc", language), Strings.get("tool_currency_calc", language), Icons.Default.CurrencyExchange)
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("toolkit_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = { viewModel.closeToolkit() }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
      }
      Column {
        Text(
          text = Strings.get("toolkit_title", language),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = Strings.get("toolkit_subtitle", language),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Tool Selector Chips
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(tools) { tool ->
        val isSelected = activeTool == tool.id
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.setActiveTool(tool.id) },
          leadingIcon = {
            Icon(imageVector = tool.icon, contentDescription = null, modifier = Modifier.size(16.dp))
          },
          label = {
            Text(
              text = if (language == AppLanguage.BENGALI) tool.titleBn else tool.titleEn,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TechBluePrimary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
      }
    }

    // Active Tool View
    Box(modifier = Modifier.fillMaxSize()) {
      when (activeTool) {
        "STORAGE" -> StorageCalculatorView(language)
        "CHARGING" -> ChargingCalculatorView(language)
        "PPI" -> ScreenPpiCalculatorView(language)
        "CONVERTER" -> DataConverterView(language)
        "CURRENCY" -> CurrencyConverterView(language)
        else -> StorageCalculatorView(language)
      }
    }
  }
}

// 1. Storage Calculator
@Composable
fun StorageCalculatorView(language: AppLanguage) {
  var totalStorageGb by remember { mutableIntStateOf(128) }
  var photosCount by remember { mutableIntStateOf(2000) } // ~5MB each
  var videoMinutes by remember { mutableIntStateOf(60) } // ~350MB per min for 4K
  var appsCount by remember { mutableIntStateOf(40) } // ~1.5GB each
  var heavyGames by remember { mutableIntStateOf(3) } // ~15GB each

  val systemOsGb = 16.0
  val photosGb = (photosCount * 5.0) / 1024.0
  val videosGb = (videoMinutes * 350.0) / 1024.0
  val appsGb = (appsCount * 1.5)
  val gamesGb = (heavyGames * 15.0)

  val totalUsedGb = systemOsGb + photosGb + videosGb + appsGb + gamesGb
  val freeGb = (totalStorageGb - totalUsedGb).coerceAtLeast(0.0)
  val usagePercent = (totalUsedGb / totalStorageGb).toFloat().coerceIn(0f, 1f)

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = if (language == AppLanguage.BENGALI) "ফোনের ইন্টারনাল স্টোরেজ বেছে নিন" else "Select Phone Storage Capacity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(64, 128, 256, 512).forEach { cap ->
              FilterChip(
                selected = totalStorageGb == cap,
                onClick = { totalStorageGb = cap },
                label = { Text("${cap}GB", fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = TechBluePrimary,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Usage Progress
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Used: ${String.format("%.1f", totalUsedGb)} GB", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "Free: ${String.format("%.1f", freeGb)} GB", color = PriceDropGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressIndicator(
            progress = { usagePercent },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp),
            color = if (usagePercent > 0.85f) Color.Red else TechBluePrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }
    }

    // Sliders
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Photos (${photosCount} pcs ~ ${String.format("%.1f", photosGb)}GB)", fontWeight = FontWeight.Bold)
          Slider(value = photosCount.toFloat(), onValueChange = { photosCount = it.toInt() }, valueRange = 0f..10000f, steps = 20)

          Spacer(modifier = Modifier.height(8.dp))
          Text(text = "4K Videos (${videoMinutes} mins ~ ${String.format("%.1f", videosGb)}GB)", fontWeight = FontWeight.Bold)
          Slider(value = videoMinutes.toFloat(), onValueChange = { videoMinutes = it.toInt() }, valueRange = 0f..300f, steps = 15)

          Spacer(modifier = Modifier.height(8.dp))
          Text(text = "Apps (${appsCount} apps ~ ${String.format("%.1f", appsGb)}GB)", fontWeight = FontWeight.Bold)
          Slider(value = appsCount.toFloat(), onValueChange = { appsCount = it.toInt() }, valueRange = 10f..120f, steps = 11)

          Spacer(modifier = Modifier.height(8.dp))
          Text(text = "Heavy Games (${heavyGames} games ~ ${gamesGb.toInt()}GB)", fontWeight = FontWeight.Bold)
          Slider(value = heavyGames.toFloat(), onValueChange = { heavyGames = it.toInt() }, valueRange = 0f..8f, steps = 8)
        }
      }
    }
  }
}

// 2. Charging Estimator
@Composable
fun ChargingCalculatorView(language: AppLanguage) {
  var batteryMah by remember { mutableIntStateOf(5000) }
  var chargerWatts by remember { mutableIntStateOf(68) }

  // Realistic calculation formula accounting for thermal tapering
  val effectiveWatts = chargerWatts * 0.72 // average power delivery
  val energyWattHours = (batteryMah / 1000.0) * 3.85
  val timeHours = energyWattHours / effectiveWatts
  val totalMinutes = (timeHours * 60).roundToInt().coerceAtLeast(15)
  val halfChargeMinutes = (totalMinutes * 0.38).roundToInt()

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = if (language == AppLanguage.BENGALI) "ব্যাটারি ও চার্জার স্পেসিফিকেশন" else "Battery & Charger Specs",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(14.dp))
          Text(text = "Battery Capacity: $batteryMah mAh", fontWeight = FontWeight.SemiBold)
          Slider(value = batteryMah.toFloat(), onValueChange = { batteryMah = it.toInt() }, valueRange = 4000f..7000f, steps = 30)

          Spacer(modifier = Modifier.height(10.dp))
          Text(text = "Charger Speed: $chargerWatts Watts", fontWeight = FontWeight.SemiBold)
          Slider(value = chargerWatts.toFloat(), onValueChange = { chargerWatts = it.toInt() }, valueRange = 15f..150f, steps = 27)
        }
      }
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = if (language == AppLanguage.BENGALI) "আনুমানিক চার্জিং টাইম ফলাফল" else "Estimated Charging Results",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "0% থেকে 50% চার্জ", style = MaterialTheme.typography.bodySmall)
              Text(text = "~$halfChargeMinutes মিনিট", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = TechBluePrimary)
            }
            Column {
              Text(text = "0% থেকে 100% ফুল চার্জ", style = MaterialTheme.typography.bodySmall)
              Text(text = "~$totalMinutes মিনিট", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = PriceDropGreen)
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "নোট: ৮০% চার্জের পর ব্যাটারি সুরক্ষায় ট্রিকল চার্জিং সক্রিয় হয়, ফলে শেষ ২০% সময় কিছুটা বেশি লাগে।",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

// 3. Screen PPI Calculator
@Composable
fun ScreenPpiCalculatorView(language: AppLanguage) {
  var inchesStr by remember { mutableStateOf("6.7") }
  var widthStr by remember { mutableStateOf("1080") }
  var heightStr by remember { mutableStateOf("2400") }

  val inches = inchesStr.toDoubleOrNull() ?: 6.7
  val width = widthStr.toDoubleOrNull() ?: 1080.0
  val height = heightStr.toDoubleOrNull() ?: 2400.0

  val ppi = if (inches > 0) (sqrt(width * width + height * height) / inches).roundToInt() else 0

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          OutlinedTextField(
            value = inchesStr,
            onValueChange = { inchesStr = it },
            label = { Text("Display Size (inches, e.g. 6.67)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = widthStr,
              onValueChange = { widthStr = it },
              label = { Text("Width px") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
              value = heightStr,
              onValueChange = { heightStr = it },
              label = { Text("Height px") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Pixel Density (PPI)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = "$ppi PPI", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = TechBluePrimary)
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (ppi >= 400) "★ রেটিনা শার্পনেস: অত্যন্ত নিখুঁত ও ক্রিস্টাল ক্লিয়ার ডিসপ্লে" else "স্ট্যান্ডার্ড শার্পনেস: সাধারণ মিডিয়া ও পড়াশোনার জন্য উপযুক্ত",
            color = PriceDropGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

// 4. Data Converter
@Composable
fun DataConverterView(language: AppLanguage) {
  var inputVal by remember { mutableStateOf("10") }
  val value = inputVal.toDoubleOrNull() ?: 10.0

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      OutlinedTextField(
        value = inputVal,
        onValueChange = { inputVal = it },
        label = { Text("Gigabytes (GB)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Terabytes (TB): ${String.format("%.4f", value / 1024.0)} TB", fontWeight = FontWeight.Bold)
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
          Text(text = "Megabytes (MB): ${String.format("%,.0f", value * 1024.0)} MB", fontWeight = FontWeight.Bold)
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
          Text(text = "Kilobytes (KB): ${String.format("%,.0f", value * 1024.0 * 1024.0)} KB", fontWeight = FontWeight.Bold)
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
          Text(text = "Bits: ${String.format("%,.0f", value * 1024.0 * 1024.0 * 1024.0 * 8.0)} bits", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// 5. Currency Converter
@Composable
fun CurrencyConverterView(language: AppLanguage) {
  var bdtStr by remember { mutableStateOf("50000") }
  val bdt = bdtStr.toDoubleOrNull() ?: 50000.0

  // Offline default benchmark rates
  val rateUsd = 122.50
  val rateInr = 1.42
  val rateEur = 130.00

  val usd = bdt / rateUsd
  val inr = bdt / rateInr
  val eur = bdt / rateEur

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      OutlinedTextField(
        value = bdtStr,
        onValueChange = { bdtStr = it },
        label = { Text("বাংলাদেশি টাকা (BDT)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "US Dollar (USD): $ ${String.format("%.2f", usd)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TechBluePrimary)
          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
          Text(text = "Indian Rupee (INR): ₹ ${String.format("%.2f", inr)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
          Text(text = "Euro (EUR): € ${String.format("%.2f", eur)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      }
    }

    item {
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(
          text = "নোট: এই বিনিময় হার অফলাইন বেঞ্চমার্কের উপর ভিত্তি করে তৈরি। লাইভ মার্কেট রেটের জন্য ইন্টারনেট কানেকশন প্রয়োজন।",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(12.dp)
        )
      }
    }
  }
}
