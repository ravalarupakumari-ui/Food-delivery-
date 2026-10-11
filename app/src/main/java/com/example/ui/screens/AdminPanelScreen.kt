package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.data.models.OrderStatus
import com.example.ui.theme.CraveonGold
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary
import com.example.ui.theme.CraveonPrimaryLight
import com.example.ui.theme.CraveonVegGreen
import com.example.ui.viewmodel.CraveonViewModel

@Composable
fun AdminPanelScreen(
    viewModel: CraveonViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val restaurants by viewModel.restaurants.collectAsState()

    val totalSimulatedRevenue = orders.sumOf { it.totalAmount } + 48250.0 // Base simulated analytics

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("admin_panel_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Column {
                    Text(
                        text = "Client Demo Admin Panel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Simulated Metrics & Control Board",
                        fontSize = 11.sp,
                        color = CraveonMuted
                    )
                }
            }
        }

        // Transparency Disclaimer Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CraveonPrimaryLight)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CraveonPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Demo Notice: All revenue metrics and statistics displayed are simulated for evaluation purposes. In production, this dashboard is guarded by server-side Firebase/OAuth security.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Metrics Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "SIMULATED PERFORMANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CraveonMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "Simulated GMV",
                        value = "₹${totalSimulatedRevenue.toInt()}",
                        icon = Icons.Default.AttachMoney,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Orders Logged",
                        value = "${orders.size + 142}",
                        icon = Icons.Default.Receipt,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "Active Restaurants",
                        value = "${restaurants.count { it.isAvailable }}",
                        icon = Icons.Default.Store,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Active Coupons",
                        value = "${MockData.PROMO_OFFERS.size}",
                        icon = Icons.Default.LocalOffer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Live Order Status Controllers
        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)) {
                Text(
                    text = "ACTIVE ORDER STATUS CONTROLLER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CraveonMuted,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Select any live order to manually switch delivery states:",
                    fontSize = 12.sp,
                    color = CraveonMuted
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Text(
                    text = "No orders placed yet. Place an order from the cart to test.",
                    fontSize = 13.sp,
                    color = CraveonMuted,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            items(orders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Order #${order.id} • ${order.restaurantName}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = order.status.displayLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CraveonPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(OrderStatus.values()) { status ->
                                val isCurrent = order.status == status
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isCurrent) CraveonPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { viewModel.setOrderStatus(order.id, status) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = status.displayLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Restaurant Availability Toggle
        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)) {
                Text(
                    text = "RESTAURANT AVAILABILITY TOGGLES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CraveonMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        restaurants.take(6).forEachIndexed { index, rest ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rest.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (rest.isAvailable) "Accepting orders" else "Temporarily closed",
                                        fontSize = 11.sp,
                                        color = if (rest.isAvailable) CraveonVegGreen else Color.Red
                                    )
                                }

                                Switch(
                                    checked = rest.isAvailable,
                                    onCheckedChange = { viewModel.toggleRestaurantAvailability(rest.id) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = CraveonVegGreen
                                    )
                                )
                            }
                            if (index < 5) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CraveonPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = CraveonMuted
            )
        }
    }
}
