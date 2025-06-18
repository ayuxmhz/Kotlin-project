package com.example.c36a.repository

import com.example.c36a.model.AuctionModel
import android.content.Context
import android.net.Uri
interface AuctionRepository {

    /*
    success : true,
    message : "product deleted succesfully"
     */
    fun addAuction(
        model: AuctionModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateAuction(
        auctionId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteAuction(
        auctionId: String,
        callback: (Boolean, String) -> Unit
    )

    /*
  success : true,
  message : "auction fetched succesfully"
   */
    fun getAuctionById(
        auctionId: String,
        callback: (Boolean, String, AuctionModel?) -> Unit
    )

    fun getAllAuction(callback: (Boolean, String,
                                 List<AuctionModel?>) -> Unit)

    fun uploadImage(
        context: Context, imageUri: Uri,
        callback: (String?) -> Unit
    )

    fun getFileNameFromUri(context: Context, uri: Uri): String?
    //present - true
    //absent - false
//    fun attendance(name:String,callback: (Boolean) -> Unit)
}