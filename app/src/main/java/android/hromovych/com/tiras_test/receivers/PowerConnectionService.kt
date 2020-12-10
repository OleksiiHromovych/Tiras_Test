package android.hromovych.com.tiras_test.receivers

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class PowerConnectionService: Service() {

    val receiver = PowerConnectionReceiver()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        registerReceiver(receiver, IntentFilter(Intent.ACTION_SCREEN_ON))

        return START_STICKY
    }

}