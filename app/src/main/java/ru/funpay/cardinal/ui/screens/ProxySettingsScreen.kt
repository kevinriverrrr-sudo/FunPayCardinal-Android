package ru.funpay.cardinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProxySettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val proxyEnabled by viewModel.proxyEnabled.collectAsState()
    val proxyAddress by viewModel.proxyAddress.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Прокси", color = CardinalTextPrimary, fontWeight = FontWeight.Bold) },
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

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Использовать прокси", color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                        Text("Для доступа к FunPay и Telegram через прокси-сервер", color = CardinalTextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = proxyEnabled,
                        onCheckedChange = viewModel::setProxyEnabled,
                        colors = SwitchDefaults.colors(checkedThumbColor = CardinalPurpleLight, checkedTrackColor = CardinalPurple)
                    )
                }
            }

            if (proxyEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardinalSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Адрес прокси", color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = proxyAddress,
                            onValueChange = viewModel::setProxyAddress,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("socks5://user:pass@host:port", color = CardinalTextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CardinalPurple,
                                unfocusedBorderColor = CardinalSurfaceVariant,
                                focusedTextColor = CardinalTextPrimary,
                                unfocusedTextColor = CardinalTextPrimary,
                                cursorColor = CardinalPurple
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Поддерживаемые форматы: socks5://, http://, https://",
                            color = CardinalTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
