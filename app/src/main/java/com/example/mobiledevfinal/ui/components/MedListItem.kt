package com.example.mobiledevfinal.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.unit.dp
import com.example.mobiledevfinal.models.Medication

@Composable
fun MedListItem(
    med: Medication,
    onEditPressed: () -> Unit = {}
) {
    Surface(
        elevation = 2.dp,
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFFF9F9F9),
    ) {
        Column (){
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Name: ${med.medName ?: ""}", style = MaterialTheme.typography.body1)
                }
                IconButton(onClick = onEditPressed) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit button", tint = Color(0xFFC7C7C7))
                }
            }
            Row(modifier = Modifier.padding(16.dp, 0.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Text(text = "Quantity: ${med.quantity ?: ""} pills")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(16.dp, 0.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Text(text = "Time: ${med.time ?: ""}:00 Hour(24h)")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(16.dp, 0.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Text(
                    text = "Dosage: ${med.dosage ?: ""} mg",
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(text= "Streak: ${med.streak ?: ""} days", style = MaterialTheme.typography.body1)
            }
                Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
