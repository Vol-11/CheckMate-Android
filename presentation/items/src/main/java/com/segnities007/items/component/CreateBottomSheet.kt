package com.segnities007.items.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.segnities007.model.item.ItemCategory
import com.segnities007.model.item.ProductInfo
import kotlin.time.ExperimentalTime

private fun getCategoryDisplayName(category: ItemCategory): String {
    return when (category) {
        ItemCategory.STUDY_SUPPLIES -> "学業用品"
        ItemCategory.DAILY_SUPPLIES -> "生活用品"
        ItemCategory.CLOTHING_SUPPLIES -> "衣類用品"
        ItemCategory.FOOD_SUPPLIES -> "食事用品"
        ItemCategory.HEALTH_SUPPLIES -> "健康用品"
        ItemCategory.BEAUTY_SUPPLIES -> "美容用品"
        ItemCategory.EVENT_SUPPLIES -> "イベント用品"
        ItemCategory.HOBBY_SUPPLIES -> "趣味用品"
        ItemCategory.TRANSPORT_SUPPLIES -> "交通用品"
        ItemCategory.CHARGING_SUPPLIES -> "充電用品"
        ItemCategory.WEATHER_SUPPLIES -> "天候対策用品"
        ItemCategory.ID_SUPPLIES -> "証明用品"
        ItemCategory.OTHER_SUPPLIES -> "その他用品"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBottomSheet(
    onDismiss: () -> Unit,
    onCreateItem: (name: String, description: String, category: ItemCategory, imageUriForCallback: Uri?) -> Unit,
    sheetState: SheetState,
    capturedImageUriFromParent: Uri?,
    onRequestLaunchCamera: () -> Unit,
    isLoadingFromParent: Boolean = false,
    productInfo: ProductInfo? = null,
) {
    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf<String>("") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }
    var imageUriForPreview by remember(capturedImageUriFromParent) { mutableStateOf(capturedImageUriFromParent) }

    // 商品情報が提供された場合、自動的に入力
    LaunchedEffect(productInfo) {
        if (productInfo != null) {
            itemName = productInfo.name
            itemDescription = productInfo.description
            selectedCategory = productInfo.category
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                "アイテムを追加",
                style = MaterialTheme.typography.headlineSmall,
            )

            // 写真エリア
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (imageUriForPreview != null) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            model = imageUriForPreview,
                            contentDescription = "選択された画像",
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                            // 角丸
                            contentScale = ContentScale.Crop,
                        )

                        // 削除ボタンを画像右上に重ねる
                        IconButton(
                            onClick = { imageUriForPreview = null },
                            modifier =
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "画像を削除",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                } else {
                    OutlinedCard(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clickable(enabled = !isLoadingFromParent) { onRequestLaunchCamera() },
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "追加",
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("写真を追加", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // アイテム名
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("アイテム名*") },
                singleLine = true,
                enabled = !isLoadingFromParent,
                modifier = Modifier.fillMaxWidth(),
            )

            // 説明
            OutlinedTextField(
                value = itemDescription,
                onValueChange = { itemDescription = it },
                label = { Text("説明") },
                minLines = 3,
                enabled = !isLoadingFromParent,
                modifier = Modifier.fillMaxWidth(),
            )

            // カテゴリ
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { if (!isLoadingFromParent) expandedDropdown = !expandedDropdown },
            ) {
                OutlinedTextField(
                    value = selectedCategory?.let { getCategoryDisplayName(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("カテゴリ*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    enabled = !isLoadingFromParent,
                    placeholder = { Text("カテゴリを選択") },
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false },
                ) {
                    ItemCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(getCategoryDisplayName(category)) },
                            onClick = {
                                selectedCategory = category
                                expandedDropdown = false
                            },
                        )
                    }
                }
            }

            // アクションボタン
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) { Text("キャンセル") }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (itemName.isNotBlank() && selectedCategory != null) {
                            onCreateItem(itemName, itemDescription, selectedCategory!!, imageUriForPreview)
                        }
                    },
                    enabled = itemName.isNotBlank() && selectedCategory != null && !isLoadingFromParent,
                ) {
                    Text("作成")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun CreateBottomSheetPreview() {
    MaterialTheme {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        CreateBottomSheet(
            sheetState = sheetState,
            onDismiss = {},
            onCreateItem = { _, _, _, _ -> },
            capturedImageUriFromParent = null,
            onRequestLaunchCamera = {},
        )
        if (LocalInspectionMode.current) {
            LaunchedEffect(sheetState) { sheetState.show() }
        }
    }
}
