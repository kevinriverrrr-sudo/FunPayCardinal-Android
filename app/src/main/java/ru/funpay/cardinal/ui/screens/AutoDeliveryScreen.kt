package ru.funpay.cardinal.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoDeliveryScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Автодоставка", color = CardinalTextPrimary, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Info card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalPurple.copy(alpha = 0.1f)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = CardinalPurpleLight, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Как работает автодоставка", color = CardinalPurpleLight, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Создайте конфигурацию для каждого лота. Когда покупатель оплатит заказ, Cardinal автоматически отправит ему товар из файла. Каждый товар — отдельная строка в файле .txt",
                            color = CardinalTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Empty state
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CardinalSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Inbox, contentDescription = null, tint = CardinalTextSecondary, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Нет конфигураций", color = CardinalTextPrimary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                    Text("Добавьте конфигурацию автодоставки для ваших лотов", color = CardinalTextSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = { },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = CardinalPurple.copy(alpha = 0.2f), contentColor = CardinalPurpleLight)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Добавить конфигурацию")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
