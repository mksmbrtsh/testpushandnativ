package com.mksmbrtsh.myhashapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val EMPTY_LIST: Int = -1;
        const val PUSH_ID_NOTIF: Int = 232
        const val NEW_PUSH_ACTION = "newPush"
        const val NEW_TOKEN_ACTION = "newToken"
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var count = EMPTY_LIST;
        remoteMessage.data?.let {
                    val inputDataToCalc = it.get("to_calc")
                    if(inputDataToCalc != null) {
                        val db = Room.databaseBuilder(
                            applicationContext,
                            MessageDatabase::class.java, "database-message"
                        ).build()
                        var hash = stringFromJNI(inputDataToCalc)
                        val newMessage = Message(inputDataToCalc, hash, Date())
                        db.messageDao().insertAll(newMessage)
                        count = db.messageDao().getAll().size
                        sendNotification(count);
                        val intent = Intent(NEW_PUSH_ACTION)
                        sendBroadcast(intent)
                        return
                    }
            }
        sendNotification(count);
    }

    override fun onNewToken(token: String) {
        Log.d(MyFirebaseMessagingService::class.simpleName, "Refreshed token: $token")
        sendRegistrationToServer(token)
        // only for see token
        getSharedPreferences("fcm", MODE_PRIVATE).edit().putString("f", token).apply();
        val intent = Intent(NEW_TOKEN_ACTION)
        sendBroadcast(intent)
    }
    private fun sendRegistrationToServer(token: String?) {
        // send token to our server for pushes
        Log.d(MyFirebaseMessagingService::class.simpleName, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(count: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        var str:String
        var title:String
        if(count >=0) {
            title = String.format(getString(R.string.fcm_message), count);
            str=""
        } else {
            title = getString(R.string.fcm_message_error)
            str = String.format(
                getString(R.string.fcm_message_error_help),
                getSharedPreferences("fcm", MODE_PRIVATE).getString("f", "")
            )
        }
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(str))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(PUSH_ID_NOTIF, notificationBuilder.build())
    }
    // simple bridge for marshaling from/to unmanager code
    private external fun stringFromJNI(str:String): String
}