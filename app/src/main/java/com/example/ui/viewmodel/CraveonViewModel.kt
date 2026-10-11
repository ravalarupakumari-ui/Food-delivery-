package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockData
import com.example.data.models.Address
import com.example.data.models.CartItem
import com.example.data.models.MenuItem
import com.example.data.models.Offer
import com.example.data.models.Order
import com.example.data.models.OrderStatus
import com.example.data.models.Restaurant
import com.example.data.models.SelectedCustomization
import com.example.data.models.User
import com.example.data.repository.CraveonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Search : Screen()
    data class RestaurantDetail(val restaurantId: String) : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderTracking(val orderId: String) : Screen()
    object OrderHistory : Screen()
    object Profile : Screen()
    object AdminPanel : Screen()
}

data class ConflictAlert(
    val pendingItem: MenuItem,
    val pendingRestaurantId: String,
    val pendingRestaurantName: String,
    val pendingCustomizations: List<SelectedCustomization>,
    val pendingQuantity: Int
)

data class FilterCriteria(
    val query: String = "",
    val category: String? = null,
    val vegOnly: Boolean = false,
    val topRated: Boolean = false,
    val fastDelivery: Boolean = false,
    val offersOnly: Boolean = false
)

class CraveonViewModel(application: Application) : AndroidViewModel(application) {

    val repository = CraveonRepository(application)

    // Navigation Stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Data from Repository
    val currentUser: StateFlow<User?> = repository.currentUser
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val appliedCoupon: StateFlow<Offer?> = repository.appliedCoupon
    val currentLocation: StateFlow<String> = repository.currentLocation
    val favoriteRestaurantIds: StateFlow<Set<String>> = repository.favoriteRestaurantIds
    val orders: StateFlow<List<Order>> = repository.orders
    val restaurants: StateFlow<List<Restaurant>> = repository.restaurants

    // UI Filters & Search State
    private val _filterCriteria = MutableStateFlow(FilterCriteria())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeCategoryFilter = MutableStateFlow<String?>(null)
    val activeCategoryFilter: StateFlow<String?> = _activeCategoryFilter.asStateFlow()

    private val _vegOnlyFilter = MutableStateFlow(false)
    val vegOnlyFilter: StateFlow<Boolean> = _vegOnlyFilter.asStateFlow()

    private val _topRatedFilter = MutableStateFlow(false)
    val topRatedFilter: StateFlow<Boolean> = _topRatedFilter.asStateFlow()

    private val _fastDeliveryFilter = MutableStateFlow(false)
    val fastDeliveryFilter: StateFlow<Boolean> = _fastDeliveryFilter.asStateFlow()

    private val _offersOnlyFilter = MutableStateFlow(false)
    val offersOnlyFilter: StateFlow<Boolean> = _offersOnlyFilter.asStateFlow()

    // Modals & Dialogs State
    private val _customizingItem = MutableStateFlow<Pair<MenuItem, Restaurant>?>(null)
    val customizingItem: StateFlow<Pair<MenuItem, Restaurant>?> = _customizingItem.asStateFlow()

    private val _conflictAlert = MutableStateFlow<ConflictAlert?>(null)
    val conflictAlert: StateFlow<ConflictAlert?> = _conflictAlert.asStateFlow()

    private val _showLocationDialog = MutableStateFlow(false)
    val showLocationDialog: StateFlow<Boolean> = _showLocationDialog.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    private val _showAddAddressDialog = MutableStateFlow(false)
    val showAddAddressDialog: StateFlow<Boolean> = _showAddAddressDialog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Selected Restaurant Detail
    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant: StateFlow<Restaurant?> = _selectedRestaurant.asStateFlow()

