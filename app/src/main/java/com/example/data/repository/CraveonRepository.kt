package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.MockData
import com.example.data.models.Address
import com.example.data.models.CartItem
import com.example.data.models.Offer
import com.example.data.models.Order
import com.example.data.models.OrderStatus
import com.example.data.models.Restaurant
import com.example.data.models.SelectedCustomization
import com.example.data.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class CraveonRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("craveon_prefs_v2", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.Default)

    // Demo Initial Users
    private val defaultUsers = listOf(
        User(
            id = "user_rahul",
            name = "Rahul Sharma",
            email = "rahul.sharma@example.com",
            phone = "+91 98112 34567",
            savedAddresses = listOf(
                Address("addr_1", "Home", "Flat 402, Tower B", "Central Park Resorts, Sector 48", "Near Subhash Chowk", "Gurgaon", isDefault = true),
                Address("addr_2", "Work", "Tower 10A, 8th Floor", "DLF Cyber City, Phase 2", "Opposite IndusInd Bank", "Gurgaon")
            )
        ),
        User(
            id = "user_priya",
            name = "Priya Patel",
            email = "priya.patel@example.com",
            phone = "+91 98980 12345",
            savedAddresses = listOf(
                Address("addr_3", "Home", "Villa 12", "Heritage City, MG Road", "Near Metro Station", "Gurgaon", isDefault = true)
            )
        )
    )

    // State Flows
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<Offer?>(null)
    val appliedCoupon: StateFlow<Offer?> = _appliedCoupon.asStateFlow()

    private val _currentLocation = MutableStateFlow("Sector 45, Gurgaon")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()

    private val _favoriteRestaurantIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteRestaurantIds: StateFlow<Set<String>> = _favoriteRestaurantIds.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(MockData.RESTAURANTS)
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    init {
        loadPersistedState()
    }

    private fun loadPersistedState() {
        val savedUserId = prefs.getString("active_user_id", "user_rahul") ?: "user_rahul"
        val user = defaultUsers.find { it.id == savedUserId } ?: defaultUsers.first()
        _currentUser.value = user

        val location = prefs.getString("selected_location", "Sector 45, Gurgaon") ?: "Sector 45, Gurgaon"
        _currentLocation.value = location

        loadOrdersForUser(user.id)
    }

    fun loadOrdersForUser(userId: String) {
        val ordersJson = prefs.getString("orders_$userId", null)
        if (ordersJson != null) {
            try {
                val list = mutableListOf<Order>()
                val array = JSONArray(ordersJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(parseOrderFromJson(obj))
                }
                _orders.value = list
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // If no saved orders yet, add one authentic initial past order for demo
        val initialOrder = createDemoPastOrder(userId)
        _orders.value = listOf(initialOrder)
        saveOrdersForUser(userId, _orders.value)
    }

    private fun createDemoPastOrder(userId: String): Order {
        val rest = MockData.RESTAURANTS.first()
        val item = rest.menuCategories.first().items.first()
        val cartItem = CartItem(
            cartItemId = "ci_demo_1",
            menuItem = item,
            restaurantId = rest.id,
            restaurantName = rest.name,
            quantity = 2,
            selectedCustomizations = emptyList(),
            unitPriceWithCustomizations = item.price
        )
        val user = _currentUser.value ?: defaultUsers.first()
        val addr = user.savedAddresses.firstOrNull() ?: Address("a_0", "Home", "Flat 101", "Green Glen Layout", "", "Gurgaon", true)
        return Order(
            id = "CRV-94812",
            orderNumber = "#CRV-94812",
            userId = userId,
            userName = user.name,
            userPhone = user.phone,
            restaurantId = rest.id,
            restaurantName = rest.name,
            restaurantImage = rest.coverImageUrl,
            items = listOf(cartItem),
            subtotal = item.price * 2,
            deliveryFee = 0.0,
            taxes = (item.price * 2) * 0.05,
            discount = 100.0,
            totalAmount = (item.price * 2) * 1.05 - 100.0,
            appliedCouponCode = "CRAVE50",
            status = OrderStatus.DELIVERED,
            deliveryAddress = addr,
            paymentMethod = "UPI / Google Pay",
            timestamp = System.currentTimeMillis() - 86400000L // Yesterday
        )
    }

    private fun saveOrdersForUser(userId: String, list: List<Order>) {
        val array = JSONArray()
        for (order in list) {
            array.put(serializeOrderToJson(order))
        }
        prefs.edit().putString("orders_$userId", array.toString()).apply()
    }

    private fun serializeOrderToJson(order: Order): JSONObject {
        val obj = JSONObject()
        obj.put("id", order.id)
        obj.put("orderNumber", order.orderNumber)
        obj.put("userId", order.userId)
        obj.put("userName", order.userName)
        obj.put("userPhone", order.userPhone)
        obj.put("restaurantId", order.restaurantId)
        obj.put("restaurantName", order.restaurantName)
        obj.put("restaurantImage", order.restaurantImage)
        obj.put("subtotal", order.subtotal)
        obj.put("deliveryFee", order.deliveryFee)
        obj.put("taxes", order.taxes)
        obj.put("discount", order.discount)
        obj.put("totalAmount", order.totalAmount)
        obj.put("appliedCouponCode", order.appliedCouponCode ?: "")
        obj.put("status", order.status.name)
        obj.put("paymentMethod", order.paymentMethod)
        obj.put("deliveryInstructions", order.deliveryInstructions)
        obj.put("timestamp", order.timestamp)
        obj.put("driverName", order.simulatedDriverName)
        obj.put("driverPhone", order.simulatedDriverPhone)

        // Address
        val addrObj = JSONObject()
        addrObj.put("id", order.deliveryAddress.id)
        addrObj.put("label", order.deliveryAddress.label)
        addrObj.put("flatNumber", order.deliveryAddress.flatNumber)
        addrObj.put("street", order.deliveryAddress.street)
        addrObj.put("landmark", order.deliveryAddress.landmark)
        addrObj.put("city", order.deliveryAddress.city)
        obj.put("address", addrObj)

        // Items
        val itemsArray = JSONArray()
        for (item in order.items) {
            val iObj = JSONObject()
            iObj.put("cartItemId", item.cartItemId)
            iObj.put("menuItemId", item.menuItem.id)
            iObj.put("name", item.menuItem.name)
            iObj.put("price", item.menuItem.price)
            iObj.put("imageUrl", item.menuItem.imageUrl)
            iObj.put("isVeg", item.menuItem.isVeg)
            iObj.put("quantity", item.quantity)
            iObj.put("unitPrice", item.unitPriceWithCustomizations)
            itemsArray.put(iObj)
        }
        obj.put("items", itemsArray)
        return obj
    }

    private fun parseOrderFromJson(obj: JSONObject): Order {
        val statusStr = obj.optString("status", OrderStatus.DELIVERED.name)
        val status = try { OrderStatus.valueOf(statusStr) } catch (e: Exception) { OrderStatus.DELIVERED }
        val addrObj = obj.getJSONObject("address")
        val addr = Address(
            id = addrObj.optString("id", "a1"),
            label = addrObj.optString("label", "Home"),
            flatNumber = addrObj.optString("flatNumber", ""),
            street = addrObj.optString("street", ""),
            landmark = addrObj.optString("landmark", ""),
            city = addrObj.optString("city", "Gurgaon")
        )

        val itemsList = mutableListOf<CartItem>()
        val itemsArr = obj.optJSONArray("items")
        if (itemsArr != null) {
            for (j in 0 until itemsArr.length()) {
                val iObj = itemsArr.getJSONObject(j)
                val mItem = com.example.data.models.MenuItem(
                    id = iObj.optString("menuItemId", "m1"),
                    name = iObj.optString("name", "Dish"),
                    description = "",
                    price = iObj.optDouble("price", 100.0),
                    imageUrl = iObj.optString("imageUrl", ""),
                    isVeg = iObj.optBoolean("isVeg", true)
                )
                itemsList.add(
                    CartItem(
                        cartItemId = iObj.optString("cartItemId", UUID.randomUUID().toString()),
                        menuItem = mItem,
                        restaurantId = obj.optString("restaurantId", ""),
                        restaurantName = obj.optString("restaurantName", ""),
                        quantity = iObj.optInt("quantity", 1),
                        selectedCustomizations = emptyList(),
                        unitPriceWithCustomizations = iObj.optDouble("unitPrice", mItem.price)
                    )
                )
            }
        }

        return Order(
            id = obj.getString("id"),
            orderNumber = obj.optString("orderNumber", "#" + obj.getString("id")),
            userId = obj.optString("userId", "user_rahul"),
            userName = obj.optString("userName", "Rahul Sharma"),
            userPhone = obj.optString("userPhone", "+91 98112 34567"),
            restaurantId = obj.getString("restaurantId"),
            restaurantName = obj.getString("restaurantName"),
            restaurantImage = obj.optString("restaurantImage", ""),
            items = itemsList,
            subtotal = obj.optDouble("subtotal", 0.0),
            deliveryFee = obj.optDouble("deliveryFee", 0.0),
            taxes = obj.optDouble("taxes", 0.0),
            discount = obj.optDouble("discount", 0.0),
            totalAmount = obj.optDouble("totalAmount", 0.0),
            appliedCouponCode = obj.optString("appliedCouponCode").ifBlank { null },
            status = status,
            deliveryAddress = addr,
            paymentMethod = obj.optString("paymentMethod", "UPI"),
            deliveryInstructions = obj.optString("deliveryInstructions", ""),
            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
            simulatedDriverName = obj.optString("driverName", "Ramesh Kumar"),
            simulatedDriverPhone = obj.optString("driverPhone", "+91 98765 43210")
        )
    }

    // User Operations
    fun switchUser(userId: String) {
        val user = defaultUsers.find { it.id == userId } ?: return
        _currentUser.value = user
        prefs.edit().putString("active_user_id", userId).apply()
        clearCart()
        loadOrdersForUser(userId)
    }

    fun login(email: String, name: String = "Valued Customer"): User {
        val existing = defaultUsers.find { it.email.equals(email, ignoreCase = true) }
        val user = existing ?: User(
            id = "user_" + UUID.randomUUID().toString().take(6),
            name = if (name.isNotBlank()) name else email.substringBefore("@").replace(".", " ").capitalizeWords(),
            email = email,
            phone = "+91 98765 " + (10000..99999).random(),
            savedAddresses = listOf(
                Address("addr_new", "Home", "Apartment 502", "Golf Course Extn", "Near Sector Club", "Gurgaon", true)
            )
        )
        _currentUser.value = user
        prefs.edit().putString("active_user_id", user.id).apply()
        loadOrdersForUser(user.id)
        return user
    }

    fun logout() {
        _currentUser.value = null
        prefs.edit().remove("active_user_id").apply()
        _cartItems.value = emptyList()
        _orders.value = emptyList()
    }

    fun setLocation(location: String) {
        _currentLocation.value = location
        prefs.edit().putString("selected_location", location).apply()
    }

    fun addAddress(label: String, flatNumber: String, street: String, landmark: String) {
        val user = _currentUser.value ?: return
        val newAddr = Address(
            id = "addr_" + UUID.randomUUID().toString().take(6),
            label = label,
            flatNumber = flatNumber,
            street = street,
            landmark = landmark,
            city = "Gurgaon",
            isDefault = user.savedAddresses.isEmpty()
        )
        val updated = user.copy(savedAddresses = user.savedAddresses + newAddr)
        _currentUser.value = updated
    }

    // Favorite Restaurants
    fun toggleFavorite(restaurantId: String) {
        _favoriteRestaurantIds.update { set ->
            if (set.contains(restaurantId)) set - restaurantId else set + restaurantId
        }
    }

    // Cart Operations
    fun addToCart(
        menuItem: com.example.data.models.MenuItem,
        restaurantId: String,
        restaurantName: String,
        selectedCustomizations: List<SelectedCustomization> = emptyList(),
        quantity: Int = 1
    ): Boolean {
        val currentItems = _cartItems.value
        if (currentItems.isNotEmpty() && currentItems.first().restaurantId != restaurantId) {
            // Signal cross-restaurant conflict
            return false
        }

        val addOnTotal = selectedCustomizations.sumOf { it.option.price }
        val unitPrice = menuItem.price + addOnTotal
        val customKey = selectedCustomizations.joinToString("-") { "${it.groupId}_${it.option.id}" }
        val cartItemId = "${menuItem.id}::$customKey"

        val existingIndex = currentItems.indexOfFirst { it.cartItemId == cartItemId }
        val updatedList = currentItems.toMutableList()

        if (existingIndex >= 0) {
            val item = updatedList[existingIndex]
            updatedList[existingIndex] = item.copy(quantity = item.quantity + quantity)
        } else {
            updatedList.add(
                CartItem(
                    cartItemId = cartItemId,
                    menuItem = menuItem,
                    restaurantId = restaurantId,
                    restaurantName = restaurantName,
                    quantity = quantity,
                    selectedCustomizations = selectedCustomizations,
                    unitPriceWithCustomizations = unitPrice
                )
            )
        }
        _cartItems.value = updatedList
        return true
    }

    fun clearAndAddToCart(
        menuItem: com.example.data.models.MenuItem,
        restaurantId: String,
        restaurantName: String,
        selectedCustomizations: List<SelectedCustomization> = emptyList(),
        quantity: Int = 1
    ) {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
        addToCart(menuItem, restaurantId, restaurantName, selectedCustomizations, quantity)
    }

    fun updateCartItemQuantity(cartItemId: String, delta: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.cartItemId == cartItemId }
        if (index >= 0) {
            val item = currentItems[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                currentItems.removeAt(index)
            } else {
                currentItems[index] = item.copy(quantity = newQty)
            }
            _cartItems.value = currentItems
            if (currentItems.isEmpty()) {
                _appliedCoupon.value = null
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    fun applyCoupon(code: String): Pair<Boolean, String> {
        val offer = MockData.PROMO_OFFERS.find { it.code.equals(code.trim(), ignoreCase = true) }
            ?: return Pair(false, "Invalid coupon code '$code'")

        val subtotal = _cartItems.value.sumOf { it.totalPrice }
        if (subtotal < offer.minOrderValue) {
            return Pair(false, "Add items worth ₹${(offer.minOrderValue - subtotal).toInt()} more to apply this coupon")
        }

        _appliedCoupon.value = offer
        return Pair(true, "Coupon '${offer.code}' applied successfully!")
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Checkout & Order Placement
    fun placeOrder(
        deliveryAddress: Address,
        paymentMethod: String,
        deliveryInstructions: String = ""
    ): Order? {
        val user = _currentUser.value ?: return null
        val items = _cartItems.value
        if (items.isEmpty()) return null

        val subtotal = items.sumOf { it.totalPrice }
        val deliveryFee = if (subtotal >= 500.0) 0.0 else 40.0
        val taxes = subtotal * 0.05 // 5% GST
        val coupon = _appliedCoupon.value

        var discount = 0.0
        if (coupon != null && subtotal >= coupon.minOrderValue) {
            discount = if (coupon.discountPercent > 0) {
                val calc = (subtotal * coupon.discountPercent) / 100.0
                calc.coerceAtMost(coupon.maxDiscount)
            } else {
                coupon.flatDiscount
            }
        }

        val grandTotal = (subtotal + deliveryFee + taxes - discount).coerceAtLeast(0.0)
        val orderId = "CRV-" + (10000..99999).random()
        val rest = MockData.RESTAURANTS.find { it.id == items.first().restaurantId } ?: MockData.RESTAURANTS.first()

        val newOrder = Order(
            id = orderId,
            orderNumber = "#$orderId",
            userId = user.id,
            userName = user.name,
            userPhone = user.phone,
            restaurantId = rest.id,
            restaurantName = rest.name,
            restaurantImage = rest.coverImageUrl,
            items = items,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            taxes = taxes,
            discount = discount,
            totalAmount = grandTotal,
            appliedCouponCode = coupon?.code,
            status = OrderStatus.PLACED,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            deliveryInstructions = deliveryInstructions,
            timestamp = System.currentTimeMillis(),
            estimatedDeliveryMinutes = rest.deliveryTimeMinutes,
            simulatedDriverName = listOf("Ramesh Kumar", "Vikram Singh", "Amit Verma", "Sunil Yadav").random(),
            simulatedDriverPhone = "+91 98" + (10000000..99999999).random()
        )

        val updatedOrders = listOf(newOrder) + _orders.value
        _orders.value = updatedOrders
        saveOrdersForUser(user.id, updatedOrders)

        // Clear cart after checkout
        clearCart()

        // Kick off simulated tracking progression
        startTrackingSimulation(newOrder.id)

        return newOrder
    }

    private fun startTrackingSimulation(orderId: String) {
        scope.launch {
            val stages = listOf(
                OrderStatus.CONFIRMED to 5000L,
                OrderStatus.PREPARING to 8000L,
                OrderStatus.READY_FOR_PICKUP to 7000L,
                OrderStatus.OUT_FOR_DELIVERY to 10000L,
                OrderStatus.DELIVERED to 12000L
            )

            for ((nextStatus, waitTime) in stages) {
                delay(waitTime)
                val currentList = _orders.value
                val existing = currentList.find { it.id == orderId } ?: break
                if (existing.status == OrderStatus.CANCELLED) break
                val updated = currentList.map {
                    if (it.id == orderId) it.copy(status = nextStatus) else it
                }
                _orders.value = updated
                _currentUser.value?.let { saveOrdersForUser(it.id, updated) }
            }
        }
    }

    // Admin Controls
    fun setOrderStatus(orderId: String, newStatus: OrderStatus) {
        val currentList = _orders.value
        val updated = currentList.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
        _orders.value = updated
        _currentUser.value?.let { saveOrdersForUser(it.id, updated) }
    }

    fun toggleRestaurantAvailability(restaurantId: String) {
        _restaurants.update { list ->
            list.map {
                if (it.id == restaurantId) it.copy(isAvailable = !it.isAvailable) else it
            }
        }
    }

    fun reorder(order: Order): Boolean {
        clearCart()
        for (item in order.items) {
            addToCart(
                menuItem = item.menuItem,
                restaurantId = order.restaurantId,
                restaurantName = order.restaurantName,
                selectedCustomizations = item.selectedCustomizations,
                quantity = item.quantity
            )
        }
        return true
    }
}

private fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
