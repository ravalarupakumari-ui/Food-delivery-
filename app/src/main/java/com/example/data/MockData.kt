package com.example.data

import com.example.data.models.CategoryShortcut
import com.example.data.models.CustomizationGroup
import com.example.data.models.CustomizationOption
import com.example.data.models.MenuCategory
import com.example.data.models.MenuItem
import com.example.data.models.Offer
import com.example.data.models.Restaurant

object MockData {

    val PROMO_OFFERS = listOf(
        Offer(
            id = "off_1",
            code = "CRAVE50",
            title = "50% OFF up to ₹100",
            description = "Use code CRAVE50 | Above ₹199",
            discountPercent = 50,
            minOrderValue = 199.0,
            maxDiscount = 100.0
        ),
        Offer(
            id = "off_2",
            code = "WELCOME200",
            title = "Flat ₹200 OFF",
            description = "Welcome special on orders above ₹599",
            flatDiscount = 200.0,
            minOrderValue = 599.0,
            maxDiscount = 200.0
        ),
        Offer(
            id = "off_3",
            code = "FREE_DEL",
            title = "FREE Delivery",
            description = "Zero delivery charges on your order",
            flatDiscount = 40.0,
            minOrderValue = 149.0,
            maxDiscount = 40.0
        ),
        Offer(
            id = "off_4",
            code = "JUMBO30",
            title = "30% OFF up to ₹300",
            description = "Valid on big family orders above ₹899",
            discountPercent = 30,
            minOrderValue = 899.0,
            maxDiscount = 300.0
        )
    )

