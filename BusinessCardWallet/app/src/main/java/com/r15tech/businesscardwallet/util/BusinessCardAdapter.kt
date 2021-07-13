package com.r15tech.businesscardwallet.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.r15tech.businesscardwallet.R
import com.r15tech.businesscardwallet.entity.BusinessCard
import com.r15tech.businesscardwallet.databinding.ItemBusinessCardBinding

class BusinessCardAdapter(private val context: Context,
                          private val cellCardClickListener: CellCardClickListener)
    : ListAdapter<BusinessCard, BusinessCardAdapter.ViewHolder>(DiffCallback()) {

    var lintenerShare: (View) -> Unit = {}

    inner class ViewHolder(private val binding: ItemBusinessCardBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: BusinessCard, context: Context, listenerS: (View) -> Unit){
            binding.txvName.text = item.name
            binding.txvPhone.text = item.phone
            binding.txvEmail.text = item.email
            binding.txvCompanyName.text = item.company

            binding.txvName.setTextColor(Color.parseColor(item.textColor))
            binding.txvPhone.setTextColor(Color.parseColor(item.textColor))
            binding.txvEmail.setTextColor(Color.parseColor(item.textColor))
            binding.txvCompanyName.setTextColor(Color.parseColor(item.textColor))

            binding.cardviewCard.setCardBackgroundColor(Color.parseColor(item.backColor))
            binding.cardviewCard.setOnClickListener {
                hideButtonMore()
                listenerS(it)
            }

            binding.btnMore.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.textColor))
            binding.btnMore.setOnClickListener {
                showPopup(context, binding.btnMore, item)
            }
        }

        //esconder o botão de edição do card, para não aparecer no screenShot.
        private fun hideButtonMore() {
            binding.btnMore.visibility = View.GONE
            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    binding.btnMore.visibility = View.VISIBLE
                }
            }
            timer.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBusinessCardBinding.inflate(inflater,parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),context, lintenerShare)
    }

    private fun showPopup(context: Context, view: View, businessCard: BusinessCard){
        val popup = PopupMenu(context, view)

            popup.inflate(R.menu.menu_item)
            popup.setOnMenuItemClickListener { item: MenuItem? ->

                when (item!!.itemId) {
                    R.id.header1 -> {
                        cellCardClickListener.onEditCellClickListener(businessCard,false)
                    }
                    R.id.header2 -> {
                        cellCardClickListener.onEditCellClickListener(businessCard,true)
                    }
                }
                true
            }

        popup.show()
    }
}

class DiffCallback: DiffUtil.ItemCallback<BusinessCard>(){

    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard)= oldItem == newItem
    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard)= oldItem.id == newItem.id

}