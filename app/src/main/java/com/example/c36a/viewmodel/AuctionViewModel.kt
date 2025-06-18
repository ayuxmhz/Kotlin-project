package com.example.c36a.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.c36a.model.AuctionModel
import com.example.c36a.repository.AuctionRepository
import android.content.Context
import android.net.Uri

class AuctionViewModel(val repo: AuctionRepository) : ViewModel() {

    fun addAuction(
        model: AuctionModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addAuction(model, callback)
    }

    fun updateAuction(
        auctionId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateAuction(auctionId, data, callback)
    }

    fun deleteAuction(
        auctionId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteAuction(auctionId, callback)
    }

    private val _auctions = MutableLiveData<AuctionModel?>()
    val auctions: LiveData<AuctionModel?> get() = _auctions

    private val _allAuctions = MutableLiveData<List<AuctionModel?>>()
    val allAuctions: LiveData<List<AuctionModel?>> get() = _allAuctions


    fun getAuctionById(
        auctionId: String,
    ) {
        repo.getAuctionById(auctionId) { success, msg, data ->
            if (success) {
                _auctions.postValue(data)
            } else {
                _auctions.postValue(null)

            }
        }
    }

    private var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading


    fun getAllAuction() {
        _loading.postValue(true)
        repo.getAllAuction { success, msg, data ->
            if (success) {
                _loading.postValue(false)
                _allAuctions.postValue(data)
            } else {
                _loading.postValue(false)
                _allAuctions.postValue(emptyList())

            }
        }
    }
    fun uploadImage(context: Context,imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadImage(context,imageUri,callback)
    }


}