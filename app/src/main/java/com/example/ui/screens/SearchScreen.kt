package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MockData
import com.example.data.models.MenuItem
import com.example.data.models.Restaurant
import com.example.ui.components.RestaurantCard
import com.example.ui.components.VegBadge
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary
import com.example.ui.theme.CraveonPrimaryLight
import com.example.ui.viewmodel.CraveonViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun SearchScreen(
    viewModel: CraveonViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allRestaurants by viewModel.restaurants.collectAsState()
    val favoriteIds by viewModel.favoriteRestaurantIds.collectAsState()

    // Find matching restaurants and dishes
    val matchingRestaurants = if (searchQuery.isBlank()) emptyList() else {
        allRestaurants.filter { r ->
            r.name.contains(searchQuery, ignoreCase = true) ||
                r.cuisines.any { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    val matchingDishes = if (searchQuery.isBlank()) emptyList() else {
        allRestaurants.flatMap { r ->
            r.menuCategories.flatMap { cat ->
                cat.items.filter { it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true) }
                    .map { Pair(it, r) }
            }
        }
    }

    val trendingKeywords = listOf("Biryani", "Butter Chicken", "Pizza", "Dosa", "Burger", "Dal Makhani", "Momos", "Kathi Roll", "Chai", "Waffles")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("search_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Search Input Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search dishes, restaurants, cuisines...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CraveonPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CraveonPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )
            }
        }

        // Trending / Recent Suggestions when query is blank
        if (searchQuery.isBlank()) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = CraveonMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "POPULAR SEARCHES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CraveonMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Keyword Chips Wrap/Row
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(trendingKeywords) { kw ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setSearchQuery(kw) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = kw,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "EXPLORE BY CUISINE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CraveonMuted,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val cuisines = listOf(
                        Pair("North Indian", "https://images.unsplash.com/photo-1517244683847-7456b63c5969?w=300&q=80"),
                        Pair("Biryani", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=300&q=80"),
                        Pair("South Indian", "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=300&q=80"),
                        Pair("Pizzas", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300&q=80"),
                        Pair("Burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300&q=80"),
                        Pair("Chinese", "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=300&q=80")
                    )

                    cuisines.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { (name, img) ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .clickable { viewModel.setSearchQuery(name) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = img,
                                            contentDescription = name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        } else {
            // When user typed something
            if (matchingRestaurants.isEmpty() && matchingDishes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No results found for '$searchQuery'",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try searching for biryani, dosa, pizza, or burger",
                                fontSize = 13.sp,
                                color = CraveonMuted
                            )
                        }
                    }
                }
            } else {
                // Matching Dishes
                if (matchingDishes.isNotEmpty()) {
                    item {
                        Text(
                            text = "DISHES (${matchingDishes.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CraveonMuted,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(matchingDishes.take(8)) { (dish, rest) ->
                        SearchDishResultItem(
                            dish = dish,
                            restaurant = rest,
                            onDishClick = { viewModel.navigateTo(Screen.RestaurantDetail(rest.id)) },
                            onAddClick = { viewModel.onAddFoodItem(dish, rest) }
                        )
                    }
                }

                // Matching Restaurants
                if (matchingRestaurants.isNotEmpty()) {
                    item {
                        Text(
                            text = "RESTAURANTS (${matchingRestaurants.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CraveonMuted,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
                        )
                    }

                    items(matchingRestaurants) { restaurant ->
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
    }
}

@Composable
fun SearchDishResultItem(
    dish: MenuItem,
    restaurant: Restaurant,
    onDishClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onDishClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = dish.imageUrl,
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VegBadge(isVeg = dish.isVeg, size = 12.dp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dish.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = "From ${restaurant.name}",
                        fontSize = 12.sp,
                        color = CraveonMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "₹${dish.price.toInt()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CraveonPrimaryLight)
                    .clickable(onClick = onAddClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "ADD",
                    color = CraveonPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
