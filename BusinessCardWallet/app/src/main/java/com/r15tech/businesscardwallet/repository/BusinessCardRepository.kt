package com.r15tech.businesscardwallet.repository

import com.r15tech.businesscardwallet.dao.BusinessCardDao
import com.r15tech.businesscardwallet.entity.BusinessCard


class BusinessCardRepository(private val dao: BusinessCardDao) {

    fun getAll() =  dao.getAll()

    suspend fun insert(businessCard: BusinessCard){
            dao.insert(businessCard)
    }

    suspend fun updateData(businessCard: BusinessCard){
            dao.updateData(businessCard)
    }

    suspend fun deleteData(businessCard: BusinessCard){
            dao.deleteData(businessCard)
    }

}