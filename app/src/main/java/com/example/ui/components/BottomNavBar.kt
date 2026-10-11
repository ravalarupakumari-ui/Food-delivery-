package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary
import com.example.ui.theme.CraveonPrimaryLight
import com.example.ui.viewmodel.Screen

@Composable
fun CraveonBottomNavBar(
    currentScreen: Screen,
    cartItemCount: Int,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Home Tab
        val isHome = currentScreen is Screen.Home
        NavigationBarItem(
            selected = isHome,
            onClick = { onTabSelected(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Delivery",
                    fontSize = 11.sp,
                    fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CraveonPrimary,
                selectedTextColor = CraveonPrimary,
                indicatorColor = CraveonPrimaryLight,
                unselectedIconColor = CraveonMuted,
                unselectedTextColor = CraveonMuted
            ),
            modifier = Modifier.testTag("nav_home")
        )

        // Search Tab
        val isSearch = currentScreen is Screen.Search
        NavigationBarItem(
            selected = isSearch,
            onClick = { onTabSelected(Screen.Search) },
            icon = {
                Icon(
                    imageVector = if (isSearch) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Search",
                    fontSize = 11.sp,
                    fontWeight = if (isSearch) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CraveonPrimary,
                selectedTextColor = CraveonPrimary,
                indicatorColor = CraveonPrimaryLight,
                unselectedIconColor = CraveonMuted,
                unselectedTextColor = CraveonMuted
            ),
            modifier = Modifier.testTag("nav_search")
        )

        // Cart Tab
        val isCart = currentScreen is Screen.Cart
        NavigationBarItem(
            selected = isCart,
            onClick = { onTabSelected(Screen.Cart) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = CraveonPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = cartItemCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isCart) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                        contentDescription = "Cart",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "Cart",
                    fontSize = 11.sp,
                    fontWeight = if (isCart) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CraveonPrimary,
                selectedTextColor = CraveonPrimary,
                indicatorColor = CraveonPrimaryLight,
                unselectedIconColor = CraveonMuted,
                unselectedTextColor = CraveonMuted
            ),
            modifier = Modifier.testTag("nav_cart")
        )

        // Orders Tab
        val isOrders = currentScreen is Screen.OrderHistory
        NavigationBarItem(
            selected = isOrders,
            onClick = { onTabSelected(Screen.OrderHistory) },
            icon = {
                Icon(
                    imageVector = if (isOrders) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                    contentDescription = "Orders",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Orders",
                    fontSize = 11.sp,
                    fontWeight = if (isOrders) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CraveonPrimary,
                selectedTextColor = CraveonPrimary,
                indicatorColor = CraveonPrimaryLight,
                unselectedIconColor = CraveonMuted,
                unselectedTextColor = CraveonMuted
            ),
            modifier = Modifier.testTag("nav_orders")
        )

        // Profile Tab
        val isProfile = currentScreen is Screen.Profile
        NavigationBarItem(
            selected = isProfile,
            onClick = { onTabSelected(Screen.Profile) },
            icon = {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 11.sp,
                    fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CraveonPrimary,
                selectedTextColor = CraveonPrimary,
                indicatorColor = CraveonPrimaryLight,
                unselectedIconColor = CraveonMuted,
                unselectedTextColor = CraveonMuted
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
