package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonesScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val language by viewModel.language.collectAsStateWithLifecycle()
  val phones by viewModel.filteredSmartphones.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedBrand by viewModel.selectedBrand.collectAsStateWithLifecycle()
  val priceRange by viewModel.priceRange.collectAsStateWithLifecycle()
  val only5g by viewModel.filter5gOnly.collectAsStateWithLifecycle()
  val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

  var showFilterSheet by remember { mutableStateOf(false) }

  val brands = listOf("ALL", "Samsung", "Apple", "Xiaomi", "Realme", "OnePlus", "Vivo", "Poco", "Infinix")

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("phones_screen")
  ) {
    // Top Search Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.setSearchQuery(it) },
        placeholder = { Text(Strings.get("search_placeholder", language), fontSize = 13.sp) },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TechBluePrimary)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.setSearchQuery("") }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("phone_search_input")
      )

      Spacer(modifier = Modifier.width(8.dp))

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selectedBrand != "ALL" || only5g || priceRange.second < 250000) TechBluePrimary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
          .size(52.dp)
          .clickable { showFilterSheet = true }
          .testTag("filter_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = Strings.get("filters", language),
            tint = if (selectedBrand != "ALL" || only5g || priceRange.second < 250000) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Horizontal Brand Filter Chips
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 6.dp)
    ) {
      items(brands) { brand ->
        val isSelected = selectedBrand == brand
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.setBrandFilter(brand) },
          label = {
            Text(
              text = if (brand == "ALL") Strings.get("all_brands", language) else brand,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TechBluePrimary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
      }
    }

    // Results Summary Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${phones.size} ${if (language == AppLanguage.BENGALI) "টি স্মার্টফোন পাওয়া গেছে" else "smartphones found"}",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { showFilterSheet = true }
      ) {
        Icon(
          imageVector = Icons.Default.Sort,
          contentDescription = null,
          modifier = Modifier.size(16.dp),
          tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (sortBy) {
            "PRICE_ASC" -> Strings.get("sort_price_low", language)
            "PRICE_DESC" -> Strings.get("sort_price_high", language)
            "NEWEST" -> Strings.get("sort_newest", language)
            else -> Strings.get("sort_rating", language)
          },
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Smartphones List
    if (phones.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Smartphone,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = Strings.get("no_phones_found", language),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(12.dp))
          Button(onClick = { viewModel.resetFilters() }) {
            Text(Strings.get("clear_filters", language))
          }
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(phones, key = { it.id }) { phone ->
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

  // Advanced Filter BottomSheet
  if (showFilterSheet) {
    ModalBottomSheet(
      onDismissRequest = { showFilterSheet = false },
      sheetState = rememberModalBottomSheetState()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = Strings.get("filters", language),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = Strings.get("clear_filters", language),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
              viewModel.resetFilters()
            }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Filter Brackets
        Text(
          text = Strings.get("price_range", language),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          FilterChip(
            selected = priceRange.second <= 25000 && priceRange.first == 0,
            onClick = { viewModel.setPriceFilter(0 to 25000) },
            label = { Text(Strings.get("under_20k", language), fontSize = 12.sp) }
          )
          FilterChip(
            selected = priceRange.first == 20000 && priceRange.second == 35000,
            onClick = { viewModel.setPriceFilter(20000 to 35000) },
            label = { Text(Strings.get("20k_35k", language), fontSize = 12.sp) }
          )
          FilterChip(
            selected = priceRange.first == 35000 && priceRange.second == 50000,
            onClick = { viewModel.setPriceFilter(35000 to 50000) },
            label = { Text(Strings.get("35k_50k", language), fontSize = 12.sp) }
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilterChip(
            selected = priceRange.first >= 50000,
            onClick = { viewModel.setPriceFilter(50000 to 250000) },
            label = { Text(Strings.get("above_50k", language), fontSize = 12.sp) }
          )
          FilterChip(
            selected = only5g,
            onClick = { viewModel.toggle5gFilter() },
            label = { Text(Strings.get("cat_5g", language), fontSize = 12.sp) }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sort Options
        Text(
          text = Strings.get("sort_by", language),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          FilterChip(
            selected = sortBy == "RATING",
            onClick = { viewModel.setSortBy("RATING") },
            label = { Text(Strings.get("sort_rating", language), fontSize = 12.sp) }
          )
          FilterChip(
            selected = sortBy == "NEWEST",
            onClick = { viewModel.setSortBy("NEWEST") },
            label = { Text(Strings.get("sort_newest", language), fontSize = 12.sp) }
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilterChip(
            selected = sortBy == "PRICE_ASC",
            onClick = { viewModel.setSortBy("PRICE_ASC") },
            label = { Text(Strings.get("sort_price_low", language), fontSize = 12.sp) }
          )
          FilterChip(
            selected = sortBy == "PRICE_DESC",
            onClick = { viewModel.setSortBy("PRICE_DESC") },
            label = { Text(Strings.get("sort_price_high", language), fontSize = 12.sp) }
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = { showFilterSheet = false },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(Strings.get("details", language) + " (${phones.size})", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
