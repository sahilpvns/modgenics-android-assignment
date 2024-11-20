package com.modgenics.mypaymentapp

import android.R
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.modgenics.mypaymentapp.ui.theme.utils.Constant
import com.modgenics.mypaymentapp.ui.theme.utils.Constant.captureComposableAsBitmap
import com.modgenics.mypaymentapp.ui.theme.utils.Constant.shareBitmap
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class UserDetailsActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Checkout.preload(applicationContext)

        setContent {
            val username = intent.getStringExtra("username")
            val designation = intent.getStringExtra("designation")
            val mobile = intent.getStringExtra("mobile")
            val email = intent.getStringExtra("email")
            val company = intent.getStringExtra("company")

            VisitingCardScreen(username, designation, mobile, email, company, ::startPayment)
        }
    }

    private fun startPayment(amount: Int, email: String?, mobile: String?) {
        val checkout = Checkout()
        checkout.setKeyID(Constant.API_KEY)

        try {
            val options = JSONObject()
            options.put("name", "Modgenics Technology")
            options.put("description", "Test Payment")
            options.put("currency", "INR")
            options.put("amount", amount * 100)

            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("contact", mobile)
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_LONG).show()
        shareWhatsapp()
    }

    private fun shareWhatsapp() {

    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun VisitingCardScreen(
    username: String?,
    designation: String?,
    mobile: String?,
    email: String?,
    company: String?,
    onPayClick: (amount: Int, email: String?, mobile: String?) -> Unit
) {

    val content = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Preview Your Visiting Card",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            VisitingCard(username, designation, mobile, email, company)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                    onPayClick(500, email, mobile)
                }, modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)) {
                Text(text = "Pay ₹500")
            }

            Spacer(Modifier.padding(16.dp))

        /*    Button(onClick = {
                val context = content
                val bitmap = captureComposableAsBitmap(context) {
                    VisitingCard(username, designation, mobile, email, company)
                }
                shareBitmap(context,bitmap)
            }, modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)) {
                Text(text = "Share to WhatsApp")
            }*/

        }
    }
}


@Composable
fun VisitingCard(username: String?, designation: String?, mobile: String?, email: String?, company: String?) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF424242)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = username.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp, color = Color.White)
            Text(
                text = designation.toString(),
                fontSize = 14.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 16.dp))

            InfoRow(iconId = R.drawable.ic_menu_call, info = "Mobile Number: $mobile")
            InfoRow(iconId = R.drawable.ic_dialog_email, info = "Email: $email")
            InfoRow(iconId = R.drawable.ic_menu_agenda, info = "Company Name: $company")
        }
    }
}

@Composable
fun InfoRow(iconId: Int, info: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "",
            tint = Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = info, fontSize = 14.sp, color = Color.LightGray
        )
    }
}
