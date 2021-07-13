package com.r15tech.businesscardwallet.util

import com.r15tech.businesscardwallet.entity.BusinessCard

interface CellCardClickListener {
    fun onEditCellClickListener (businessCard: BusinessCard, toDelete: Boolean )
}