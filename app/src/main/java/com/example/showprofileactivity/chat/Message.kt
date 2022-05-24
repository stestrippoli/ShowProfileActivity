package com.example.showprofileactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Message(val text: String, val user: String, val creator: Boolean, val time: String, val day: String)


class MessageAdapter(val data: List<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        val m = data[position].creator
        if(m)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        println("debug,  dentro")
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
        println("debug, sono dentro")
        println(data)
        if(holder.itemViewType==0){
            onBindMeMessage(holder, data[position], position)
        }
        else {
            onBindOtherMessage(holder, data[position], position)
        }
    }

    fun onBindMeMessage(holder: RecyclerView.ViewHolder, message:Message, p:Int) {
        val meHolder = holder as MeMessageViewHolder
        holder.text1.text = message.text
        holder.time1.text = message.time
        if(p==0) {
            holder.day1.text = message.day
        }
        else if(message.day!=data[p-1].day){
            holder.day1.text = message.day
        }
        else{
            holder.day1.visibility = View.GONE
        }
    }

    fun onBindOtherMessage(holder: RecyclerView.ViewHolder, message:Message, p:Int) {
        val meHolder = holder as OtherMessageViewHolder
        if(p==0) {
            holder.day2.text = message.day
        }
        else if(message.day!=data[p-1].day) {
            holder.day2.text = message.day
        }
        else {
            holder.day2.visibility = View.GONE
        }
        holder.text2.text = message.text
        holder.time2.text = message.time
    }

    override fun getItemCount(): Int {
        return data.size
    }
}