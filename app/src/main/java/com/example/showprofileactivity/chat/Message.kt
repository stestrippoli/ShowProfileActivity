package com.example.showprofileactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Message(val text: String = "", val user: String = "", val creator: Boolean = false, val time: String = "", val day: String = "")


class MessageAdapter(val data: List<Message>, val user: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MeMessageViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val text1: TextView = v.findViewById(R.id.message1_text)
        val time1: TextView = v.findViewById(R.id.message1_time)
        val day1: TextView = v.findViewById(R.id.message1_day)
    }

    class OtherMessageViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val text2: TextView = v.findViewById(R.id.message2_text)
        val time2: TextView = v.findViewById(R.id.message2_time)
        val day2: TextView = v.findViewById(R.id.message2_day)
    }

    override fun getItemViewType(position: Int): Int {
        val m = data[position].user == user
        if(m)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vg1 = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_message_layout, parent, false)
        val vg2 = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.other_user_message_layout, parent, false)
        if(viewType==0)
            return MeMessageViewHolder(vg1)
        return OtherMessageViewHolder(vg2)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType==0){
            onBindMeMessage(holder, data[position], position)
        }
        else {
            onBindOtherMessage(holder, data[position], position)
        }
    }

    fun onBindMeMessage(holder: RecyclerView.ViewHolder, message:Message, p:Int) {
        val meHolder = holder as MeMessageViewHolder
        if(p==0) {
            meHolder.day1.text = message.day
        }
        else if(message.day!=data[p-1].day){
            meHolder.day1.text = message.day
        }
        else{
            meHolder.day1.visibility = View.GONE
        }
        meHolder.text1.text = message.text
        meHolder.time1.text = message.time
    }

    fun onBindOtherMessage(holder: RecyclerView.ViewHolder, message:Message, p:Int) {
        val otherHolder = holder as OtherMessageViewHolder
        if(p==0) {
            otherHolder.day2.text = message.day
        }
        else if(message.day!=data[p-1].day) {
            otherHolder.day2.text = message.day
        }
        else {
            otherHolder.day2.visibility = View.GONE
        }
        otherHolder.text2.text = message.text
        otherHolder.time2.text = message.time
    }

    override fun getItemCount(): Int {
        return data.size
    }
}