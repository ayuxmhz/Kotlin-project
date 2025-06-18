package com.example.c36a.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.c36a.R
import com.example.c36a.repository.AuctionRepositoryImpl
import com.example.c36a.utils.ImageUtils
import com.example.c36a.viewmodel.AuctionViewModel

class UpdateAuctionActivity : ComponentActivity() {
    lateinit var imageUtils: ImageUtils
    var selectedImageUri by mutableStateOf<Uri?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        imageUtils = ImageUtils(this, this)
        imageUtils.registerLaunchers { uri ->
            selectedImageUri = uri
        }
        setContent {
            UpdateAuctionBody(
                selectedImageUri = selectedImageUri,
                onPickImage = { imageUtils.launchImagePicker() })
        }
    }
}

@Composable
fun UpdateAuctionBody( selectedImageUri: Uri?,
                       onPickImage: () -> Unit) {
    var auctionName by remember { mutableStateOf("") }
    var auctionPrice by remember { mutableStateOf("") }
    var auctionDesc by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity

    val auctionId : String? = activity?.intent?.getStringExtra("auctionId")

    val repo = remember { AuctionRepositoryImpl() }
    val viewModel = remember { AuctionViewModel(repo) }

    val auctions = viewModel.auctions.observeAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.getAuctionById(auctionId.toString())
    }

    auctionName = auctions.value?.auctionName ?: ""
    auctionDesc = auctions.value?.auctionDesc ?: ""
    auctionPrice = auctions.value?.auctionPrice.toString()


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        onPickImage()
                    }
                    .padding(10.dp)
            ) {
                when {
                    selectedImageUri != null -> {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.img),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            OutlinedTextField(
                value = auctionName, onValueChange = {
                    auctionName = it
                }, placeholder = {
                    Text("Auction name")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = auctionDesc, onValueChange = {
                    auctionDesc = it
                }, placeholder = {
                    Text("Auction Description")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))


            OutlinedTextField(
                value = auctionPrice, onValueChange = {
                    auctionPrice = it
                }, placeholder = {
                    Text("Auction Price")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.uploadImage(context, selectedImageUri!!) { imageUrl ->
                    if(imageUrl != null){
                        var maps = mutableMapOf<String, Any?>()

                        maps["auctionId"] = auctionId
                        maps["auctionName"] = auctionName
                        maps["auctionDesc"] = auctionDesc
                        maps["auctionPrice"] = auctionPrice.toInt()
                        maps["imageUrl"] = imageUrl

                        viewModel.updateAuction(auctionId.toString(), maps) { success, message ->
                            if (success) {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                activity?.finish()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                            }
                        }
                    }
                }
            }) {
                Text("Update auction")
            }

        }
    }

}


@Preview
@Composable
fun previewUpdate(){
    UpdateAuctionBody(
        selectedImageUri = null, // or pass a mock Uri if needed
        onPickImage = {})
}