    // Filtered Restaurants State
    val filteredRestaurants: StateFlow<List<Restaurant>> = combine(
        restaurants,
        _filterCriteria
    ) { list, criteria ->
        list.filter { rest ->
            // Search Query
            val matchesQuery = criteria.query.isBlank() ||
                rest.name.contains(criteria.query, ignoreCase = true) ||
                rest.cuisines.any { it.contains(criteria.query, ignoreCase = true) } ||
                rest.menuCategories.any { cat -> cat.items.any { it.name.contains(criteria.query, ignoreCase = true) } }

            // Category Filter
            val matchesCategory = criteria.category == null ||
                rest.cuisines.any { it.equals(criteria.category, ignoreCase = true) } ||
                rest.menuCategories.any { cat -> cat.items.any { it.name.contains(criteria.category, ignoreCase = true) } }

            // Veg Only
            val matchesVeg = !criteria.vegOnly || rest.isVegOnly || rest.menuCategories.any { cat -> cat.items.any { it.isVeg } }

            // Rating
            val matchesRating = !criteria.topRated || rest.rating >= 4.5

            // Delivery Time
            val matchesFast = !criteria.fastDelivery || rest.deliveryTimeMinutes <= 25

            // Offers
            val matchesOffers = !criteria.offersOnly || rest.offers.isNotEmpty()

            matchesQuery && matchesCategory && matchesVeg && matchesRating && matchesFast && matchesOffers
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, MockData.RESTAURANTS)

    // Cart Calculations
    val cartSubtotal: StateFlow<Double> = combine(cartItems, appliedCoupon) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val cartDeliveryFee: StateFlow<Double> = combine(cartSubtotal, appliedCoupon) { subtotal, coupon ->
        if (subtotal == 0.0) 0.0
        else if (coupon?.code == "FREE_DEL") 0.0
        else if (subtotal >= 500.0) 0.0
        else 40.0
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val cartTaxes: StateFlow<Double> = combine(cartSubtotal, cartDeliveryFee) { subtotal, _ ->
        subtotal * 0.05
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val cartDiscount: StateFlow<Double> = combine(cartSubtotal, appliedCoupon) { subtotal, coupon ->
        if (coupon == null || subtotal < coupon.minOrderValue) 0.0
        else if (coupon.discountPercent > 0) {
            val calc = (subtotal * coupon.discountPercent) / 100.0
            calc.coerceAtMost(coupon.maxDiscount)
        } else {
            coupon.flatDiscount
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val cartGrandTotal: StateFlow<Double> = combine(
        combine(cartSubtotal, cartDeliveryFee) { s, d -> s + d },
        combine(cartTaxes, cartDiscount) { t, disc -> t - disc }
    ) { part1, part2 ->
        (part1 + part2).coerceAtLeast(0.0)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // Navigation Controls
    fun navigateTo(screen: Screen) {
        val current = _screenStack.value
        _screenStack.value = current + screen
        _currentScreen.value = screen

        if (screen is Screen.RestaurantDetail) {
            _selectedRestaurant.value = MockData.RESTAURANTS.find { it.id == screen.restaurantId }
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            val updated = current.dropLast(1)
            _screenStack.value = updated
            _currentScreen.value = updated.last()

            val top = updated.last()
            if (top is Screen.RestaurantDetail) {
                _selectedRestaurant.value = MockData.RESTAURANTS.find { it.id == top.restaurantId }
            }
            return true
        }
        return false
    }

    fun navigateToHome() {
        _screenStack.value = listOf(Screen.Home)
        _currentScreen.value = Screen.Home
    }

    // Search and Filter updates
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _filterCriteria.update { it.copy(query = query) }
    }

    fun toggleCategoryFilter(category: String) {
        val next = if (_activeCategoryFilter.value == category) null else category
        _activeCategoryFilter.value = next
        _filterCriteria.update { it.copy(category = next) }
    }

    fun toggleVegOnly() {
        val next = !_vegOnlyFilter.value
        _vegOnlyFilter.value = next
        _filterCriteria.update { it.copy(vegOnly = next) }
    }

    fun toggleTopRated() {
        val next = !_topRatedFilter.value
        _topRatedFilter.value = next
        _filterCriteria.update { it.copy(topRated = next) }
    }

    fun toggleFastDelivery() {
        val next = !_fastDeliveryFilter.value
        _fastDeliveryFilter.value = next
        _filterCriteria.update { it.copy(fastDelivery = next) }
    }

    fun toggleOffersOnly() {
        val next = !_offersOnlyFilter.value
        _offersOnlyFilter.value = next
        _filterCriteria.update { it.copy(offersOnly = next) }
    }

    fun clearAllFilters() {
        _searchQuery.value = ""
        _activeCategoryFilter.value = null
        _vegOnlyFilter.value = false
        _topRatedFilter.value = false
        _fastDeliveryFilter.value = false
        _offersOnlyFilter.value = false
        _filterCriteria.value = FilterCriteria()
    }

    // Cart Actions
    fun onAddFoodItem(item: MenuItem, restaurant: Restaurant) {
        if (item.customizationGroups.isNotEmpty()) {
            _customizingItem.value = Pair(item, restaurant)
        } else {
            val success = repository.addToCart(
                menuItem = item,
                restaurantId = restaurant.id,
                restaurantName = restaurant.name,
                selectedCustomizations = emptyList(),
                quantity = 1
            )
            if (!success) {
                _conflictAlert.value = ConflictAlert(
                    pendingItem = item,
                    pendingRestaurantId = restaurant.id,
                    pendingRestaurantName = restaurant.name,
                    pendingCustomizations = emptyList(),
                    pendingQuantity = 1
                )
            } else {
                showSnackbar("Added '${item.name}' to cart")
            }
        }
    }

    fun onConfirmCustomization(
        item: MenuItem,
        restaurant: Restaurant,
        selectedCustomizations: List<SelectedCustomization>,
        quantity: Int
    ) {
        val success = repository.addToCart(
            menuItem = item,
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            selectedCustomizations = selectedCustomizations,
            quantity = quantity
        )
        _customizingItem.value = null
        if (!success) {
            _conflictAlert.value = ConflictAlert(
                pendingItem = item,
                pendingRestaurantId = restaurant.id,
                pendingRestaurantName = restaurant.name,
                pendingCustomizations = selectedCustomizations,
                pendingQuantity = quantity
            )
        } else {
            showSnackbar("Added '${item.name}' to cart")
        }
    }

    fun resolveConflictAndAdd(alert: ConflictAlert) {
        repository.clearAndAddToCart(
            menuItem = alert.pendingItem,
            restaurantId = alert.pendingRestaurantId,
            restaurantName = alert.pendingRestaurantName,
            selectedCustomizations = alert.pendingCustomizations,
            quantity = alert.pendingQuantity
        )
        _conflictAlert.value = null
        showSnackbar("Cart cleared and '${alert.pendingItem.name}' added")
    }

    fun dismissConflictAlert() {
        _conflictAlert.value = null
    }

    fun dismissCustomizingItem() {
        _customizingItem.value = null
    }

    fun updateCartQuantity(cartItemId: String, delta: Int) {
        repository.updateCartItemQuantity(cartItemId, delta)
    }

    fun applyCoupon(code: String) {
        val (success, msg) = repository.applyCoupon(code)
        showSnackbar(msg)
    }

    fun removeCoupon() {
        repository.removeCoupon()
        showSnackbar("Coupon removed")
    }

    fun clearCart() {
        repository.clearCart()
        showSnackbar("Cart cleared")
    }

    // Checkout & Order Placement
    fun placeOrder(address: Address, paymentMethod: String, instructions: String) {
        viewModelScope.launch {
            val order = repository.placeOrder(
                deliveryAddress = address,
                paymentMethod = paymentMethod,
                deliveryInstructions = instructions
            )
            if (order != null) {
                showSnackbar("Order placed successfully! (#${order.id})")
                navigateTo(Screen.OrderTracking(order.id))
            } else {
                showSnackbar("Could not place order. Please check cart.")
            }
        }
    }

    fun reorder(order: Order) {
        repository.reorder(order)
        showSnackbar("Items from #${order.orderNumber} added to cart!")
        navigateTo(Screen.Cart)
    }

    // Location
    fun openLocationDialog() {
        _showLocationDialog.value = true
    }

    fun selectLocation(location: String) {
        repository.setLocation(location)
        _showLocationDialog.value = false
        showSnackbar("Delivery location updated to $location")
    }

    fun dismissLocationDialog() {
        _showLocationDialog.value = false
    }

    // Auth & Accounts
    fun openAuthDialog() {
        _showAuthDialog.value = true
    }

    fun dismissAuthDialog() {
        _showAuthDialog.value = false
    }

    fun switchUser(userId: String) {
        repository.switchUser(userId)
        _showAuthDialog.value = false
        showSnackbar("Switched user profile")
    }

    fun login(email: String, name: String) {
        val user = repository.login(email, name)
        _showAuthDialog.value = false
        showSnackbar("Welcome back, ${user.name}!")
    }

    fun logout() {
        repository.logout()
        showSnackbar("Logged out successfully")
    }

    // Address Management
    fun openAddAddressDialog() {
        _showAddAddressDialog.value = true
    }

    fun dismissAddAddressDialog() {
        _showAddAddressDialog.value = false
    }

    fun saveAddress(label: String, flat: String, street: String, landmark: String) {
        repository.addAddress(label, flat, street, landmark)
        _showAddAddressDialog.value = false
        showSnackbar("Saved new address: $label")
    }

    fun toggleFavorite(restaurantId: String) {
        repository.toggleFavorite(restaurantId)
    }

    // Admin Panel Controls
    fun setOrderStatus(orderId: String, newStatus: OrderStatus) {
        repository.setOrderStatus(orderId, newStatus)
        showSnackbar("Order status updated to ${newStatus.displayLabel}")
    }

    fun toggleRestaurantAvailability(restaurantId: String) {
        repository.toggleRestaurantAvailability(restaurantId)
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }
}
