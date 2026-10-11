package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MockData
import com.example.data.models.CategoryShortcut
import com.example.data.models.Offer
import com.example.data.models.Restaurant
import com.example.ui.components.RestaurantCard
import com.example.ui.components.VegBadge
import com.example.ui.theme.CraveonGold
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary
import com.example.ui.theme.CraveonPrimaryLight
import com.example.ui.theme.CraveonStarGreen
import com.example.ui.theme.CraveonVegGreen
import com.example.ui.viewmodel.CraveonViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: CraveonViewModel,
    modifier: Modifier = Modifier
) {
    val filteredRestaurants by viewModel.filteredRestaurants.collectAsState()
    val activeCategory by viewModel.activeCategoryFilter.collectAsState()
    val vegOnly by viewModel.vegOnlyFilter.collectAsState()
    val topRated by viewModel.topRatedFilter.collectAsState()
    val fastDelivery by viewModel.fastDeliveryFilter.collectAsState()
    val offersOnly by viewModel.offersOnlyFilter.collectAsState()
    val favoriteIds by viewModel.favoriteRestaurantIds.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Search Bar Shortcut Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.navigateTo(Screen.Search) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CraveonPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search for biryani, pizza, or dishes...",
                        fontSize = 14.sp,
                        color = CraveonMuted
                    )
                }
            }
        }

        // 2. Promotional Banners Carousel
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(MockData.PROMO_OFFERS) { offer ->
                        PromoBannerCard(
                            offer = offer,
                            onBannerClick = {
                                viewModel.applyCoupon(offer.code)
                            }
                        )
                    }
                }
            }
        }

        // 3. Food Category Shortcuts ("What's on your mind?")
        item {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WHAT'S ON YOUR MIND?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CraveonMuted,
                        letterSpacing = 1.sp
                    )
                    if (activeCategory != null) {
                        Text(
                            text = "Clear filter",
                            color = CraveonPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.toggleCategoryFilter(activeCategory!!) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(MockData.CATEGORY_SHORTCUTS) { category ->
                        val isSelected = activeCategory == category.name
                        CategoryShortcutItem(
                            category = category,
                            isSelected = isSelected,
                            onClick = { viewModel.toggleCategoryFilter(category.name) }
                        )
                    }
                }
            }
        }

        // 4. Filter Pills Strip
        item {
            Column(modifier = Modifier.padding(top = 22.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterPill(
                        label = "Pure Veg",
                        icon = { VegBadge(isVeg = true, size = 12.dp) },
                        isSelected = vegOnly,
                        onClick = { viewModel.toggleVegOnly() }
                    )
                    FilterPill(
                        label = "Rating 4.5+",
                        icon = { Icon(Icons.Default.Star, contentDescription = null, tint = if (topRated) Color.White else CraveonGold, modifier = Modifier.size(14.dp)) },
                        isSelected = topRated,
                        onClick = { viewModel.toggleTopRated() }
                    )
                    FilterPill(
                        label = "Fast Delivery",
                        icon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = if (fastDelivery) Color.White else CraveonPrimary, modifier = Modifier.size(14.dp)) },
                        isSelected = fastDelivery,
                        onClick = { viewModel.toggleFastDelivery() }
                    )
                    FilterPill(
                        label = "Great Offers",
                        icon = { Icon(Icons.Default.LocalOffer, contentDescription = null, tint = if (offersOnly) Color.White else CraveonPrimary, modifier = Modifier.size(14.dp)) },
                        isSelected = offersOnly,
                        onClick = { viewModel.toggleOffersOnly() }
                    )
                }
            }
        }

        // 5. Popular Dishes Shortcut ("Quick Picks")
        item {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = "POPULAR DISHES NEAR YOU",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CraveonMuted,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val quickDishes = listOf(
                        Pair(MockData.RESTAURANTS[0].menuCategories[0].items[0], MockData.RESTAURANTS[0]),
                        Pair(MockData.RESTAURANTS[1].menuCategories[0].items[0], MockData.RESTAURANTS[1]),
                        Pair(MockData.RESTAURANTS[2].menuCategories[0].items[0], MockData.RESTAURANTS[2]),
                        Pair(MockData.RESTAURANTS[3].menuCategories[0].items[0], MockData.RESTAURANTS[3]),
                        Pair(MockData.RESTAURANTS[4].menuCategories[0].items[0], MockData.RESTAURANTS[4])
                    )

                    items(quickDishes) { (dish, rest) ->
                        QuickDishCard(
                            dish = dish,
                            restaurant = rest,
                            onDishClick = { viewModel.navigateTo(Screen.RestaurantDetail(rest.id)) },
                            onAddClick = { viewModel.onAddFoodItem(dish, rest) }
                        )
                    }
                }
            }
        }

        // 6. Restaurant Section Header
        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp, bottom = 12.dp)) {
                Text(
                    text = if (activeCategory != null) "Restaurants serving $activeCategory" else "ALL RESTAURANTS",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${filteredRestaurants.size} restaurants delivering to $currentLocation",
                    fontSize = 13.sp,
                    color = CraveonMuted
                )
            }
        }

        // 7. Restaurant Cards List
        if (filteredRestaurants.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No restaurants match your filters",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try clearing filters to see all restaurants",
                            fontSize = 13.sp,
                            color = CraveonMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.clearAllFilters() },
                            colors = ButtonDefaults.buttonColors(containerColor = CraveonPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Reset Filters", color = Color.White)
                        }
                    }
                }
            }
        } else {
            items(filteredRestaurants) { restaurant ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    RestaurantCard(
                        restaurant = restaurant,
                        isFavorite = favoriteIds.contains(restaurant.id),
                        onCardClick = { viewModel.navigateTo(Screen.RestaurantDetail(restaurant.id)) },
                        onFavoriteClick = { viewModel.toggleFavorite(restaurant.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PromoBannerCard(
    offer: Offer,
    onBannerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(125.dp)
            .clickable(onClick = onBannerClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CraveonPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CraveonPrimary, Color(0xFFE64A19), Color(0xFFC2185B))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "USE CODE ${offer.code}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = offer.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = offer.description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryShortcutItem(
    category: CategoryShortcut,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    color = if (isSelected) CraveonPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (isSelected) CraveonPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FilterPill(
    label: String,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CraveonPrimary else MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (isSelected) CraveonPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon()
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun QuickDishCard(
    dish: com.example.data.models.MenuItem,
    restaurant: Restaurant,
    onDishClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onDishClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(100.dp)) {
                AsyncImage(
                    model = dish.imageUrl,
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {
                    VegBadge(isVeg = dish.isVeg)
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = dish.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = restaurant.name,
                    fontSize = 11.sp,
                    color = CraveonMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${dish.price.toInt()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CraveonPrimaryLight)
                            .clickable(onClick = onAddClick)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ADD",
                            color = CraveonPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
