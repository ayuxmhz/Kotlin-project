package com.example.c36a.repository

import com.example.c36a.model.HouseModel
import android.content.Context
import android.net.Uri
interface HouseRepository {

    /*
    success : true,
    message : "product deleted succesfully"
     */
    fun addHouse(
        model: HouseModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateHouse(
        houseId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteHouse(
        houseId: String,
        callback: (Boolean, String) -> Unit
    )

    /*
  success : true,
  message : "house fetched succesfully"
   */
    fun getHouseById(
        houseId: String,
        callback: (Boolean, String, HouseModel?) -> Unit
    )

    fun getAllHouse(callback: (Boolean, String,
                                 List<HouseModel?>) -> Unit)

    fun uploadImage(
        context: Context, imageUri: Uri,
        callback: (String?) -> Unit
    )

    fun getFileNameFromUri(context: Context, uri: Uri): String?
    //present - true
    //absent - false
//    fun attendance(name:String,callback: (Boolean) -> Unit)
}