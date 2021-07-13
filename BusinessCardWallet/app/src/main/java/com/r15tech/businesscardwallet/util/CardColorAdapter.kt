package com.r15tech.businesscardwallet.util

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.r15tech.businesscardwallet.R
import com.r15tech.businesscardwallet.entity.ColorItem


class CardColorAdapter(private val cellColorClickListener: CellColorClickListener)
    : RecyclerView.Adapter<CardColorAdapter.MyViewHolder>() {

    private var objList = emptyList<ColorItem>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: CardView = itemView.findViewById(R.id.card_color)

        fun bind(colorList: ColorItem) {

            val color = colorList.color
            card.setCardBackgroundColor(Color.parseColor(color))
        }
        companion object {
            fun create(parent: ViewGroup): MyViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_color, parent, false)
                return MyViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = objList[position]

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            cellColorClickListener.onCellClickListener(position)
        }
    }

    fun setData(obj: List<ColorItem>) {
        this.objList = obj
        notifyDataSetChanged()
    }
}