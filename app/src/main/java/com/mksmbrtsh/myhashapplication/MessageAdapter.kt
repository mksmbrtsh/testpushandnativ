package com.mksmbrtsh.myhashapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.text.DateFormat

class MessageAdapter(private val msgs: MutableList<Message>) : Adapter<MessageAdapter.MessageViewHolder>() {

    val dateFormat : DateFormat = DateFormat.getInstance()

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var msg: TextView
        var hash: TextView
        var date: TextView
        init {
            msg = itemView.findViewById(R.id.msg)
            hash = itemView.findViewById(R.id.hash)
            date = itemView.findViewById(R.id.date)
        }
    }

    fun addLastToTop(msg:Message) {
        msgs.add(0,msg)
    }

    override fun getItemCount() = msgs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.msg?.text = msgs[position].input
        holder.hash?.text = msgs[position].hash
        holder.date?.text =dateFormat.format(msgs[position].date)
    }
}