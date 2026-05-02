package ru.funpay.cardinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.funpay.cardinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Логи", color = CardinalTextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = CardinalTextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardinalSurface)
            )
        },
        containerColor = CardinalBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurface,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Консоль FunPay Cardinal", color = CardinalTextPrimary, fontWeight = FontWeight.SemiBold)
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = CardinalPurple, modifier = Modifier.size(20.dp))
                    }
                    HorizontalDivider(color = CardinalSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CardinalBackground, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Ожидание запуска FunPay Cardinal...\n\n" +
                                "После запуска здесь будут отображаться:\n" +
                                "• Статус подключения к FunPay\n" +
                                "• Автоподнятие лотов\n" +
                                "• Автодоставка товаров\n" +
                                "• Telegram уведомления\n" +
                                "• Ошибки и предупреждения",
                            color = CardinalTextSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
