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
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.c36a.model.HouseModel
import com.example.c36a.repository.HouseRepositoryImpl
import com.example.c36a.utils.ImageUtils
import com.example.c36a.view.ui.theme.C36ATheme
import com.example.c36a.viewmodel.HouseViewModel

class UpdateHouseActivity : ComponentActivity() {
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
            UpdateHouseBody(
                selectedImageUri = selectedImageUri,
                onPickImage = { imageUtils.launchImagePicker() })
        }
    }
}

@Composable
fun UpdateHouseBody( selectedImageUri: Uri?,
                       onPickImage: () -> Unit) {
    var houseName by remember { mutableStateOf("") }
    var housePrice by remember { mutableStateOf("") }
    var houseDesc by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity

    val houseId : String? = activity?.intent?.getStringExtra("houseId")

    val repo = remember { HouseRepositoryImpl() }
    val viewModel = remember { HouseViewModel(repo) }

    val houses = viewModel.houses.observeAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.getHouseById(houseId.toString())
    }

    houseName = houses.value?.houseName ?: ""
    houseDesc = houses.value?.houseDesc ?: ""
    housePrice = houses.value?.housePrice.toString()


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
                value = houseName, onValueChange = {
                    houseName = it
                }, placeholder = {
                    Text("House name")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = houseDesc, onValueChange = {
                    houseDesc = it
                }, placeholder = {
                    Text("House Description")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))


            OutlinedTextField(
                value = housePrice, onValueChange = {
                    housePrice = it
                }, placeholder = {
                    Text("House Price")
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.uploadImage(context, selectedImageUri!!) { imageUrl ->
                    if(imageUrl != null){
                        var maps = mutableMapOf<String, Any?>()

                        maps["houseId"] = houseId
                        maps["houseName"] = houseName
                        maps["houseDesc"] = houseDesc
                        maps["housePrice"] = housePrice.toInt()
                        maps["imageUrl"] = imageUrl

                        viewModel.updateHouse(houseId.toString(), maps) { success, message ->
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
                Text("Update house")
            }

        }
    }

}


@Preview
@Composable
fun previewUpdate(){
    UpdateHouseBody(
        selectedImageUri = null, // or pass a mock Uri if needed
        onPickImage = {})
}