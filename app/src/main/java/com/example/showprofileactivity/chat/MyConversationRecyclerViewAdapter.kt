package com.example.showprofileactivity.chat

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.showprofileactivity.R

import com.example.showprofileactivity.chat.placeholder.PlaceholderContent.PlaceholderItem
import com.example.showprofileactivity.databinding.FragmentConversationBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyConversationRecyclerViewAdapter(
    private val values: List<Conversation>,
    private val user : String
) : RecyclerView.Adapter<MyConversationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.chatname.text = item.user
        holder.chatoffer.text = item.offer
        var o = Bundle()
        o.putString("cMail", item.user)
        o.putString("oid", item.offer)
        o.putString("user", user)
        o.putString("uName", "WEEE")
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_conversationFragment2_to_fragment_chat, o) }

        /*
            holder.itemView.setOnClickListener{
                var o = Bundle()
                o.putString("uName", )
            }

             */
        }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chatname: TextView = binding.chatname
        val chatoffer: TextView = binding.chatoffer
    }

}