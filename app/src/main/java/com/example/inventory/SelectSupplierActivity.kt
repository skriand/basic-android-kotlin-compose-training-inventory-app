package com.example.inventory

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.inventory.Supplier.Companion.suppliers
import com.example.inventory.ui.theme.InventoryTheme

class SelectSupplierActivity : ComponentActivity() {
    private lateinit var textToShare: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handled = handleIntent(intent)
        if (!handled) {
            finish()
            return
        }

        setContent {
            InventoryTheme {
                SuppliersDialog(textToShare) { this.finish() }
            }
        }
    }

    private fun handleIntent(intent: Intent): Boolean {
        if (Intent.ACTION_SEND == intent.action && "text/plain" == intent.type) {
            textToShare = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
            // The intent comes from Direct share
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                intent.hasExtra(Intent.EXTRA_SHORTCUT_ID)
            ) {
                val shortcutId = intent.getStringExtra(Intent.EXTRA_SHORTCUT_ID)
            }
            return true
        }
        return false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliersDialog(textToShare: String?, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn(
                modifier = modifier
            ) {
                items(items = suppliers) { item ->
                    Card(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Sent to ${item.name} \n$textToShare",
                                Toast.LENGTH_LONG
                            ).show()
                            onDismissRequest()
                        }
                    ) {
                        Row(
                            modifier = modifier,
                        ) {
                            Image(
                                modifier = Modifier.height(32.dp),
                                painter = painterResource(R.drawable.logo_avatar),
                                contentDescription = null
                            )
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InventoryTheme {
        Greeting("Android")
    }
}