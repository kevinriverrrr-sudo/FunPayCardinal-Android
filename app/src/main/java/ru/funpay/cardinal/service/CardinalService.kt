package ru.funpay.cardinal.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ru.funpay.cardinal.R
import ru.funpay.cardinal.python.CardinalBridge
import kotlinx.coroutines.*

class CardinalService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var cardinalBridge: CardinalBridge? = null

    companion object {
        const val CHANNEL_ID = "cardinal_service_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ru.funpay.cardinal.START"
        const val ACTION_STOP = "ru.funpay.cardinal.STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        cardinalBridge = CardinalBridge(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startCardinal()
            ACTION_STOP -> stopCardinal()
        }
        return START_STICKY
    }

    private fun startCardinal() {
        val notification = createNotification("FunPay Cardinal запускается...")
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopCardinal() {
        serviceScope.launch {
            cardinalBridge?.stopCardinal()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FunPay Cardinal")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "FunPay Cardinal Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Фоновая служба FunPay Cardinal"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        cardinalBridge?.let { bridge ->
            runBlocking { bridge.stopCardinal() }
        }
    }
}
