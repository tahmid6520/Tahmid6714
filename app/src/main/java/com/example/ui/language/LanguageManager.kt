package com.example.ui.language

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
  BENGALI("bn", "Bengali", "বাংলা"),
  ENGLISH("en", "English", "English")
}

object Strings {
  private val translations = mapOf(
    // App & Navigation
    "app_name" to mapOf("bn" to "টেক মেধা", "en" to "Tech Medha"),
    "app_tagline" to mapOf("bn" to "প্রযুক্তির সহজ সমাধান ও স্মার্টফোন ডেটাবেস", "en" to "Smart Technology & Smartphone Hub"),
    "nav_home" to mapOf("bn" to "হোম", "en" to "Home"),
    "nav_phones" to mapOf("bn" to "স্মার্টফোন", "en" to "Phones"),
    "nav_videos" to mapOf("bn" to "ভিডিও", "en" to "Videos"),
    "nav_explore" to mapOf("bn" to "এক্সপ্লোর", "en" to "Explore"),
    "nav_profile" to mapOf("bn" to "প্রোফাইল", "en" to "Profile"),

    // Home Section
    "featured" to mapOf("bn" to "স্পেশাল ফিচার্ড", "en" to "Featured"),
    "latest_videos" to mapOf("bn" to "লেটেস্ট ভিডিও", "en" to "Latest Videos"),
    "latest_shorts" to mapOf("bn" to "জনপ্রিয় শর্টস", "en" to "Latest Shorts"),
    "trending_phones" to mapOf("bn" to "ট্রেন্ডিং স্মার্টফোন", "en" to "Trending Phones"),
    "latest_launches" to mapOf("bn" to "নতুন লঞ্চ হওয়া ফোন", "en" to "Latest Launches"),
    "popular_phones" to mapOf("bn" to "জনপ্রিয় ফোনসমূহ", "en" to "Popular Phones"),
    "buying_guides" to mapOf("bn" to "বায়িং গাইড", "en" to "Buying Guides"),
    "tech_news" to mapOf("bn" to "প্রযুক্তি সংবাদ", "en" to "Tech News"),
    "recommended_comparisons" to mapOf("bn" to "সেরা তুলনা", "en" to "Top Comparisons"),
    "quick_categories" to mapOf("bn" to "ক্যাটাগরি", "en" to "Categories"),
    "view_all" to mapOf("bn" to "সব দেখুন", "en" to "View All"),
    "watch_now" to mapOf("bn" to "ভিডিও দেখুন", "en" to "Watch Now"),
    "compare" to mapOf("bn" to "তুলনা করুন", "en" to "Compare"),
    "details" to mapOf("bn" to "বিস্তারিত", "en" to "Details"),

    // Categories
    "cat_flagship" to mapOf("bn" to "ফ্ল্যাগশিপ", "en" to "Flagship"),
    "cat_midrange" to mapOf("bn" to "মিড-রেঞ্জ", "en" to "Mid-Range"),
    "cat_budget" to mapOf("bn" to "বাজেট ফোন", "en" to "Budget"),
    "cat_gaming" to mapOf("bn" to "গেমিং", "en" to "Gaming"),
    "cat_camera" to mapOf("bn" to "ক্যামেরা স্পেশাল", "en" to "Camera Focus"),
    "cat_5g" to mapOf("bn" to "৫জি ফোন", "en" to "5G Phones"),

    // Smartphone Catalog & Filters
    "search_placeholder" to mapOf("bn" to "ফোন, ব্র্যান্ড বা প্রসেসর খুঁজুন...", "en" to "Search phones, brands, processors..."),
    "filters" to mapOf("bn" to "ফিল্টার", "en" to "Filters"),
    "clear_filters" to mapOf("bn" to "ফিল্টার মুছুন", "en" to "Reset"),
    "all_brands" to mapOf("bn" to "সব ব্র্যান্ড", "en" to "All Brands"),
    "price_range" to mapOf("bn" to "বাজেট সীমা", "en" to "Price Range"),
    "under_20k" to mapOf("bn" to "২০,০০০ টাকার নিচে", "en" to "Under ৳20K"),
    "20k_35k" to mapOf("bn" to "২০,০০০ - ৩৫,০০০ টাকা", "en" to "৳20K - ৳35K"),
    "35k_50k" to mapOf("bn" to "৩৫,০০০ - ৫০,০০০ টাকা", "en" to "৳35K - ৳50K"),
    "above_50k" to mapOf("bn" to "৫০,০০০ টাকার উপরে", "en" to "Above ৳50K"),
    "sort_by" to mapOf("bn" to "সাজান", "en" to "Sort By"),
    "sort_price_low" to mapOf("bn" to "কম দাম প্রথমে", "en" to "Price: Low to High"),
    "sort_price_high" to mapOf("bn" to "বেশি দাম প্রথমে", "en" to "Price: High to Low"),
    "sort_newest" to mapOf("bn" to "নতুন লঞ্চ", "en" to "Newest First"),
    "sort_rating" to mapOf("bn" to "রেটিং অনুসারে", "en" to "Highest Rated"),
    "no_phones_found" to mapOf("bn" to "কোনো স্মার্টফোন পাওয়া যায়নি", "en" to "No smartphones found"),

    // Smartphone Specs
    "spec_price" to mapOf("bn" to "মূল্য", "en" to "Price"),
    "spec_prev_price" to mapOf("bn" to "পূর্ববর্তী মূল্য", "en" to "Previous Price"),
    "spec_price_drop" to mapOf("bn" to "দাম কমেছে", "en" to "Price Dropped"),
    "spec_display" to mapOf("bn" to "ডিসপ্লে", "en" to "Display"),
    "spec_processor" to mapOf("bn" to "প্রসেসর", "en" to "Processor"),
    "spec_gpu" to mapOf("bn" to "গ্রাফিক্স (GPU)", "en" to "GPU"),
    "spec_ram_rom" to mapOf("bn" to "র‍্যাম / স্টোরেজ", "en" to "RAM / Storage"),
    "spec_camera" to mapOf("bn" to "ক্যামেরা", "en" to "Camera"),
    "spec_front_cam" to mapOf("bn" to "সেলফি ক্যামেরা", "en" to "Selfie Camera"),
    "spec_battery" to mapOf("bn" to "ব্যাটারি ও চার্জিং", "en" to "Battery & Charging"),
    "spec_charging" to mapOf("bn" to "চার্জিং স্পিড", "en" to "Charging Speed"),
    "spec_os" to mapOf("bn" to "অপারেটিং সিস্টেম", "en" to "OS"),
    "spec_network" to mapOf("bn" to "নেটওয়ার্ক ও ৫জি", "en" to "Network & 5G"),
    "spec_water_resistance" to mapOf("bn" to "ওয়াটার রেজিস্ট্যান্স", "en" to "Water Resistance"),
    "spec_weight" to mapOf("bn" to "ওজন ও মাপ", "en" to "Weight & Dimensions"),
    "spec_colors" to mapOf("bn" to "রং", "en" to "Colors"),
    "spec_pros" to mapOf("bn" to "ভালো দিক (Pros)", "en" to "Pros"),
    "spec_cons" to mapOf("bn" to "খারাপ দিক (Cons)", "en" to "Cons"),
    "tech_medha_review" to mapOf("bn" to "টেক মেধা রিভিউ", "en" to "Tech Medha Review"),
    "price_history" to mapOf("bn" to "মূল্যের ইতিহাস", "en" to "Price History"),
    "related_videos" to mapOf("bn" to "সম্পর্কিত ভিডিও", "en" to "Related Videos"),
    "save_to_offline" to mapOf("bn" to "সংরক্ষণ করুন", "en" to "Save Offline"),
    "saved" to mapOf("bn" to "সংরক্ষিত", "en" to "Saved"),
    "add_to_compare" to mapOf("bn" to "তুলনায় যোগ করুন", "en" to "Add to Compare"),

    // Compare Screen
    "compare_title" to mapOf("bn" to "স্মার্টফোন তুলনা", "en" to "Compare Phones"),
    "select_device" to mapOf("bn" to "ডিভাইস নির্বাচন করুন", "en" to "Select Device"),
    "change_phone" to mapOf("bn" to "পরিবর্তন", "en" to "Change"),
    "save_comparison" to mapOf("bn" to "তুলনা সেভ করুন", "en" to "Save Comparison"),
    "comparison_saved" to mapOf("bn" to "তুলনা সংরক্ষিত হয়েছে", "en" to "Comparison Saved"),
    "spec_difference_notice" to mapOf("bn" to "এখানে সঠিক কারিগরি তথ্য উপস্থাপন করা হয়েছে। বিজয়ী ঘোষণা ছাড়াই আপনি প্রয়োজনমতো পছন্দ করতে পারেন।", "en" to "Factual specifications presented side-by-side to help you decide without biased winner tags."),

    // Phone Finder
    "finder_title" to mapOf("bn" to "ফোন ফাইন্ডার", "en" to "Phone Finder"),
    "finder_subtitle" to mapOf("bn" to "আপনার চাহিদা অনুযায়ী সেরা ফোনটি খুঁজুন", "en" to "Find the right smartphone based on your needs"),
    "select_budget" to mapOf("bn" to "আপনার বাজেট সিলেক্ট করুন:", "en" to "Select your budget:"),
    "primary_usage" to mapOf("bn" to "আপনার প্রধান ব্যবহার:", "en" to "Primary purpose:"),
    "need_5g" to mapOf("bn" to "৫জি থাকা কি আবশ্যক?", "en" to "Require 5G support?"),
    "find_phones_button" to mapOf("bn" to "সেরা ফোনগুলো খুঁজুন", "en" to "Find Matching Phones"),
    "matching_devices" to mapOf("bn" to "আপনার পছন্দের সাথে মিল পাওয়া ফোনসমূহ", "en" to "Matching Smartphones Found"),

    // Usages
    "usage_gaming" to mapOf("bn" to "ভারী গেমিং ও পারফরম্যান্স", "en" to "Gaming & High Performance"),
    "usage_camera" to mapOf("bn" to "ছবি ও ভিডিওগ্রাফি", "en" to "Photography & Videography"),
    "usage_battery" to mapOf("bn" to "দীর্ঘস্থায়ী ব্যাটারি ব্যাকআপ", "en" to "Long Battery Endurance"),
    "usage_student" to mapOf("bn" to "শিক্ষার্থী / সাশ্রয়ী বাজেট", "en" to "Student / Value for Money"),
    "usage_everyday" to mapOf("bn" to "সাধারণ ব্যবহার ও সোশ্যাল মিডিয়া", "en" to "Everyday & Social Media"),

    // Videos & Shorts
    "tech_medha_channel" to mapOf("bn" to "টেক মেধা অফিশিয়াল চ্যানেল", "en" to "Tech Medha Official Channel"),
    "channel_handle" to mapOf("bn" to "@techmedha.t", "en" to "@techmedha.t"),
    "subscriber_count" to mapOf("bn" to "স্মার্ট টেক রিভিউ এবং তুলনা", "en" to "Tech Reviews & Comparisons"),
    "tab_all_videos" to mapOf("bn" to "সব ভিডিও", "en" to "All Videos"),
    "tab_reviews" to mapOf("bn" to "রিভিউ", "en" to "Reviews"),
    "tab_comparisons" to mapOf("bn" to "তুলনা", "en" to "Comparisons"),
    "tab_shorts" to mapOf("bn" to "শর্টস", "en" to "Shorts"),
    "play_offline_cached" to mapOf("bn" to "ভিডিও তথ্য অফলাইনে ক্যাশ করা আছে", "en" to "Video info cached for offline reading"),

    // Toolkit
    "toolkit_title" to mapOf("bn" to "টেক মেধা টুলকিট", "en" to "Tech Medha Toolkit"),
    "toolkit_subtitle" to mapOf("bn" to "প্রয়োজনীয় টেক ক্যালকুলেটর ও কনভার্টার", "en" to "Useful Tech Calculators & Converters"),
    "tool_storage_calc" to mapOf("bn" to "স্টোরেজ ক্যালকুলেটর", "en" to "Storage Calculator"),
    "tool_storage_desc" to mapOf("bn" to "কতগুলো ছবি, 4K ভিডিও ও গেম রাখা সম্ভব জানুন", "en" to "Estimate how many photos, 4K videos & apps fit"),
    "tool_charging_calc" to mapOf("bn" to "চার্জিং সময় ক্যালকুলেটর", "en" to "Charging Time Estimator"),
    "tool_charging_desc" to mapOf("bn" to "ব্যাটারি ক্যাপাসিটি ও চার্জারের ওয়াট দিয়ে সময় হিসাব করুন", "en" to "Calculate charge time based on mAh and Wattage"),
    "tool_ppi_calc" to mapOf("bn" to "স্ক্রিন সাইজ ও PPI হিসাব", "en" to "Screen Size & PPI Checker"),
    "tool_ppi_desc" to mapOf("bn" to "ডিসপ্লে পিক্সেল ডেনসিটি ও শার্পনেস বের করুন", "en" to "Calculate pixel density & sharpness"),
    "tool_unit_calc" to mapOf("bn" to "ডাটা / স্টোরেজ কনভার্টার", "en" to "Data & Storage Converter"),
    "tool_unit_desc" to mapOf("bn" to "MB, GB, TB সহজে কনভার্ট করুন", "en" to "Convert MB, GB, TB instantaneously"),
    "tool_currency_calc" to mapOf("bn" to "কারেন্সি কনভার্টার", "en" to "Currency Converter"),
    "tool_currency_desc" to mapOf("bn" to "BDT, USD, INR, EUR মুদ্রা হিসাব (অফলাইন রেট সহ)", "en" to "Convert BDT, USD, INR with offline rates"),

    // Offline & Sync
    "offline_manager" to mapOf("bn" to "অফলাইন সিঙ্ক ম্যানেজার", "en" to "Offline Sync Manager"),
    "offline_status_ok" to mapOf("bn" to "সম্পূর্ণ অফলাইন প্রস্তুত", "en" to "Offline Ready"),
    "last_synced" to mapOf("bn" to "সর্বশেষ সিঙ্ক:", "en" to "Last Synced:"),
    "database_size" to mapOf("bn" to "ক্যাশ ডাটা সাইজ:", "en" to "Cached Data Size:"),
    "sync_now" to mapOf("bn" to "এখনই আপডেট করুন", "en" to "Sync Now"),
    "syncing" to mapOf("bn" to "ডাটা আপডেট হচ্ছে...", "en" to "Syncing data..."),
    "auto_sync" to mapOf("bn" to "অটো সিঙ্ক (ইন্টারনেট পেলে)", "en" to "Auto Sync on Network"),
    "wifi_only_sync" to mapOf("bn" to "শুধুমাত্র ওয়াইফাই সিঙ্ক", "en" to "Wi-Fi Only Sync"),
    "clear_cache" to mapOf("bn" to "ক্যাশ পরিষ্কার করুন", "en" to "Clear Cache"),
    "cache_cleared" to mapOf("bn" to "ক্যাশ সফলভাবে সাফ করা হয়েছে", "en" to "Cache cleared successfully"),

    // Profile & Settings
    "saved_items" to mapOf("bn" to "সংরক্ষিত আইটেম", "en" to "Saved Items"),
    "tab_saved_phones" to mapOf("bn" to "ফোন", "en" to "Phones"),
    "tab_saved_comparisons" to mapOf("bn" to "তুলনা", "en" to "Comparisons"),
    "tab_saved_guides" to mapOf("bn" to "গাইড", "en" to "Guides"),
    "tab_saved_videos" to mapOf("bn" to "ভিডিও", "en" to "Videos"),
    "theme_mode" to mapOf("bn" to "থিম মোড", "en" to "Theme Mode"),
    "theme_light" to mapOf("bn" to "লাইট মোড", "en" to "Light Mode"),
    "theme_dark" to mapOf("bn" to "ডার্ক মোড", "en" to "Dark Mode"),
    "theme_system" to mapOf("bn" to "সিস্টেম ডিফল্ট", "en" to "System Default"),
    "language" to mapOf("bn" to "ভাষা (Language)", "en" to "Language (ভাষা)"),
    "notifications" to mapOf("bn" to "নোটিফিকেশন সেটিংস", "en" to "Notification Settings"),
    "notif_videos" to mapOf("bn" to "নতুন ভিডিও নোটিফিকেশন", "en" to "New Videos Alerts"),
    "notif_price" to mapOf("bn" to "মূল্য পরিবর্তন অ্যালার্ট", "en" to "Price Drop Alerts"),
    "notif_guides" to mapOf("bn" to "নতুন বায়িং গাইড", "en" to "Buying Guide Alerts"),
    "notif_news" to mapOf("bn" to "টেক নিউজ", "en" to "Tech News Alerts"),
    "about_tech_medha" to mapOf("bn" to "টেক মেধা সম্পর্কে", "en" to "About Tech Medha"),
    "about_desc" to mapOf("bn" to "টেক মেধা হলো প্রযুক্তিপ্রেমীদের জন্য এক বিশ্বস্ত অফলাইন-ফার্স্ট প্ল্যাটফর্ম। এখানে পাচ্ছেন স্মার্টফোনের খুঁটিনাটি স্পেসিফিকেশন, নিরপেক্ষ রিভিউ, সঠিক তুলনা এবং বায়িং গাইড।", "en" to "Tech Medha is your trusted offline-first technology platform providing unbiased smartphone specs, in-depth reviews, realistic comparisons, and buying guides."),
    "admin_panel" to mapOf("bn" to "অ্যাডমিন প্যানেল", "en" to "Admin Panel"),
    "admin_subtitle" to mapOf("bn" to "স্মার্টফোন, দাম, ভিডিও ও গাইড ম্যানেজ করুন", "en" to "Manage phones, prices, videos & guides"),
    "admin_add_phone" to mapOf("bn" to "নতুন স্মার্টফোন যোগ করুন", "en" to "Add New Smartphone"),
    "admin_add_video" to mapOf("bn" to "নতুন ভিডিও যোগ করুন", "en" to "Add Tech Video"),
    "admin_manage_prices" to mapOf("bn" to "মূল্য আপডেট করুন", "en" to "Update Prices"),
    "save_changes" to mapOf("bn" to "সংরক্ষণ করুন", "en" to "Save Changes"),
    "delete" to mapOf("bn" to "মুছে ফেলুন", "en" to "Delete"),
    "edit" to mapOf("bn" to "সম্পাদনা", "en" to "Edit"),
    "cancel" to mapOf("bn" to "বাতিল", "en" to "Cancel"),
    "bdt_symbol" to mapOf("bn" to "৳", "en" to "৳")
  )

  fun get(key: String, language: AppLanguage): String {
    val langKey = language.code
    return translations[key]?.get(langKey) ?: translations[key]?.get("bn") ?: key
  }
}
