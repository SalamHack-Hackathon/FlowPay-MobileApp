package com.abdallamusa.flowpay.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdallamusa.flowpay.utils.Strings
import androidx.compose.ui.tooling.preview.Preview
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary

sealed class TopBarStyle {
    object Logo : TopBarStyle()
    object LogoWithActions : TopBarStyle()
    data class Title(val title: String) : TopBarStyle()
    data class Dashboard(val greeting: String, val name: String) : TopBarStyle()
    data class AiChat(val title: String = "FlowAI") : TopBarStyle()
}

@Composable
fun FlowPayTopAppBar(
    style: TopBarStyle,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundDark)
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (style) {
            is TopBarStyle.Logo -> {
                Spacer(modifier = Modifier.width(48.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LogoText()
                }
                Spacer(modifier = Modifier.width(48.dp))
            }

            is TopBarStyle.LogoWithActions -> {
                ActionIcons(
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LogoText()
                }
                Spacer(modifier = Modifier.width(80.dp))
            }

            is TopBarStyle.Title -> {
                ActionIcons(
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = style.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(80.dp))
            }

            is TopBarStyle.Dashboard -> {
                ActionIcons(
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
                Spacer(modifier = Modifier.width(24.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = style.greeting,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary
                    )
                    Text(
                        text = style.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            is TopBarStyle.AiChat -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = BackgroundDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = style.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = TextPrimary
                    )
                }
            }
        }
    }
    }
}

@Composable
private fun LogoText() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = Strings.APP_NAME,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(2.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary)
        )
    }
}

@Composable
private fun ActionIcons(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = Strings.Dashboard.NOTIFICATIONS,
            tint = TextPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onNotificationClick() }
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = Strings.Dashboard.PROFILE,
                tint = BackgroundDark,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
fun FlowPayTopAppBarLogoPreview() {
    FlowPayTopAppBar(style = TopBarStyle.Logo)
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
fun FlowPayTopAppBarLogoWithActionsPreview() {
    FlowPayTopAppBar(style = TopBarStyle.LogoWithActions)
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
fun FlowPayTopAppBarTitlePreview() {
    FlowPayTopAppBar(style = TopBarStyle.Title("Create Invoice"))
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
fun FlowPayTopAppBarDashboardPreview() {
    FlowPayTopAppBar(style = TopBarStyle.Dashboard(greeting = "صباح الخير", name = "احمد"))
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
fun FlowPayTopAppBarAiChatPreview() {
    FlowPayTopAppBar(style = TopBarStyle.AiChat())
}
