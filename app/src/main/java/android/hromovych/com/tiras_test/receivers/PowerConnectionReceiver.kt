package android.hromovych.com.tiras_test.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hromovych.com.tiras_test.MainActivity

class PowerConnectionReceiver : BroadcastReceiver() {
    /**If an app registers to receive broadcasts, the app's receiver consumes resources every time
     * the broadcast is sent. This can cause problems if too many apps register to receive
     * broadcasts based on system events; a system event that triggers a broadcast can cause all of
     * those apps to consume resources in rapid succession, impairing the user experience. To
     * mitigate this problem, Android 7.0 (API level 24) placed limitations on broadcasts, as
     * described in Background Optimization. Android 8.0 (API level 26) makes these limitations
     * more stringent.
     * */
    override fun onReceive(context: Context?, intent: Intent?) {
//        Toast.makeText(context, "Receive", Toast.LENGTH_SHORT).show()
//        val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
//        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
//                || status == BatteryManager.BATTERY_STATUS_FULL
//// How are we charging?
//        val chargePlug: Int = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
//        val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
//        val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
//
//        if (isCharging || usbCharging || acCharging) {
            if (intent?.action == Intent.ACTION_POWER_CONNECTED) {
                val i = Intent(context!!, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(i)
//            }
        }
    }
}