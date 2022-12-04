package com.lnight.encriptiondecriptionproject

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lnight.encriptiondecriptionproject.ui.theme.EncriptionDecriptionProjectTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoManager = CryptoManager()
        setContent {
            EncriptionDecriptionProjectTheme {
                var messageToEncrypt by remember {
                    mutableStateOf("")
                }
                var messageToDecrypt by remember {
                    mutableStateOf("")
                }
                var showToast by remember {
                    mutableStateOf("")
                }

                if(showToast.isNotBlank()) {
                    Toast.makeText(LocalContext.current, showToast, Toast.LENGTH_SHORT).show()
                    showToast = ""
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    TextField(
                        value = messageToEncrypt,
                        onValueChange = { messageToEncrypt = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Encrypt string") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = {
                            if(messageToEncrypt.isNotBlank()) {
                                val bytes = messageToEncrypt.encodeToByteArray()
                                val file = File(filesDir, "secret.txt")
                                if (!file.exists()) {
                                    file.createNewFile()
                                }
                                val fos = FileOutputStream(file)
                                messageToDecrypt = cryptoManager.encrypt(
                                    bytes = bytes,
                                    outputStream = fos
                                ).decodeToString()
                            } else {
                                showToast = "String cannot be empty!"
                            }
                        }) {
                            Text(text = "Encrypt")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            val file = File(filesDir, "secret.txt")
                            if(file.exists()) {
                            messageToEncrypt = cryptoManager.decrypt(
                                inputStream = FileInputStream(file)
                            ).decodeToString()
                            } else {
                                showToast = "You should encrypt string before!"
                            }
                        }) {
                            Text(text = "Decrypt")
                        }
                    }
                    Text(text = messageToDecrypt)
                }
            }
        }
    }
}