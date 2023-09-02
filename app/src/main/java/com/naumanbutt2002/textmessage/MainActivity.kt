package com.naumanbutt2002.textmessage

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission


class MainActivity : ComponentActivity() {

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

        if (isGranted) {
            // Permission granted, you can send the SMS.
            sendSms()
        } else {
            // Permission denied, handle accordingly.
            // You can show a message to the user indicating that the SMS cannot be sent without permission.
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SmsCompose()
                }
            }
        }
    }
    var textToSend by mutableStateOf("")
    var phoneNumber by mutableStateOf("")

    fun sendSms() {

        val smsManager = SmsManager.getDefault()
        val phNo = phoneNumber
        val msg = textToSend
        smsManager.sendTextMessage(phNo, null, msg, null, null)
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun SmsCompose() {
//        var textToSend by remember { mutableStateOf("") }
//        var phoneNumber   by remember { mutableStateOf("") } // Replace with the recipient's phone number

        val context = LocalContext.current // Access the Context using LocalContext.current
         Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
             Spacer(modifier = Modifier.padding(30.dp))

             Text(
                 text ="Send Message to Anyone",
                 fontWeight= FontWeight.Bold,
                 fontSize=32.sp,
                 fontStyle = FontStyle.Italic,
             )
             Spacer(modifier = Modifier.padding(15.dp))

             OutlinedTextField(
                modifier=Modifier.fillMaxWidth(0.8f),

                value =phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Send
                ),
                singleLine=true,
                label ={Text(text="Sent To")},
                placeholder={Text(text="Enter Phone NO ")},
                leadingIcon = { Icon(Icons.Filled.Phone, null) },

                )
            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                modifier=Modifier.fillMaxWidth(0.8f),
                value =textToSend,
                onValueChange = {
                    textToSend = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                label ={Text(text="Message")},
                placeholder={Text(text="Enter Message ")},
                leadingIcon = { Icon(Icons.Filled.Email, null) },
                maxLines=5,
                )
            Spacer(modifier = Modifier.padding(10.dp))

            val mContext= LocalContext.current

            Button(
                onClick = {
                    val permission = Manifest.permission.SEND_SMS
                    if (checkSelfPermission(context, permission) == PERMISSION_GRANTED) {
                        // Permission already granted, send SMS
                         sendSms()
                        mToast(context = mContext)

                    } else {
                        // Permission not granted, request it
                        requestPermissionLauncher.launch(permission)
                    }
                }
            )
            {
                Text("Send SMS")
            }

        }
    }

    private fun mToast(context: Context) {
        Toast.makeText(context, "Msg Sent", Toast.LENGTH_SHORT).show()

    }
}
