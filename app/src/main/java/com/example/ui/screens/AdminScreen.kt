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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.BuyingGuideEntity
import com.example.data.local.SmartphoneEntity
import com.example.data.local.VideoEntity
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.TechBluePrimary
import com.example.ui.viewmodel.TechMedhaViewModel

@Composable
fun AdminScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.closeAdminScreen()
  }

  val language by viewModel.language.collectAsStateWithLifecycle()
  val phones by viewModel.allSmartphones.collectAsStateWithLifecycle()
  val videos by viewModel.allVideos.collectAsStateWithLifecycle()
  val guides by viewModel.allGuides.collectAsStateWithLifecycle()

  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddPhoneDialog by remember { mutableStateOf(false) }
  var showUpdatePriceDialog by remember { mutableStateOf<SmartphoneEntity?>(null) }
  var showAddVideoDialog by remember { mutableStateOf(false) }

  val tabs = listOf(
    Strings.get("nav_phones", language),
    Strings.get("admin_manage_prices", language),
    Strings.get("nav_videos", language),
    Strings.get("buying_guides", language)
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("admin_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { viewModel.closeAdminScreen() }) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
          text = Strings.get("admin_panel", language),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }
    }

    TabRow(selectedTabIndex = selectedTab) {
      tabs.forEachIndexed { index, tabTitle ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = { Text(tabTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        )
      }
    }

    when (selectedTab) {
      0 -> { // Smartphones Management
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Button(
              onClick = { showAddPhoneDialog = true },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text(Strings.get("admin_add_phone", language), fontWeight = FontWeight.Bold)
            }
          }

          items(phones) { phone ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = "${phone.brand} ${phone.model}", fontWeight = FontWeight.Bold)
                  Text(text = "৳ ${String.format("%,d", phone.currentPriceBdt)} • ${phone.processor}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row {
                  IconButton(onClick = { showUpdatePriceDialog = phone }) {
                    Icon(imageVector = Icons.Default.PriceChange, contentDescription = "Price", tint = TechBluePrimary)
                  }
                  IconButton(onClick = { viewModel.deleteSmartphone(phone.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                  }
                }
              }
            }
          }
        }
      }

      1 -> { // Price Management
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "যেকোনো ফোনের বর্তমান দাম পরিবর্তন করুন। পূর্ববর্তী মূল্য ও তারিখ স্বয়ংক্রিয়ভাবে প্রাইজ হিস্টোরিতে যুক্ত হবে।",
                fontSize = 12.sp,
                modifier = Modifier.padding(12.dp)
              )
            }
          }

          items(phones) { phone ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { showUpdatePriceDialog = phone },
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(text = "${phone.brand} ${phone.model}", fontWeight = FontWeight.Bold)
                  Text(text = "Current: ৳ ${String.format("%,d", phone.currentPriceBdt)} (Last: ${phone.lastPriceUpdated})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = TechBluePrimary)
              }
            }
          }
        }
      }

      2 -> { // Videos Management
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Button(
              onClick = { showAddVideoDialog = true },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text(Strings.get("admin_add_video", language), fontWeight = FontWeight.Bold)
            }
          }

          items(videos) { video ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = video.titleBn, fontWeight = FontWeight.Bold, maxLines = 1)
                  Text(text = "${video.category} • ${video.duration} • ${video.views}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { viewModel.deleteVideo(video.id) }) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
              }
            }
          }
        }
      }

      3 -> { // Guides Management
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(guides) { guide ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = guide.titleBn, fontWeight = FontWeight.Bold, maxLines = 2)
                  Text(text = "${guide.category} • ${guide.readTimeMinutes} mins read", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { viewModel.deleteGuide(guide.id) }) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
              }
            }
          }
        }
      }
    }
  }

  // Update Price Dialog
  if (showUpdatePriceDialog != null) {
    val phone = showUpdatePriceDialog!!
    var newPriceStr by remember { mutableStateOf(phone.currentPriceBdt.toString()) }
    var noteStr by remember { mutableStateOf("অফিসিয়াল প্রাইস ড্রপ অফার") }

    AlertDialog(
      onDismissRequest = { showUpdatePriceDialog = null },
      title = { Text("Update Price: ${phone.model}", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(text = "Current Price: ৳${phone.currentPriceBdt}", fontSize = 13.sp)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = newPriceStr,
            onValueChange = { newPriceStr = it },
            label = { Text("New Price (BDT)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = noteStr,
            onValueChange = { noteStr = it },
            label = { Text("Price Change Note") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val newPrice = newPriceStr.toIntOrNull()
            if (newPrice != null) {
              viewModel.updateSmartphonePrice(phone.id, newPrice, phone, noteStr)
            }
            showUpdatePriceDialog = null
          }
        ) {
          Text(Strings.get("save_changes", language))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showUpdatePriceDialog = null }) {
          Text(Strings.get("cancel", language))
        }
      }
    )
  }

  // Add Phone Dialog
  if (showAddPhoneDialog) {
    var brand by remember { mutableStateOf("Samsung") }
    var model by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("30000") }
    var processor by remember { mutableStateOf("Snapdragon 7s Gen 3") }
    var ram by remember { mutableStateOf("8GB / 12GB") }
    var storage by remember { mutableStateOf("128GB / 256GB") }
    var batteryStr by remember { mutableStateOf("5000") }
    var chargingStr by remember { mutableStateOf("45") }
    var camera by remember { mutableStateOf("50 MP, OIS") }
    var display by remember { mutableStateOf("6.7 inch 120Hz AMOLED") }

    AlertDialog(
      onDismissRequest = { showAddPhoneDialog = false },
      title = { Text(Strings.get("admin_add_phone", language), fontWeight = FontWeight.Bold) },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .verticalScroll(rememberScrollState())
        ) {
          OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model Name") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Price (BDT)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = processor, onValueChange = { processor = it }, label = { Text("Processor") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = ram, onValueChange = { ram = it }, label = { Text("RAM") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = batteryStr, onValueChange = { batteryStr = it }, label = { Text("Battery mAh") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(value = camera, onValueChange = { camera = it }, label = { Text("Main Camera") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (model.isNotBlank()) {
              val newId = (brand + "-" + model).lowercase().replace(" ", "-")
              val price = priceStr.toIntOrNull() ?: 30000
              val battery = batteryStr.toIntOrNull() ?: 5000
              val charging = chargingStr.toIntOrNull() ?: 45
              val phone = SmartphoneEntity(
                id = newId,
                brand = brand,
                model = model,
                imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&auto=format&fit=crop&q=80",
                releaseDate = "2025-02-01",
                currentPriceBdt = price,
                previousPriceBdt = price,
                lastPriceUpdated = "2025-02-01",
                displaySize = display,
                displayResolution = "1080 x 2400 pixels",
                refreshRate = "120Hz AMOLED",
                displayPanel = "Gorilla Glass",
                processor = processor,
                gpu = "Adreno",
                ram = ram,
                storage = storage,
                expandableStorage = true,
                os = "Android 15",
                mainCamera = camera,
                ultrawideCamera = "8 MP",
                telephotoCamera = "None",
                frontCamera = "16 MP",
                videoRecording = "4K@30fps",
                batteryMah = battery,
                chargingSpeedWatt = charging,
                wirelessCharging = false,
                network5g = true,
                simInfo = "Dual SIM (Nano-SIM)",
                wifi = "Wi-Fi 6",
                bluetooth = "5.4",
                nfc = true,
                usbType = "USB Type-C 2.0",
                waterResistance = "IP68",
                weightGrams = 190,
                dimensions = "162 x 75 x 8 mm",
                colors = "Black, Blue",
                pros = "চমৎকার ডিসপ্লে ও ব্যাটারি ব্যাকআপ,৫জি নেটওয়ার্ক ও দ্রুত পারফরম্যান্স",
                cons = "চার্জার স্পিড মাঝারি",
                reviewBn = "$brand $model স্মার্টফোনটি এই বাজেটে দারুণ অলরাউন্ডার পারফর্ম করবে।",
                reviewEn = "The $brand $model offers solid battery life and high value in this segment.",
                relatedVideoId = "tm-vid-01",
                isTrending = true,
                isLatestLaunch = true,
                isPopular = true,
                rating = 4.6f,
                category = "Midrange"
              )
              viewModel.addSmartphone(phone)
            }
            showAddPhoneDialog = false
          }
        ) {
          Text(Strings.get("save_changes", language))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showAddPhoneDialog = false }) {
          Text(Strings.get("cancel", language))
        }
      }
    )
  }

  // Add Video Dialog
  if (showAddVideoDialog) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Reviews") }
    var duration by remember { mutableStateOf("12:00") }
    var isShort by remember { mutableStateOf(false) }

    AlertDialog(
      onDismissRequest = { showAddVideoDialog = false },
      title = { Text(Strings.get("admin_add_video", language)) },
      text = {
        Column {
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Video Title") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Reviews, Comparisons, Guides, Shorts)") }, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration (e.g. 14:20)") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              val newVideo = VideoEntity(
                id = "tm-custom-${System.currentTimeMillis()}",
                titleBn = title,
                titleEn = title,
                youtubeUrl = "https://www.youtube.com/@techmedha.t",
                videoId = "custom_vid",
                thumbnailUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=700&auto=format&fit=crop&q=80",
                category = category,
                duration = duration,
                views = "10K",
                publishedDate = "Just now",
                isShort = isShort || category.equals("Shorts", ignoreCase = true)
              )
              viewModel.addVideo(newVideo)
            }
            showAddVideoDialog = false
          }
        ) {
          Text(Strings.get("save_changes", language))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showAddVideoDialog = false }) {
          Text(Strings.get("cancel", language))
        }
      }
    )
  }
}
