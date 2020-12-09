package android.hromovych.com.tiras_test.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hromovych.com.tiras_test.MainActivity
import android.os.Build
import android.widget.Toast


class StartSlideShowReceiver : BroadcastReceiver() {
    /**If an app registers to receive broadcasts, the app's receiver consumes resources every time
     * the broadcast is sent. This can cause problems if too many apps register to receive
     * broadcasts based on system events; a system event that triggers a broadcast can cause all of
     * those apps to consume resources in rapid succession, impairing the user experience. To
     * mitigate this problem, Android 7.0 (API level 24) placed limitations on broadcasts, as
     * described in Background Optimization. Android 8.0 (API level 26) makes these limitations
     * more stringent.
     * */
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "receive", Toast.LENGTH_SHORT).show()
        context?.startActivity(Intent(context, MainActivity::class.java))
    }

//    fun setAlarm(context: Context){
//        val cal: Calendar = Calendar.getInstance()
//        // add 30 seconds to the calendar object
//        cal.add(Calendar.SECOND, 10)
//        val intent = Intent(context, StartSlideShowReceiver::class.java)
//        val sender =
//            PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // Get the AlarmManager service
//        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        am[AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()] = sender
//    }

    fun setAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, StartSlideShowReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis()+(1000 * 30), pendingIntent)
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis()+(1000 * 30), pendingIntent)        }
    
    }
}