    val CATEGORY_SHORTCUTS = listOf(
        CategoryShortcut("c1", "Biryani", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=300&q=80"),
        CategoryShortcut("c2", "Pizza", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300&q=80"),
        CategoryShortcut("c3", "Burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300&q=80"),
        CategoryShortcut("c4", "Dosa", "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=300&q=80"),
        CategoryShortcut("c5", "Idli", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=300&q=80"),
        CategoryShortcut("c6", "Rolls", "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=300&q=80"),
        CategoryShortcut("c7", "Thali", "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=300&q=80"),
        CategoryShortcut("c8", "Momos", "https://images.unsplash.com/photo-1625220194771-7ebdec0dbfde?w=300&q=80"),
        CategoryShortcut("c9", "Desserts", "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=300&q=80"),
        CategoryShortcut("c10", "North Indian", "https://images.unsplash.com/photo-1517244683847-7456b63c5969?w=300&q=80"),
        CategoryShortcut("c11", "South Indian", "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=300&q=80"),
        CategoryShortcut("c12", "Chinese", "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=300&q=80")
    )

    // Reusable Customization Groups
    val PIZZA_SIZE_CUSTOMIZATIONS = CustomizationGroup(
        id = "size_group",
        title = "Choice of Size",
        isRequired = true,
        isMultiSelect = false,
        options = listOf(
            CustomizationOption("s_reg", "Regular (7 inch)", 0.0),
            CustomizationOption("s_med", "Medium (10 inch)", 120.0),
            CustomizationOption("s_lrg", "Large (12 inch)", 240.0)
        )
    )

    val PIZZA_CRUST_CUSTOMIZATIONS = CustomizationGroup(
        id = "crust_group",
        title = "Choice of Crust",
        isRequired = false,
        isMultiSelect = false,
        options = listOf(
            CustomizationOption("cr_classic", "Classic Hand Tossed", 0.0),
            CustomizationOption("cr_thin", "Crispy Thin Crust", 30.0),
            CustomizationOption("cr_cheese", "Cheese Burst", 75.0)
        )
    )

    val PIZZA_TOPPINGS = CustomizationGroup(
        id = "toppings_group",
        title = "Extra Toppings (Optional)",
        isRequired = false,
        isMultiSelect = true,
        options = listOf(
            CustomizationOption("top_cheese", "Extra Mozzarella Cheese", 45.0),
            CustomizationOption("top_paneer", "Peri-Peri Paneer", 50.0),
            CustomizationOption("top_jalapeno", "Pickled Jalapeños", 25.0),
            CustomizationOption("top_olives", "Black Olives", 30.0),
            CustomizationOption("top_mushrooms", "Fresh Grilled Mushrooms", 35.0)
        )
    )

    val SPICE_LEVEL_CUSTOMIZATION = CustomizationGroup(
        id = "spice_group",
        title = "Select Spice Level",
        isRequired = false,
        isMultiSelect = false,
        options = listOf(
            CustomizationOption("sp_mild", "Mild & Flavorful", 0.0),
            CustomizationOption("sp_medium", "Authentic Spicy", 0.0),
            CustomizationOption("sp_hot", "Extra Hot & Spicy", 0.0)
        )
    )

    val BIRYANI_PORTION_CUSTOMIZATION = CustomizationGroup(
        id = "portion_group",
        title = "Select Portion",
        isRequired = true,
        isMultiSelect = false,
        options = listOf(
            CustomizationOption("p_regular", "Regular (Serves 1-2)", 0.0),
            CustomizationOption("p_jumbo", "Family Pack (Serves 3-4) + Raita & Salan", 280.0)
        )
    )

    val BIRYANI_ADDONS = CustomizationGroup(
        id = "biryani_addons",
        title = "Add-ons & Accompaniments",
        isRequired = false,
        isMultiSelect = true,
        options = listOf(
            CustomizationOption("ba_raita", "Boondi Raita (200ml)", 35.0),
            CustomizationOption("ba_salan", "Mirchi Ka Salan (200ml)", 45.0),
            CustomizationOption("ba_egg", "Extra Boiled Egg (2 pcs)", 30.0, isVeg = false),
            CustomizationOption("ba_gulab", "Gulab Jamun (2 pcs)", 50.0)
        )
    )

    // Build the 20 Authentic Restaurants
    val RESTAURANTS: List<Restaurant> = listOf(
        Restaurant(
            id = "rest_1",
            name = "The Great Biryani Company",
            coverImageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600&q=80",
            cuisines = listOf("Biryani", "Hyderabadi", "Mughlai", "North Indian"),
            rating = 4.6,
            ratingCountText = "12K+",
            deliveryTimeMinutes = 28,
            distanceKm = 2.4,
            priceForTwo = 450.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[2]),
            isVegOnly = false,
            isPromoted = true,
            isPremium = true,
            locationArea = "Sector 29, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_b1",
                    name = "Chef's Signature Biryanis",
                    items = listOf(
                        MenuItem(
                            id = "item_b1",
                            name = "Dum Chicken Hyderabadi Biryani",
                            description = "Fragrant long-grain basmati rice layered with slow-cooked marinated chicken, saffron, and caramelised onions.",
                            price = 340.0,
                            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8,
                            customizationGroups = listOf(BIRYANI_PORTION_CUSTOMIZATION, SPICE_LEVEL_CUSTOMIZATION, BIRYANI_ADDONS)
                        ),
                        MenuItem(
                            id = "item_b2",
                            name = "Awadhi Mutton Dum Biryani",
                            description = "Tender goat meat infused with royal Awadhi spices, kewra essence, and aromatic clarified butter.",
                            price = 450.0,
                            imageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.9,
                            customizationGroups = listOf(BIRYANI_PORTION_CUSTOMIZATION, BIRYANI_ADDONS)
                        ),
                        MenuItem(
                            id = "item_b3",
                            name = "Paneer Tikka Dum Biryani",
                            description = "Smoky tandoori paneer cubes gently cooked with rich basmati rice, mint, and saffron.",
                            price = 290.0,
                            imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400&q=80",
                            isVeg = true,
                            isBestseller = false,
                            rating = 4.5,
                            customizationGroups = listOf(BIRYANI_PORTION_CUSTOMIZATION, BIRYANI_ADDONS)
                        )
                    )
                ),
                MenuCategory(
                    id = "mc_b2",
                    name = "Kebabs & Starters",
                    items = listOf(
                        MenuItem(
                            id = "item_b4",
                            name = "Galouti Kebab Melt (4 pcs)",
                            description = "Mouth-melting Lucknowi style spiced mutton patties served with mint chutney.",
                            price = 360.0,
                            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=400&q=80",
                            isVeg = false,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_b5",
                            name = "Tandoori Malai Chaap",
                            description = "Charcoal grilled soya chaap marinated in creamy cashew, cheese, and cardamom paste.",
                            price = 240.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            rating = 4.4
                        )
                    )
                ),
                MenuCategory(
                    id = "mc_b3",
                    name = "Desserts & Beverages",
                    items = listOf(
                        MenuItem(
                            id = "item_b6",
                            name = "Shahi Tukda with Rabri",
                            description = "Crispy golden fried bread soaked in saffron syrup and topped with thick malai rabri.",
                            price = 140.0,
                            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        ),
                        MenuItem(
                            id = "item_b7",
                            name = "Thandai Kulfi on Stick",
                            description = "Traditional slow churned milk kulfi with crushed almonds, pistachios, and saffron.",
                            price = 90.0,
                            imageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_2",
            name = "Crust & Craft Artisanal Pizza",
            coverImageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80",
            cuisines = listOf("Pizzas", "Italian", "Pastas", "Desserts"),
            rating = 4.5,
            ratingCountText = "8.4K+",
            deliveryTimeMinutes = 24,
            distanceKm = 1.9,
            priceForTwo = 550.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[1]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = true,
            locationArea = "Cyber Hub, DLF Phase 2",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_p1",
                    name = "Wood-Fired Pizzas",
                    items = listOf(
                        MenuItem(
                            id = "item_p1",
                            name = "Farmhouse Gourmet Pizza",
                            description = "San Marzano tomato base with fresh mozzarella, button mushrooms, crunchy bell peppers, red onions, and sweet corn.",
                            price = 380.0,
                            imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.7,
                            customizationGroups = listOf(PIZZA_SIZE_CUSTOMIZATIONS, PIZZA_CRUST_CUSTOMIZATIONS, PIZZA_TOPPINGS)
                        ),
                        MenuItem(
                            id = "item_p2",
                            name = "Pepperoni & Smoked Chicken",
                            description = "Loaded with artisan chicken pepperoni, smoked pulled chicken breast, and melted aged provolone.",
                            price = 470.0,
                            imageUrl = "https://images.unsplash.com/photo-1534308983496-4fabb1a015ee?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.9,
                            customizationGroups = listOf(PIZZA_SIZE_CUSTOMIZATIONS, PIZZA_CRUST_CUSTOMIZATIONS, PIZZA_TOPPINGS)
                        ),
                        MenuItem(
                            id = "item_p3",
                            name = "Classic Margherita di Bufala",
                            description = "Fresh Buffalo mozzarella, San Marzano marinara, basil leaves, extra virgin olive oil drizzle.",
                            price = 320.0,
                            imageUrl = "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400&q=80",
                            isVeg = true,
                            rating = 4.6,
                            customizationGroups = listOf(PIZZA_SIZE_CUSTOMIZATIONS, PIZZA_CRUST_CUSTOMIZATIONS, PIZZA_TOPPINGS)
                        )
                    )
                ),
                MenuCategory(
                    id = "mc_p2",
                    name = "Pastas & Garlic Breads",
                    items = listOf(
                        MenuItem(
                            id = "item_p4",
                            name = "Creamy Truffle Mushroom Penne",
                            description = "Penne tossed in silky parmesan cream sauce with wild sautéed mushrooms and white truffle oil.",
                            price = 340.0,
                            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        ),
                        MenuItem(
                            id = "item_p5",
                            name = "Cheesy Stuffed Garlic Bread",
                            description = "Freshly baked baguette loaded with garlic herbed butter, mozzarella, and green jalapeños.",
                            price = 180.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.8
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_3",
            name = "Sagar Ratna Heritage",
            coverImageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=600&q=80",
            cuisines = listOf("South Indian", "Udupi", "Filter Coffee"),
            rating = 4.7,
            ratingCountText = "22K+",
            deliveryTimeMinutes = 20,
            distanceKm = 1.2,
            priceForTwo = 300.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[2]),
            isVegOnly = true,
            isPromoted = true,
            isPremium = false,
            locationArea = "Sector 14, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_s1",
                    name = "Crispy Dosas & Vadas",
                    items = listOf(
                        MenuItem(
                            id = "item_s1",
                            name = "Mysore Butter Masala Dosa",
                            description = "Crisp golden dosa lined with spicy garlic-red chilli chutney and spiced potato bhaji, served with coconut chutney & sambar.",
                            price = 190.0,
                            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_s2",
                            name = "Steamed Ghee Podi Idli (4 pcs)",
                            description = "Soft mini idlis tossed with fragrant gun powder podi and hot melted desi cow ghee.",
                            price = 150.0,
                            imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_s3",
                            name = "Medu Vada with Sambar (2 pcs)",
                            description = "Crispy lentil fritters deep fried till golden crunch, soaked in piping hot lentil vegetable sambar.",
                            price = 120.0,
                            imageUrl = "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        )
                    )
                ),
                MenuCategory(
                    id = "mc_s2",
                    name = "Traditional Drinks & Sweets",
                    items = listOf(
                        MenuItem(
                            id = "item_s4",
                            name = "Authentic Madras Filter Coffee (Flask)",
                            description = "Frothy dark roasted chicory coffee brewed fresh in brass drip, serves 2.",
                            price = 90.0,
                            imageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400&q=80",
                            isVeg = true,
                            rating = 4.9
                        ),
                        MenuItem(
                            id = "item_s5",
                            name = "Badam Kesari Halwa",
                            description = "Semolina pudding cooked with real saffron strands, pure ghee, and toasted cashews.",
                            price = 110.0,
                            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_4",
            name = "Punjab Grill & Curry House",
            coverImageUrl = "https://images.unsplash.com/photo-1517244683847-7456b63c5969?w=600&q=80",
            cuisines = listOf("North Indian", "Mughlai", "Thalis", "Tandoor"),
            rating = 4.4,
            ratingCountText = "15K+",
            deliveryTimeMinutes = 32,
            distanceKm = 3.1,
            priceForTwo = 600.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[3]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = true,
            locationArea = "Galleria Market, DLF Phase 4",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_pg1",
                    name = "Main Curries",
                    items = listOf(
                        MenuItem(
                            id = "item_pg1",
                            name = "Butter Chicken Delhi Style",
                            description = "Tandoori chicken simmered in a velvety tomato and cream gravy with fenugreek and butter.",
                            price = 380.0,
                            imageUrl = "https://images.unsplash.com/photo-1517244683847-7456b63c5969?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_pg2",
                            name = "Dal Makhani 24-Hr Slow Simmered",
                            description = "Black urad lentils cooked overnight with tomatoes, garlic, white butter, and fresh cream.",
                            price = 280.0,
                            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.9
                        ),
                        MenuItem(
                            id = "item_pg3",
                            name = "Paneer Tikka Lababdar",
                            description = "Charred cottage cheese in a tangy onion-tomato gravy with grated paneer and coriander.",
                            price = 320.0,
                            imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        )
                    )
                ),
                MenuCategory(
                    id = "mc_pg2",
                    name = "Tandoori Breads",
                    items = listOf(
                        MenuItem(
                            id = "item_pg4",
                            name = "Garlic Butter Naan",
                            description = "Fluffy refined flour bread roasted in clay oven, brushed with melted butter and crushed garlic.",
                            price = 65.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_pg5",
                            name = "Amritsari Chur Chur Kulcha with Chole",
                            description = "Flaky stuffed potato onion kulcha crushed with butter, served with pindi chole.",
                            price = 190.0,
                            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_5",
            name = "Smash Brothers Gourmet Burgers",
            coverImageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
            cuisines = listOf("Burgers", "American", "Fries", "Shakes"),
            rating = 4.5,
            ratingCountText = "6.1K+",
            deliveryTimeMinutes = 22,
            distanceKm = 1.6,
            priceForTwo = 400.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[2]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 56, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_bgr1",
                    name = "Smash Burgers",
                    items = listOf(
                        MenuItem(
                            id = "item_bgr1",
                            name = "Double Crispy Tender Chicken Burger",
                            description = "Two seasoned crispy chicken fillets, melted cheddar, pickled gherkins, and house smoked secret sauce on a toasted brioche bun.",
                            price = 280.0,
                            imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_bgr2",
                            name = "Truffle Mushroom Swiss Veg Burger",
                            description = "Handmade crispy vegetable patty topped with sautéed portobello mushrooms and Swiss emmental cheese.",
                            price = 240.0,
                            imageUrl = "https://images.unsplash.com/photo-1550547660-d9450f859349?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.6
                        ),
                        MenuItem(
                            id = "item_bgr3",
                            name = "Fiery Peri Peri Loaded Fries",
                            description = "Crispy skin-on fries tossed in peri-peri spice dust and drenched in hot liquid cheese.",
                            price = 160.0,
                            imageUrl = "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_6",
            name = "Himalayan Momo & Noodle Bar",
            coverImageUrl = "https://images.unsplash.com/photo-1625220194771-7ebdec0dbfde?w=600&q=80",
            cuisines = listOf("Momos", "Tibetan", "Chinese", "Asian"),
            rating = 4.6,
            ratingCountText = "9.5K+",
            deliveryTimeMinutes = 20,
            distanceKm = 1.5,
            priceForTwo = 250.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = false,
            isPromoted = true,
            isPremium = false,
            locationArea = "Sushant Lok 1, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_m1",
                    name = "Steamed & Pan Fried Momos",
                    items = listOf(
                        MenuItem(
                            id = "item_m1",
                            name = "Darjeeling Steamed Chicken Momos (8 pcs)",
                            description = "Thin translucent wrappers stuffed with minced juicy chicken, spring onions, and coriander, served with fiery red sesame dip.",
                            price = 190.0,
                            imageUrl = "https://images.unsplash.com/photo-1625220194771-7ebdec0dbfde?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_m2",
                            name = "Cheese & Corn Crispy Kurkure Momos (6 pcs)",
                            description = "Crunchy crumb coated momos stuffed with gooey molten mozzarella and sweet corn.",
                            price = 180.0,
                            imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_m3",
                            name = "Chilli Garlic Hakka Noodles",
                            description = "Wok tossed noodles with crunchy julienned veggies, crushed garlic, and dark soy.",
                            price = 210.0,
                            imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=400&q=80",
                            isVeg = true,
                            rating = 4.4
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_7",
            name = "Kolkata Kathi Rolls & Baati",
            coverImageUrl = "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=600&q=80",
            cuisines = listOf("Rolls", "Fast Food", "Street Food", "Bengali"),
            rating = 4.3,
            ratingCountText = "5.3K+",
            deliveryTimeMinutes = 18,
            distanceKm = 1.1,
            priceForTwo = 200.0,
            offers = listOf(PROMO_OFFERS[2]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Vyapar Kendra, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_r1",
                    name = "Signature Paratha Rolls",
                    items = listOf(
                        MenuItem(
                            id = "item_r1",
                            name = "Double Egg Double Chicken Kathi Roll",
                            description = "Crispy layered paratha lined with two eggs, loaded with spicy roasted chicken boti, sliced onions, and green lime chutney.",
                            price = 180.0,
                            imageUrl = "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_r2",
                            name = "Paneer Tikka Malai Roll",
                            description = "Soft rumali roll packed with tandoori paneer, capsicum, and creamy mint mayonnaise.",
                            price = 150.0,
                            imageUrl = "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=400&q=80",
                            isVeg = true,
                            rating = 4.4
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_8",
            name = "Rajdhani Royal Thali",
            coverImageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=600&q=80",
            cuisines = listOf("Thali", "Rajasthani", "Gujarati", "North Indian"),
            rating = 4.8,
            ratingCountText = "14K+",
            deliveryTimeMinutes = 35,
            distanceKm = 3.6,
            priceForTwo = 650.0,
            offers = listOf(PROMO_OFFERS[1], PROMO_OFFERS[3]),
            isVegOnly = true,
            isPromoted = true,
            isPremium = true,
            locationArea = "Golf Course Road, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_th1",
                    name = "Royal Thali Boxes",
                    items = listOf(
                        MenuItem(
                            id = "item_th1",
                            name = "Maharaja Rajasthani Thali Box",
                            description = "Dal Baati Churma, Gatte ki Sabzi, Paneer Butter Masala, Ker Sangri, 4 Ghee Phulkas, Pulao, Gulab Jamun, and Chaas.",
                            price = 420.0,
                            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.9
                        ),
                        MenuItem(
                            id = "item_th2",
                            name = "Mini Deluxe Executive Thali",
                            description = "Paneer gravy, Dal Makhani, Seasonal Veggie, 3 Butter Rotis, Jeera Rice, and Sweet.",
                            price = 260.0,
                            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_9",
            name = "Sweet Truth & Dessert Haven",
            coverImageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=600&q=80",
            cuisines = listOf("Desserts", "Bakery", "Ice Cream", "Waffles"),
            rating = 4.6,
            ratingCountText = "4.2K+",
            deliveryTimeMinutes = 19,
            distanceKm = 1.4,
            priceForTwo = 280.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = true,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 45, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_ds1",
                    name = "Cakes, Brownies & Jars",
                    items = listOf(
                        MenuItem(
                            id = "item_ds1",
                            name = "Warm Molten Lava Choco Cake",
                            description = "Decadent chocolate cake with a gushing gooey molten chocolate core.",
                            price = 130.0,
                            imageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_ds2",
                            name = "Nutella Belgian Waffle",
                            description = "Crispy golden waffle smothered in rich Nutella and roasted almond flakes.",
                            price = 190.0,
                            imageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400&q=80",
                            isVeg = true,
                            rating = 4.7
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_10",
            name = "Dragon Wok Pan-Asian Kitchen",
            coverImageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=600&q=80",
            cuisines = listOf("Chinese", "Thai", "Asian", "Dim Sum"),
            rating = 4.4,
            ratingCountText = "7.8K+",
            deliveryTimeMinutes = 26,
            distanceKm = 2.1,
            priceForTwo = 480.0,
            offers = listOf(PROMO_OFFERS[1]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 31, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_dw1",
                    name = "Wok Bowls & Starters",
                    items = listOf(
                        MenuItem(
                            id = "item_dw1",
                            name = "Crispy Chilli Chicken Dry",
                            description = "Tender fried chicken tossed with green bell peppers, spring onions, and spicy schezwan sauce.",
                            price = 290.0,
                            imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.7
                        ),
                        MenuItem(
                            id = "item_dw2",
                            name = "Veg Manchurian with Fried Rice Bowl",
                            description = "Vegetable dumplings in savory dark soy gravy paired with aromatic garlic fried rice.",
                            price = 240.0,
                            imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=400&q=80",
                            isVeg = true,
                            rating = 4.5
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_11",
            name = "Haldiram's Sweets & Quick Bites",
            coverImageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=600&q=80",
            cuisines = listOf("North Indian", "Chaat", "Sweets", "Thali"),
            rating = 4.5,
            ratingCountText = "35K+",
            deliveryTimeMinutes = 25,
            distanceKm = 2.0,
            priceForTwo = 350.0,
            offers = listOf(PROMO_OFFERS[0], PROMO_OFFERS[2]),
            isVegOnly = true,
            isPromoted = true,
            isPremium = false,
            locationArea = "Sohna Road, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_h1",
                    name = "Famous Chaat & Mains",
                    items = listOf(
                        MenuItem(
                            id = "item_h1",
                            name = "Special Dahi Bhalla Chaat",
                            description = "Soft lentil dumplings soaked in sweet churned yogurt, topped with roasted cumin and tamarind glaze.",
                            price = 140.0,
                            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.8
                        ),
                        MenuItem(
                            id = "item_h2",
                            name = "Chole Bhature Platter (2 pcs)",
                            description = "Fluffy deep fried leavened bread served with spicy chickpea curry, pickled onions, and mint.",
                            price = 180.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.9
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_12",
            name = "Bikanervala Traditional Rasoi",
            coverImageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600&q=80",
            cuisines = listOf("North Indian", "Street Food", "Sweets"),
            rating = 4.3,
            ratingCountText = "18K+",
            deliveryTimeMinutes = 27,
            distanceKm = 2.5,
            priceForTwo = 300.0,
            offers = listOf(PROMO_OFFERS[2]),
            isVegOnly = true,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 14, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_bv1",
                    name = "Bites & Snacks",
                    items = listOf(
                        MenuItem(
                            id = "item_bv1",
                            name = "Raj Kachori Supreme",
                            description = "Giant crispy kachori filled with potatoes, sprouted moong, sweetened curd, and spicy chutneys.",
                            price = 160.0,
                            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b547b4e5c?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.7
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_13",
            name = "Barbeque Nation Express",
            coverImageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600&q=80",
            cuisines = listOf("Barbeque", "North Indian", "Kebabs", "Biryani"),
            rating = 4.6,
            ratingCountText = "19K+",
            deliveryTimeMinutes = 35,
            distanceKm = 3.8,
            priceForTwo = 850.0,
            offers = listOf(PROMO_OFFERS[1], PROMO_OFFERS[3]),
            isVegOnly = false,
            isPromoted = true,
            isPremium = true,
            locationArea = "MG Road, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_bbq1",
                    name = "Barbeque Feast Boxes",
                    items = listOf(
                        MenuItem(
                            id = "item_bbq1",
                            name = "Non-Veg Barbeque Overload Box",
                            description = "Tandoori tangdi, chicken tikka, mutton seekh kebab, butter chicken gravy, 2 parathas, and brownie.",
                            price = 590.0,
                            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_14",
            name = "Moti Mahal Delux (Since 1920)",
            coverImageUrl = "https://images.unsplash.com/photo-1517244683847-7456b63c5969?w=600&q=80",
            cuisines = listOf("Mughlai", "North Indian", "Tandoor"),
            rating = 4.5,
            ratingCountText = "20K+",
            deliveryTimeMinutes = 30,
            distanceKm = 2.9,
            priceForTwo = 700.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = true,
            locationArea = "Sector 29, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_mm1",
                    name = "Original Tandoori Curries",
                    items = listOf(
                        MenuItem(
                            id = "item_mm1",
                            name = "Original 1920 Tandoori Chicken (Half)",
                            description = "The birthplace recipe of tandoori chicken, roasted over smoky charcoal with ground spices.",
                            price = 320.0,
                            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.9
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_15",
            name = "Green Bowl & Salad Bar",
            coverImageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
            cuisines = listOf("Healthy Food", "Salads", "Smoothie Bowls"),
            rating = 4.3,
            ratingCountText = "3.8K+",
            deliveryTimeMinutes = 22,
            distanceKm = 1.8,
            priceForTwo = 380.0,
            offers = listOf(PROMO_OFFERS[2]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Cyber City, DLF Phase 3",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_gb1",
                    name = "Nutri Bowls",
                    items = listOf(
                        MenuItem(
                            id = "item_gb1",
                            name = "Roasted Paneer & Quinoa Power Bowl",
                            description = "Warm organic quinoa, grilled cottage cheese, cherry tomatoes, avocado, and tahini lemon dressing.",
                            price = 280.0,
                            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_16",
            name = "Madras Tiffin Room",
            coverImageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&q=80",
            cuisines = listOf("South Indian", "Chettinad", "Udupi"),
            rating = 4.6,
            ratingCountText = "11K+",
            deliveryTimeMinutes = 21,
            distanceKm = 1.7,
            priceForTwo = 240.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = true,
            isPromoted = false,
            isPremium = false,
            locationArea = "South City 1, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_mtr1",
                    name = "Tiffin Specials",
                    items = listOf(
                        MenuItem(
                            id = "item_mtr1",
                            name = "Onion Rava Masala Dosa",
                            description = "Crispy semolina lattice crepe studded with crushed black pepper, ginger, and curry leaves.",
                            price = 170.0,
                            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.8
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_17",
            name = "Keventers Milkshake & Waffles",
            coverImageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=600&q=80",
            cuisines = listOf("Beverages", "Desserts", "Milkshakes"),
            rating = 4.4,
            ratingCountText = "8.2K+",
            deliveryTimeMinutes = 16,
            distanceKm = 1.0,
            priceForTwo = 260.0,
            offers = listOf(PROMO_OFFERS[2]),
            isVegOnly = true,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 43, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_kv1",
                    name = "Thick Shakes",
                    items = listOf(
                        MenuItem(
                            id = "item_kv1",
                            name = "Belgian Chocolate Thick Shake (Glass Bottle)",
                            description = "Ultra creamy shake made with rich dark chocolate fudge and chocolate ice cream.",
                            price = 199.0,
                            imageUrl = "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.7
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_18",
            name = "Tandoori Nights & Kabab Bar",
            coverImageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=600&q=80",
            cuisines = listOf("Mughlai", "North Indian", "Rolls"),
            rating = 4.3,
            ratingCountText = "6.9K+",
            deliveryTimeMinutes = 29,
            distanceKm = 2.7,
            priceForTwo = 500.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 50, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_tn1",
                    name = "Barbeque Starters",
                    items = listOf(
                        MenuItem(
                            id = "item_tn1",
                            name = "Afghani Chicken Tikka",
                            description = "Creamy marinated boneless chicken skewers grilled in tandoor with aromatic ground herbs.",
                            price = 320.0,
                            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=400&q=80",
                            isVeg = false,
                            rating = 4.6
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_19",
            name = "Beirut Shawarma & Falafel",
            coverImageUrl = "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=600&q=80",
            cuisines = listOf("Middle Eastern", "Fast Food", "Rolls"),
            rating = 4.4,
            ratingCountText = "5.1K+",
            deliveryTimeMinutes = 20,
            distanceKm = 1.3,
            priceForTwo = 280.0,
            offers = listOf(PROMO_OFFERS[2]),
            isVegOnly = false,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 28, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_bs1",
                    name = "Lebanese Rolls",
                    items = listOf(
                        MenuItem(
                            id = "item_bs1",
                            name = "Classic Roasted Chicken Shawarma",
                            description = "Spit-roasted shredded chicken stuffed in warm pita with garlic toum sauce, french fries, and pickled cucumber.",
                            price = 170.0,
                            imageUrl = "https://images.unsplash.com/photo-1626776875911-9992d4fa4444?w=400&q=80",
                            isVeg = false,
                            isBestseller = true,
                            rating = 4.8
                        )
                    )
                )
            )
        ),
        Restaurant(
            id = "rest_20",
            name = "Chai Point & All-Day Breakfast",
            coverImageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=600&q=80",
            cuisines = listOf("Beverages", "Snacks", "Street Food", "Breakfast"),
            rating = 4.5,
            ratingCountText = "16K+",
            deliveryTimeMinutes = 15,
            distanceKm = 0.9,
            priceForTwo = 180.0,
            offers = listOf(PROMO_OFFERS[0]),
            isVegOnly = true,
            isPromoted = false,
            isPremium = false,
            locationArea = "Sector 45, Gurgaon",
            menuCategories = listOf(
                MenuCategory(
                    id = "mc_cp1",
                    name = "Fresh Chai & Snacks",
                    items = listOf(
                        MenuItem(
                            id = "item_cp1",
                            name = "Ginger Cardamom Masala Chai Flask (500ml)",
                            description = "Freshly brewed hot milk tea infused with real crushed ginger and green cardamom, serves 4-5 cups.",
                            price = 120.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            isBestseller = true,
                            rating = 4.9
                        ),
                        MenuItem(
                            id = "item_cp2",
                            name = "Crispy Samosa with Mint Chutney (2 pcs)",
                            description = "Flaky golden fried pyramid pastries stuffed with spiced potatoes and peas.",
                            price = 50.0,
                            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0566456955?w=400&q=80",
                            isVeg = true,
                            rating = 4.6
                        )
                    )
                )
            )
        )
    )
}
