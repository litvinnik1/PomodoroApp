package com.example.pomodoroapp.feature_pomodoro.domain.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoroapp.R
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem

class PomodoroListAdapter(
    private val context: Context,
    val listener: PomodorosClickListener
) : RecyclerView.Adapter<PomodoroListAdapter.ItemViewHolder>() {

    private val pomodorosList =  ArrayList<LocalPomodoroItem>()
    private val fullList = ArrayList<LocalPomodoroItem>()

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pomodorosLayout = view.findViewById<CardView>(R.id.pomodoitem_layout)
        val title = view.findViewById<TextView>(R.id.pomodoroNameTv)
        val date = view.findViewById<TextView>(R.id.dateTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.pomodoroitem_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return pomodorosList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentPomodoro = pomodorosList.reversed()[position]
        holder.title.text = currentPomodoro.title
        holder.title.isSelected = true

        holder.date.text = currentPomodoro.date
        holder.date.isSelected = true

        holder.pomodorosLayout.setOnClickListener {

            listener.onItemClicked(pomodorosList.reversed()[holder.adapterPosition])

        }

        holder.pomodorosLayout.setOnLongClickListener {
            listener.onLongItemClicked(pomodorosList.reversed()[holder.adapterPosition], holder.pomodorosLayout)
            true
        }
    }

    fun updateList(newList: List<LocalPomodoroItem>){

        fullList.clear()
        fullList.addAll(newList)

        pomodorosList.clear()
        pomodorosList.addAll(fullList)
        notifyDataSetChanged()
    }

    

    interface PomodorosClickListener {
        fun onItemClicked(pomodoro: LocalPomodoroItem)
        fun onLongItemClicked(pomodoro: LocalPomodoroItem, cardView: CardView)
    }


}