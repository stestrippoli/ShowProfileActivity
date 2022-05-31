package com.example.showprofileactivity.chat

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.R
import com.example.showprofileactivity.chat.placeholder.PlaceholderContent.PlaceholderItem
import com.example.showprofileactivity.databinding.FragmentConversationBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyConversationRecyclerViewAdapter(
    private val values: List<Conversation>,
    private val user:String,
    private val name: String
) : RecyclerView.Adapter<MyConversationRecyclerViewAdapter.ViewHolder>() {
    var lastPosition = -1
    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
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
        holder.chatname.text = item.user.fullname
        holder.chatoffer.text = item.offer.title
        if(item.user.img != null)
            downloadImage(item.user.img!!, holder)
        var o = Bundle()
        o.putString("oid", item.offer.id)
        o.putString("oTitle", item.offer.title)
        o.putString("user", user)
        if(item.offer.email==user)
            o.putString("otherUser", item.user.email)
        else
            o.putString("otherUser", user)
        o.putString("cMail", item.offer.email)
        o.putString("uName", item.user.fullname)
        o.putString("myName", name)
        o.putBoolean("accepted", item.offer.accepted!!)
        o.putLong("hours", item.offer.hours!!)

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_conversationFragment2_to_fragment_chat, o) }

        }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chatname: TextView = binding.chatname
        val chatoffer: TextView = binding.chatoffer
        val img : ImageView= binding.convPropic
    }
    fun downloadImage(picture: String, holder: ViewHolder) {

        // Create a storage reference from our app
        val storageRef = FirebaseStorage.getInstance().reference
        val imgRef = storageRef.child("/images/$picture")
        val imageView = holder.img

        val localFile = File.createTempFile("images", "png")
        imgRef.getFile(localFile).addOnSuccessListener {
            val bitmap = getCroppedBitmap(
                Bitmap.createScaledBitmap(BitmapFactory.decodeFile(localFile.path),200,200,false))

            if(bitmap != null)
                imageView.setImageBitmap(bitmap)

        }.addOnFailureListener {

        }
    }
    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }
}