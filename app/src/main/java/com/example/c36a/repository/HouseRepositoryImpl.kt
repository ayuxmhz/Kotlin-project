package com.example.c36a.repository

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.c36a.model.HouseModel
import com.example.c36a.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class HouseRepositoryImpl : HouseRepository {
    val database = FirebaseDatabase.getInstance()
    val ref = database.reference.child("houses")

    //add -> setValue()
    //update -> updateChildren()
    //delete -> removeValue()
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dlebqgpz6",
            "api_key" to "973342636882269",
            "api_secret" to "fnMlfGJCxyPnJqLU2ehqPpXYCFs"
        )
    )

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                // ✅ Fix: Remove extensions from file name before upload
                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                //  Run UI updates on the Main Thread
                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    override fun addHouse(
        model: HouseModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = ref.push().key.toString()
        model.houseId = id
        ref.child(id).setValue(model).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"House added")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun getHouseById(
        houseId: String,
        callback: (Boolean, String, HouseModel?) -> Unit
    ) {
        ref.child(houseId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var houses = snapshot.getValue(HouseModel::class.java)
//                    if(houses != null){
//                        callback(true,"Fetched",houses)
//                    }

                    houses?.let {
                        callback(true,"Fetched",it)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,null)
            }

        })
    }

    override fun getAllHouse(callback: (Boolean, String, List<HouseModel?>) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val allHouses = mutableListOf<HouseModel>()
                if(snapshot.exists()){
                    for(eachData in snapshot.children){
                        var houses = eachData.getValue(HouseModel::class.java)
                        if(houses != null){
                            allHouses.add(houses)
                        }
                    }
                    callback(true,"fetched",allHouses)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,emptyList())
            }

        })
    }

    override fun updateHouse(
        houseId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(houseId).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"House updated")
            }else{
                callback(false,"${it.exception?.message}")

            }
        }
    }

    override fun deleteHouse(
        houseId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(houseId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"House deleted")
            }else{
                callback(false,"${it.exception?.message}")

            }
        }
    }
}