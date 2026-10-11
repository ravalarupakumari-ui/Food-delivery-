package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddAddressDialog
import com.example.ui.components.AuthDialog
import com.example.ui.components.ConflictDialog
import com.example.ui.components.CraveonBottomNavBar
import com.example.ui.components.CraveonTopBar
import com.example.ui.components.CustomizationDialog
import com.example.ui.components.LocationDialog
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RestaurantDetailScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CraveonViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: CraveonViewModel = viewModel()
                CraveonApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CraveonApp(viewModel: CraveonViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    val customizingItem by viewModel.customizingItem.collectAsState()
    val conflictAlert by viewModel.conflictAlert.collectAsState()
    val showLocationDialog by viewModel.showLocationDialog.collectAsState()
    val showAuthDialog by viewModel.showAuthDialog.collectAsState()
    val showAddAddressDialog by viewModel.showAddAddressDialog.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val selectedRestaurant by viewModel.selectedRestaurant.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Snackbars
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnackbar()
        }
    }

    // Predictive / System Back Handling
    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    val totalCartCount = cartItems.sumOf { it.quantity }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            if (currentScreen is Screen.Home) {
                CraveonTopBar(
                    currentLocation = currentLocation,
                    currentUser = currentUser,
                    onLocationClick = { viewModel.openLocationDialog() },
                    onSearchClick = { viewModel.navigateTo(Screen.Search) },
                    onProfileClick = { viewModel.navigateTo(Screen.Profile) },
                    onAdminClick = { viewModel.navigateTo(Screen.AdminPanel) }
                )
            }
        },
        bottomBar = {
            CraveonBottomNavBar(
                currentScreen = currentScreen,
                cartItemCount = totalCartCount,
                onTabSelected = { screen -> viewModel.navigateTo(screen) },
                modifier = Modifier.navigationBarsPadding()
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(viewModel = viewModel)
                }
                is Screen.Search -> {
                    SearchScreen(viewModel = viewModel)
                }
                is Screen.RestaurantDetail -> {
                    val rest = selectedRestaurant ?: com.example.data.MockData.RESTAURANTS.first()
                    RestaurantDetailScreen(restaurant = rest, viewModel = viewModel)
                }
                is Screen.Cart -> {
                    CartScreen(viewModel = viewModel)
                }
                is Screen.Checkout -> {
                    CheckoutScreen(viewModel = viewModel)
                }
                is Screen.OrderTracking -> {
                    OrderTrackingScreen(orderId = screen.orderId, viewModel = viewModel)
                }
                is Screen.OrderHistory -> {
                    OrderHistoryScreen(viewModel = viewModel)
                }
                is Screen.Profile -> {
                    ProfileScreen(viewModel = viewModel)
                }
                is Screen.AdminPanel -> {
                    AdminPanelScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Modal Overlays
    customizingItem?.let { (item, rest) ->
        CustomizationDialog(
            item = item,
            restaurant = rest,
            onDismiss = { viewModel.dismissCustomizingItem() },
            onConfirm = { selectedCustomizations, qty ->
                viewModel.onConfirmCustomization(item, rest, selectedCustomizations, qty)
            }
        )
    }

    conflictAlert?.let { alert ->
        ConflictDialog(
            alert = alert,
            onDismiss = { viewModel.dismissConflictAlert() },
            onReplace = { viewModel.resolveConflictAndAdd(alert) }
        )
    }

    if (showLocationDialog) {
        LocationDialog(
            currentLocation = currentLocation,
            onDismiss = { viewModel.dismissLocationDialog() },
            onSelectLocation = { viewModel.selectLocation(it) }
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            currentUser = currentUser,
            onDismiss = { viewModel.dismissAuthDialog() },
            onSwitchUser = { viewModel.switchUser(it) },
            onLogin = { email, name -> viewModel.login(email, name) }
        )
    }

    if (showAddAddressDialog) {
        AddAddressDialog(
            onDismiss = { viewModel.dismissAddAddressDialog() },
            onSave = { label, flat, street, landmark ->
                viewModel.saveAddress(label, flat, street, landmark)
            }
        )
    }
}
