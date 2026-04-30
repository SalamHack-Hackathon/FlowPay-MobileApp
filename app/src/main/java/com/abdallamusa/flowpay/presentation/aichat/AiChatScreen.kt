package com.abdallamusa.flowpay.presentation.aichat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abdallamusa.flowpay.R
import com.abdallamusa.flowpay.presentation.viewmodel.AiChatViewModel
import com.abdallamusa.flowpay.presentation.viewmodel.AiChatUiState
import com.abdallamusa.flowpay.presentation.components.FlowPayTopAppBar
import com.abdallamusa.flowpay.presentation.components.TopBarStyle
import com.abdallamusa.flowpay.ui.theme.BackgroundDark
import com.abdallamusa.flowpay.ui.theme.ChatBubbleAI
import com.abdallamusa.flowpay.ui.theme.ChatBubbleUser
import com.abdallamusa.flowpay.ui.theme.EmeraldPrimary
import com.abdallamusa.flowpay.ui.theme.TextPrimary
import com.abdallamusa.flowpay.ui.theme.TextSecondary
import com.abdallamusa.flowpay.utils.Strings
import com.abdallamusa.flowpay.domain.model.ChatMessage
import com.abdallamusa.flowpay.ui.theme.CardBackground
import com.abdallamusa.flowpay.ui.theme.SurfaceDark
import kotlinx.coroutines.launch

@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AiChatScreenContent(
        uiState = uiState,
        onSendMessage = { viewModel.sendMessage(it) }
    )
}

@Composable
fun AiChatScreenContent(
    uiState: AiChatUiState,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                val layoutInfo = listState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                val lastIndex = uiState.messages.size - 1

                if (lastVisibleItem == null || lastVisibleItem.index < lastIndex) {
                    val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                    val scrollDistance = (lastIndex - (lastVisibleItem?.index ?: 0)) * 100f
                    listState.animateScrollBy(
                        value = scrollDistance.coerceAtMost(viewportHeight * 2f),
                        animationSpec = tween(
                            durationMillis = 450,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                items(uiState.messages) { message ->
                    ChatMessageItem(message = message)
                }
                if (uiState.isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            ChatInputField(
                onSendMessage = onSendMessage
            )
    }
}

@Composable
fun ChatMessageItem(message: com.abdallamusa.flowpay.domain.model.ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
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
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isFromUser) {
                        ChatBubbleUser
                    } else {
                        ChatBubbleAI
                    }
                ),
                shape = RoundedCornerShape(
                    topStart = if (message.isFromUser) 16.dp else 4.dp,
                    topEnd = if (message.isFromUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US).format(java.util.Date(message.timestamp)),
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                if (message.isFromUser) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
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
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = ChatBubbleAI
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    val animatable = remember { Animatable(0f) }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInputField(
    onSendMessage: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = {
                Text(
                    text = Strings.AiChat.INPUT_HINT,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.isNotBlank()) {
                        onSendMessage(message)
                        message = ""
                    }
                }
            ),
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary)
                        .clickable {
                            if (message.isNotBlank()) {
                                onSendMessage(message)
                                message = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = null,
                        tint = BackgroundDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AiChatScreenPreview() {
    val mockMessages = listOf(
        ChatMessage(
            id = "1",
            text = "مرحباً! كيف يمكنني مساعدتك اليوم؟",
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        ),
        ChatMessage(
            id = "2",
            text = "أريد معرفة كيفية إضافة عميل جديد",
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        ),
        ChatMessage(
            id = "3",
            text = "يمكنك إضافة عميل جديد من خلال الضغط على زر + في شاشة العملاء، ثم ملء البيانات المطلوبة مثل اسم الشركة واسم جهة الاتصال.",
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        )
    )
    
    AiChatScreenContent(
        uiState = AiChatUiState(
            messages = mockMessages,
            isTyping = false
        ),
        onSendMessage = {}
    )
}
