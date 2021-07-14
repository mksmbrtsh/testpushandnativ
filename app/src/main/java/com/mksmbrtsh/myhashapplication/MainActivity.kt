package com.mksmbrtsh.myhashapplication

import android.app.NotificationManager
import android.content.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.mksmbrtsh.myhashapplication.MyFirebaseMessagingService.Companion.NEW_PUSH_ACTION
import com.mksmbrtsh.myhashapplication.MyFirebaseMessagingService.Companion.NEW_TOKEN_ACTION
import com.mksmbrtsh.myhashapplication.MyFirebaseMessagingService.Companion.PUSH_ID_NOTIF
import com.mksmbrtsh.myhashapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var filter: IntentFilter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.list.layoutManager = LinearLayoutManager(this);
        GlobalScope.launch(Dispatchers.IO) {
            // not block UI
            val db = Room.databaseBuilder(
                applicationContext,
                MessageDatabase::class.java, "database-message").build()
            adapter = MessageAdapter(db.messageDao().getAll());
            withContext(Dispatchers.Main) {
                binding.list.adapter = adapter;
                binding.header.text = String.format(getString(R.string.fcm_message), adapter.itemCount);
            }
        }
        filter = IntentFilter()
        filter.addAction(NEW_PUSH_ACTION)
        filter.addAction(NEW_TOKEN_ACTION)
        setCurrentTokenToUI()
        binding.token.setOnClickListener({ v -> copyToClipboard() })
    }
    /*
    if nofification shown, delete them
     */
    private fun closeNotif() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(PUSH_ID_NOTIF);
    }
    /*
    only for request to fcm server
     */
    private fun setCurrentTokenToUI() {
        binding.token.text = String.format(getString(R.string.current_token), getSharedPreferences("fcm", MODE_PRIVATE).getString("f", getString(R.string.not_response_token)))
    }

    fun Context.copyToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.app_name), binding.token.text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copy_clipboard_label), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        closeNotif()
        registerReceiver(broadcastForPush, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastForPush)
    }

    private val broadcastForPush: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when {
                    it.action.equals(NEW_PUSH_ACTION) ->
                        GlobalScope.launch(Dispatchers.IO) {
                            // no block UI
                            val db = Room.databaseBuilder(
                                context!!,
                                MessageDatabase::class.java, "database-message"
                            ).build()
                            var newItem = db.messageDao().getLast()
                            adapter.addLastToTop(newItem)
                            withContext(Dispatchers.Main) {
                                adapter.notifyItemInserted(0)
                                val layoutManager =
                                    binding.list.getLayoutManager() as LinearLayoutManager
                                layoutManager.scrollToPosition(0)
                                binding.header.text = String.format(
                                    getString(R.string.fcm_message),
                                    adapter.itemCount
                                );
                                closeNotif()
                            }
                        }
                    it.action.equals(NEW_TOKEN_ACTION) -> setCurrentTokenToUI()
                    else -> {
                    }
                }
            }
        }
    }
}


