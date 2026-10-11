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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.data.models.CartItem
import com.example.ui.components.VegBadge
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary
import com.example.ui.theme.CraveonPrimaryLight
import com.example.ui.theme.CraveonVegGreen
import com.example.ui.viewmodel.CraveonViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun CartScreen(
    viewModel: CraveonViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val deliveryFee by viewModel.cartDeliveryFee.collectAsState()
    val taxes by viewModel.cartTaxes.collectAsState()
    val discount by viewModel.cartDiscount.collectAsState()
    val grandTotal by viewModel.cartGrandTotal.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var couponInput by remember { mutableStateOf("") }
    var deliveryInstructions by remember { mutableStateOf("") }

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("empty_cart_view"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(CraveonPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Empty Cart",
                        tint = CraveonPrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Explore top rated restaurants and add your favorite dishes to start ordering!",
                    fontSize = 14.sp,
                    color = CraveonMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    colors = ButtonDefaults.buttonColors(containerColor = CraveonPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("browse_restaurants_button")
                ) {
                    Text(
                        text = "Browse Restaurants",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        return
    }

    val restaurantName = cartItems.first().restaurantName

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("cart_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = "Checkout Cart",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = restaurantName,
                            fontSize = 12.sp,
                            color = CraveonMuted
                        )
                    }
                }

                Text(
                    text = "Clear Cart",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CraveonPrimary,
                    modifier = Modifier
                        .clickable { viewModel.clearCart() }
                        .padding(8.dp)
                        .testTag("clear_cart_button")
                )
            }
        }

        // 2. Items List Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ORDER ITEMS (${cartItems.sumOf { it.quantity }})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CraveonMuted,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    cartItems.forEachIndexed { index, cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onIncrement = { viewModel.updateCartQuantity(cartItem.cartItemId, 1) },
                            onDecrement = { viewModel.updateCartQuantity(cartItem.cartItemId, -1) }
                        )

                        if (index < cartItems.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Coupons & Offers Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = CraveonPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Apply Coupon Code",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input Row
                    if (appliedCoupon == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = couponInput,
                                onValueChange = { couponInput = it },
                                placeholder = { Text("Enter coupon code (e.g. CRAVE50)", fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("coupon_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CraveonPrimary
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    if (couponInput.isNotBlank()) {
                                        viewModel.applyCoupon(couponInput.trim())
                                        couponInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CraveonPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("apply_coupon_button")
                            ) {
                                Text("Apply", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Available coupons chips
                        Text(
                            text = "AVAILABLE FOR YOU",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CraveonMuted,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(MockData.PROMO_OFFERS) { offer ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(CraveonPrimaryLight)
                                        .border(1.dp, CraveonPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                        .clickable { viewModel.applyCoupon(offer.code) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = offer.code,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CraveonPrimary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "• ${offer.title}",
                                            fontSize = 11.sp,
                                            color = CraveonMuted
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Applied state
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CraveonVegGreen.copy(alpha = 0.12f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CraveonVegGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "'${appliedCoupon?.code}' Applied!",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CraveonVegGreen
                                    )
                                    Text(
                                        text = "You are saving ₹${discount.toInt()} on this order",
                                        fontSize = 11.sp,
                                        color = CraveonMuted
                                    )
                                }
                            }

                            Text(
                                text = "Remove",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CraveonPrimary,
                                modifier = Modifier
                                    .clickable { viewModel.removeCoupon() }
                                    .testTag("remove_coupon_button")
                            )
                        }
                    }
                }
            }
        }

        // 4. Delivery Instructions
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DELIVERY INSTRUCTIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CraveonMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryInstructions,
                        onValueChange = { deliveryInstructions = it },
                        placeholder = { Text("e.g. Leave order at door, don't ring doorbell", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
        }

        // 5. Bill Breakdown Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "BILL SUMMARY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CraveonMuted,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BillRow(label = "Item Total", amount = "₹${subtotal.toInt()}")
                    BillRow(
                        label = "Delivery Partner Fee",
                        amount = if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                        isHighlight = deliveryFee == 0.0
                    )
                    BillRow(label = "Govt Taxes & Restaurant Packaging (5%)", amount = "₹${taxes.toInt()}")

                    if (discount > 0.0) {
                        BillRow(
                            label = "Coupon Discount (${appliedCoupon?.code})",
                            amount = "-₹${discount.toInt()}",
                            isHighlight = true
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TO PAY",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (discount > 0.0) {
                                Text(
                                    text = "Total savings ₹${discount.toInt()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CraveonVegGreen
                                )
                            }
                        }

                        Text(
                            text = "₹${grandTotal.toInt()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // 6. Checkout CTA Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.Checkout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("proceed_to_checkout_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CraveonPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total ₹${grandTotal.toInt()}",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${cartItems.sumOf { it.quantity }} items",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "Proceed to Checkout →",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            VegBadge(isVeg = cartItem.menuItem.isVeg, modifier = Modifier.padding(top = 3.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = cartItem.menuItem.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (cartItem.selectedCustomizations.isNotEmpty()) {
                    Text(
                        text = cartItem.selectedCustomizations.joinToString(" • ") { it.option.name },
                        fontSize = 11.sp,
                        color = CraveonMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "₹${cartItem.unitPriceWithCustomizations.toInt()}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Stepper Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CraveonPrimaryLight)
                    .border(1.dp, CraveonPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = CraveonPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onDecrement)
                )
                Text(
                    text = "${cartItem.quantity}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CraveonPrimary
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = CraveonPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onIncrement)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "₹${cartItem.totalPrice.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun BillRow(
    label: String,
    amount: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isHighlight) CraveonVegGreen else CraveonMuted,
            fontWeight = if (isHighlight) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = amount,
            fontSize = 13.sp,
            color = if (isHighlight) CraveonVegGreen else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}
