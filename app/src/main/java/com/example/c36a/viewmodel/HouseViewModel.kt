package com.example.c36a.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.c36a.model.HouseModel
import com.example.c36a.repository.HouseRepository
import android.content.Context
import android.net.Uri

class HouseViewModel(val repo: HouseRepository) : ViewModel() {

    fun addHouse(
        model: HouseModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addHouse(model, callback)
    }

    fun updateHouse(
        houseId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateHouse(houseId, data, callback)
    }

    fun deleteHouse(
        houseId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteHouse(houseId, callback)
    }

    private val _houses = MutableLiveData<HouseModel?>()
    val houses: LiveData<HouseModel?> get() = _houses

    private val _allHouses = MutableLiveData<List<HouseModel?>>()
    val allHouses: LiveData<List<HouseModel?>> get() = _allHouses


    fun getHouseById(
        houseId: String,
    ) {
        repo.getHouseById(houseId) { success, msg, data ->
            if (success) {
                _houses.postValue(data)
            } else {
                _houses.postValue(null)

            }
        }
    }

    private var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading


    fun getAllHouse() {
        _loading.postValue(true)
        repo.getAllHouse { success, msg, data ->
            if (success) {
                _loading.postValue(false)
                _allHouses.postValue(data)
            } else {
                _loading.postValue(false)
                _allHouses.postValue(emptyList())

            }
        }
    }
    fun uploadImage(context: Context,imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadImage(context,imageUri,callback)
    }


}