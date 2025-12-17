package com.example.trash2cash.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RowScope.MenuButton(
    navController: NavController,
    route: String,
    iconRes: Int,
    text: String
) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = Modifier
            .weight(1f)
            .height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = text,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = text,
                maxLines = 1,                // ← PASTI 1 BARIS
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


