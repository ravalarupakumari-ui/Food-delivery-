package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.CustomizationOption
import com.example.data.models.MenuItem
import com.example.data.models.Restaurant
import com.example.data.models.SelectedCustomization
import com.example.ui.theme.CraveonMuted
import com.example.ui.theme.CraveonPrimary

@Composable
fun CustomizationDialog(
    item: MenuItem,
    restaurant: Restaurant,
    onDismiss: () -> Unit,
    onConfirm: (List<SelectedCustomization>, Int) -> Unit
) {
    // Track selections: Single select per group ID -> Option, Multi-select per group ID -> Set<Option>
    val singleSelections = remember {
        mutableStateMapOf<String, CustomizationOption>().apply {
            // Pre-select first option for required single-select groups
            item.customizationGroups.forEach { group ->
                if (!group.isMultiSelect && group.options.isNotEmpty()) {
                    put(group.id, group.options.first())
                }
            }
        }
    }

    val multiSelections = remember {
        mutableStateMapOf<String, Set<CustomizationOption>>()
    }

    var quantity by remember { mutableStateOf(1) }

    // Calculate dynamic total price
    val addOnsPrice = singleSelections.values.sumOf { it.price } +
        multiSelections.values.flatten().sumOf { it.price }
    val totalItemPrice = (item.price + addOnsPrice) * quantity

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("customization_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VegBadge(isVeg = item.isVeg)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Base price: ₹${item.price.toInt()}",
                            fontSize = 13.sp,
                            color = CraveonMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Customization Groups List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(item.customizationGroups) { group ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = group.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (group.isRequired) "Required" else "Optional",
                                    fontSize = 11.sp,
                                    color = if (group.isRequired) CraveonPrimary else CraveonMuted,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            group.options.forEach { option ->
                                if (group.isMultiSelect) {
                                    val currentSet = multiSelections[group.id] ?: emptySet()
                                    val isChecked = currentSet.contains(option)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                multiSelections[group.id] = if (isChecked) {
                                                    currentSet - option
                                                } else {
                                                    currentSet + option
                                                }
                                            }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { checked ->
                                                    multiSelections[group.id] = if (checked) {
                                                        currentSet + option
                                                    } else {
                                                        currentSet - option
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(checkedColor = CraveonPrimary)
                                            )
                                            Text(
                                                text = option.name,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Text(
                                            text = if (option.price > 0) "+₹${option.price.toInt()}" else "Free",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = CraveonMuted
                                        )
                                    }
                                } else {
                                    // Single Select Radio
                                    val isSelected = singleSelections[group.id]?.id == option.id

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { singleSelections[group.id] = option }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { singleSelections[group.id] = option },
                                                colors = RadioButtonDefaults.colors(selectedColor = CraveonPrimary)
                                            )
                                            Text(
                                                text = option.name,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Text(
                                            text = if (option.price > 0) "+₹${option.price.toInt()}" else "Free",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = CraveonMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Bottom Action: Dynamic Price & CTA
                Button(
                    onClick = {
                        val selectedList = mutableListOf<SelectedCustomization>()
                        singleSelections.forEach { (groupId, option) ->
                            val g = item.customizationGroups.find { it.id == groupId }
                            if (g != null) {
                                selectedList.add(SelectedCustomization(g.id, g.title, option))
                            }
                        }
                        multiSelections.forEach { (groupId, options) ->
                            val g = item.customizationGroups.find { it.id == groupId }
                            if (g != null) {
                                options.forEach { option ->
                                    selectedList.add(SelectedCustomization(g.id, g.title, option))
                                }
                            }
                        }
                        onConfirm(selectedList, quantity)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_customization_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CraveonPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Item to Cart",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "₹${totalItemPrice.toInt()}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
