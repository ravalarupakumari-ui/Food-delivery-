package com.example.data.models

enum class OrderStatus(val displayLabel: String, val subtitle: String) {
    PLACED("Order Placed", "Your order has been sent to the restaurant"),
    CONFIRMED("Order Confirmed", "Restaurant has accepted your order"),
    PREPARING("Preparing Food", "Chef is carefully preparing your meal"),
    READY_FOR_PICKUP("Ready for Pickup", "Packed and waiting for delivery partner"),
    OUT_FOR_DELIVERY("Out for Delivery", "Delivery partner is on the way to you"),
    DELIVERED("Delivered", "Enjoy your delicious meal!"),
    CANCELLED("Cancelled", "Order was cancelled")
}

data class CategoryShortcut(
    val id: String,
    val name: String,
    val imageUrl: String
)

data class Offer(
    val id: String,
    val code: String,
    val title: String,
    val description: String,
    val discountPercent: Int = 0,
    val flatDiscount: Double = 0.0,
    val minOrderValue: Double = 199.0,
    val maxDiscount: Double = 150.0
)

data class CustomizationOption(
    val id: String,
    val name: String,
    val price: Double = 0.0,
    val isVeg: Boolean = true
)

data class CustomizationGroup(
    val id: String,
    val title: String,
    val isRequired: Boolean = false,
    val isMultiSelect: Boolean = false,
    val options: List<CustomizationOption>
)

data class SelectedCustomization(
    val groupId: String,
    val groupTitle: String,
    val option: CustomizationOption
)

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val isVeg: Boolean,
    val isBestseller: Boolean = false,
    val rating: Double = 4.3,
    val ratingCount: Int = 120,
    val customizationGroups: List<CustomizationGroup> = emptyList(),
    val isAvailable: Boolean = true
)

data class MenuCategory(
    val id: String,
    val name: String,
    val items: List<MenuItem>
)

data class Restaurant(
    val id: String,
    val name: String,
    val coverImageUrl: String,
    val cuisines: List<String>,
    val rating: Double,
    val ratingCountText: String,
    val deliveryTimeMinutes: Int,
    val distanceKm: Double,
    val priceForTwo: Double,
    val offers: List<Offer>,
    val isVegOnly: Boolean = false,
    val isPromoted: Boolean = false,
    val isPremium: Boolean = false,
    val isAvailable: Boolean = true,
    val locationArea: String = "Connaught Place, New Delhi",
    val menuCategories: List<MenuCategory>
)

data class CartItem(
    val cartItemId: String,
    val menuItem: MenuItem,
    val restaurantId: String,
    val restaurantName: String,
    val quantity: Int = 1,
    val selectedCustomizations: List<SelectedCustomization> = emptyList(),
    val unitPriceWithCustomizations: Double
) {
    val totalPrice: Double
        get() = unitPriceWithCustomizations * quantity
}

data class Address(
    val id: String,
    val label: String, // Home, Work, Other
    val flatNumber: String,
    val street: String,
    val landmark: String,
    val city: String = "Gurgaon",
    val isDefault: Boolean = false
) {
    val formattedAddress: String
        get() = "$flatNumber, $street${if (landmark.isNotBlank()) ", near $landmark" else ""}, $city"
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val savedAddresses: List<Address> = emptyList()
)

data class Order(
    val id: String,
    val orderNumber: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val restaurantId: String,
    val restaurantName: String,
    val restaurantImage: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val taxes: Double,
    val discount: Double,
    val totalAmount: Double,
    val appliedCouponCode: String? = null,
    val status: OrderStatus,
    val deliveryAddress: Address,
    val paymentMethod: String, // "UPI / Google Pay", "Credit Card", "Cash on Delivery"
    val deliveryInstructions: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val estimatedDeliveryMinutes: Int = 30,
    val simulatedDriverName: String = "Ramesh Kumar",
    val simulatedDriverPhone: String = "+91 98765 43210"
)
