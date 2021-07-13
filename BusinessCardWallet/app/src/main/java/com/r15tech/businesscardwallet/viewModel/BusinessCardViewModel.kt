package com.r15tech.businesscardwallet.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.r15tech.businesscardwallet.entity.BusinessCard
import com.r15tech.businesscardwallet.repository.BusinessCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class BusinessCardViewModel(private val businessCardRepository: BusinessCardRepository): ViewModel() {

    fun getAll(): LiveData<List<BusinessCard>>{
        return businessCardRepository.getAll()
    }

    fun insert(businessCard: BusinessCard)= runBlocking {
        launch(Dispatchers.IO) {
            businessCardRepository.insert(businessCard)
        }
    }

    fun updateData(businessCard: BusinessCard)= runBlocking {
        launch(Dispatchers.IO) {
            businessCardRepository.updateData(businessCard)
        }
    }
    fun deleteData(businessCard: BusinessCard)= runBlocking {
        launch(Dispatchers.IO) {
            businessCardRepository.deleteData(businessCard)
        }
    }

//    fun findByName(search: String) = runBlocking {
//        launch(Dispatchers.IO) {
//            businessCardRepository.findByName(search)
//        }
//    }

}

class MainViewModelFactory(private val repository: BusinessCardRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BusinessCardViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BusinessCardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}