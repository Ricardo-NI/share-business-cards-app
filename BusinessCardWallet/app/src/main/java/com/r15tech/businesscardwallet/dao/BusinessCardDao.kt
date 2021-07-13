package com.r15tech.businesscardwallet.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.r15tech.businesscardwallet.entity.BusinessCard

@Dao
interface BusinessCardDao {

    @Query("SELECT * FROM businesscard ORDER BY id ASC")
    fun getAll(): LiveData<List<BusinessCard>>

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insert(businessCard: BusinessCard)

    @Update
    suspend fun updateData(businessCard: BusinessCard)

    @Delete
    suspend fun deleteData(businessCard: BusinessCard)

}