package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.language.AppLanguage
import com.example.ui.language.Strings
import com.example.ui.theme.TechBluePrimary
import com.example.ui.theme.TechCyanAccent
import com.example.ui.viewmodel.NavTab
import com.example.ui.viewmodel.TechMedhaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  viewModel: TechMedhaViewModel,
  modifier: Modifier = Modifier
) {
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val language by viewModel.language.collectAsStateWithLifecycle()
  val selectedPhoneId by viewModel.selectedPhoneId.collectAsStateWithLifecycle()
  val selectedGuideId by viewModel.selectedGuideId.collectAsStateWithLifecycle()
  val showCompareScreen by viewModel.showCompareScreen.collectAsStateWithLifecycle()
  val showPhoneFinderScreen by viewModel.showPhoneFinderScreen.collectAsStateWithLifecycle()
  val showToolkitScreen by viewModel.showToolkitScreen.collectAsStateWithLifecycle()
  val showAdminScreen by viewModel.showAdminScreen.collectAsStateWithLifecycle()
  val showShortsFeed by viewModel.showShortsFeed.collectAsStateWithLifecycle()
  val compareTray by viewModel.comparisonTray.collectAsStateWithLifecycle()

  val hasSubScreenOpen = selectedPhoneId != null ||
      selectedGuideId != null ||
      showCompareScreen ||
      showPhoneFinderScreen ||
      showToolkitScreen ||
      showAdminScreen ||
      showShortsFeed

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      if (!hasSubScreenOpen) {
        CenterAlignedTopAppBar(
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(TechBluePrimary),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "TM",
                  color = Color.White,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = Strings.get("app_name", language),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 19.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          },
          actions = {
            // Quick Compare Tray Button
            if (compareTray.isNotEmpty()) {
              IconButton(onClick = { viewModel.openCompareScreen() }) {
                BadgedBox(
                  badge = {
                    Badge(containerColor = TechBluePrimary) {
                      Text(text = compareTray.size.toString(), color = Color.White)
                    }
                  }
                ) {
                  Icon(
                    imageVector = Icons.Default.CompareArrows,
                    contentDescription = "Compare Tray",
                    tint = TechBluePrimary
                  )
                }
              }
            }

            // Language Toggle Switch (BN <-> EN)
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier
                .padding(end = 12.dp)
                .clickable {
                  val nextLang = if (language == AppLanguage.BENGALI) AppLanguage.ENGLISH else AppLanguage.BENGALI
                  viewModel.setLanguage(nextLang)
                }
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              ) {
                Text(
                  text = if (language == AppLanguage.BENGALI) "BN | বাং" else "EN | Eng",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
          },
          colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          )
        )
      }
    },
    bottomBar = {
      if (!hasSubScreenOpen) {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 8.dp
        ) {
          NavigationBarItem(
            selected = currentTab == NavTab.HOME,
            onClick = { viewModel.setTab(NavTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = Strings.get("nav_home", language)) },
            label = { Text(Strings.get("nav_home", language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = TechBluePrimary.copy(alpha = 0.2f))
          )

          NavigationBarItem(
            selected = currentTab == NavTab.PHONES,
            onClick = { viewModel.setTab(NavTab.PHONES) },
            icon = { Icon(Icons.Default.Smartphone, contentDescription = Strings.get("nav_phones", language)) },
            label = { Text(Strings.get("nav_phones", language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = TechBluePrimary.copy(alpha = 0.2f))
          )

          NavigationBarItem(
            selected = currentTab == NavTab.VIDEOS,
            onClick = { viewModel.setTab(NavTab.VIDEOS) },
            icon = { Icon(Icons.Default.PlayCircle, contentDescription = Strings.get("nav_videos", language)) },
            label = { Text(Strings.get("nav_videos", language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = TechBluePrimary.copy(alpha = 0.2f))
          )

          NavigationBarItem(
            selected = currentTab == NavTab.EXPLORE,
            onClick = { viewModel.setTab(NavTab.EXPLORE) },
            icon = { Icon(Icons.Default.Explore, contentDescription = Strings.get("nav_explore", language)) },
            label = { Text(Strings.get("nav_explore", language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = TechBluePrimary.copy(alpha = 0.2f))
          )

          NavigationBarItem(
            selected = currentTab == NavTab.PROFILE,
            onClick = { viewModel.setTab(NavTab.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = Strings.get("nav_profile", language)) },
            label = { Text(Strings.get("nav_profile", language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = TechBluePrimary.copy(alpha = 0.2f))
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when {
        selectedPhoneId != null -> {
          PhoneDetailScreen(phoneId = selectedPhoneId!!, viewModel = viewModel)
        }
        selectedGuideId != null -> {
          BuyingGuideScreen(guideId = selectedGuideId!!, viewModel = viewModel)
        }
        showCompareScreen -> {
          CompareScreen(viewModel = viewModel)
        }
        showPhoneFinderScreen -> {
          PhoneFinderScreen(viewModel = viewModel)
        }
        showToolkitScreen -> {
          ToolkitScreen(viewModel = viewModel)
        }
        showAdminScreen -> {
          AdminScreen(viewModel = viewModel)
        }
        showShortsFeed -> {
          ShortsFeedScreen(viewModel = viewModel)
        }
        else -> {
          when (currentTab) {
            NavTab.HOME -> HomeScreen(viewModel = viewModel)
            NavTab.PHONES -> PhonesScreen(viewModel = viewModel)
            NavTab.VIDEOS -> VideosScreen(viewModel = viewModel)
            NavTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
            NavTab.PROFILE -> ProfileScreen(viewModel = viewModel)
          }
        }
      }
    }
  }
}
