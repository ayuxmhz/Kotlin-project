package com.example.c36a.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashBody()
        }
    }
}

@Composable
fun SplashBody() {

    val context = LocalContext.current
    val activity = context as Activity

    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val localEmail: String = sharedPreferences.getString("email", "").toString()

    // Animation values
    val scaleAnimation = remember { Animatable(0f) }
    val alphaAnimation = remember { Animatable(0f) }
    val rotationAnimation = remember { Animatable(0f) }

    // Auction-themed color scheme
    val primaryColor = Color(0xFFD4AF37) // Gold
    val secondaryColor = Color(0xFF8B4513) // Rich brown
    val backgroundColor = Color(0xFF1A1A1A) // Dark elegant background
    val gradientColors = listOf(
        Color(0xFF2C1810), // Dark brown
        Color(0xFF3D2817), // Medium brown
        Color(0xFF1A1A1A)  // Charcoal
    )

    LaunchedEffect(Unit) {
        // Start animations
        scaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )

        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )

        // Continuous rotation for loading indicator
        rotationAnimation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        if (localEmail.isEmpty()) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo with Animation - Auction themed
                Box(
                    modifier = Modifier
                        .scale(scaleAnimation.value)
                        .alpha(alphaAnimation.value)
                        .background(
                            primaryColor.copy(alpha = 0.1f),
                            CircleShape
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Auction Gavel Icon (using a styled text representation)
                        Text(
                            text = "⚖️",
                            fontSize = 48.sp,
                            color = primaryColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Auction",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "PREMIUM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = primaryColor.copy(alpha = 0.8f),
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Tagline
                Text(
                    text = "Bid • Win • Collect",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier.alpha(alphaAnimation.value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover rare treasures and exclusive collectibles",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(alphaAnimation.value)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Elegant Loading Indicator
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.alpha(alphaAnimation.value)
                ) {
                    // Outer ring
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(56.dp)
                            .rotate(rotationAnimation.value),
                        color = primaryColor.copy(alpha = 0.3f),
                        strokeWidth = 2.dp
                    )

                    // Inner ring
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = primaryColor,
                        strokeWidth = 3.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Loading text
                Text(
                    text = "Preparing your auction experience...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.alpha(alphaAnimation.value)
                )
            }

            // Bottom branding
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .alpha(alphaAnimation.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Powered by Auction",
                    fontSize = 12.sp,
                    color = primaryColor.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Version 1.0 • Est. 2024",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSplash() {
    SplashBody()
}