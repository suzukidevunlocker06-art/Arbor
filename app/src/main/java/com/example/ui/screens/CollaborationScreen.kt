package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab

import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.UserEntity
import com.example.ui.components.VoiceWaveformPlayer
import com.example.ui.theme.ArborAlert
import com.example.ui.theme.ArborBlack
import com.example.ui.theme.ArborBorder
import com.example.ui.theme.ArborForest
import com.example.ui.theme.ArborForestLight
import com.example.ui.theme.ArborSage
import com.example.ui.theme.ArborSageDark
import com.example.ui.theme.ArborSuccess
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CollaborationScreen(
  messages: List<ChatMessageEntity>,
  activities: List<ActivityLogEntity>,
  users: List<UserEntity>,
  chatInputText: String,
  onChatInputChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  onSendVoiceNote: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Mensajes & Discusión", "Actividades en Tiempo Real")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
  ) {
    // Remote Team Presence row
    RemoteTeamPresenceBar(users = users)

    // Tabs: Chat vs Activity stream
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = ArborWhite,
      contentColor = ArborBlack,
      modifier = Modifier.fillMaxWidth()
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontSize = 12.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedTab == index) ArborBlack else ArborTextSecondary
            )
          }
        )
      }
    }

    if (selectedTab == 0) {
      // Chat view
      ChatView(
        messages = messages,
        chatInputText = chatInputText,
        onChatInputChange = onChatInputChange,
        onSendMessage = onSendMessage,
        onSendVoiceNote = onSendVoiceNote,
        modifier = Modifier.weight(1f)
      )
    } else {
      // Live Activity Stream
      ActivityStreamView(
        activities = activities,
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
fun RemoteTeamPresenceBar(users: List<UserEntity>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(ArborWhiteSubtle)
      .border(0.5.dp, ArborBorder)
      .padding(vertical = 12.dp, horizontal = 16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "EQUIPO REMOTO SINCRONIZADO",
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        color = ArborTextTertiary
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Box(
          modifier = Modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(ArborSuccess)
        )
        Text(
          text = "4 en línea",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = ArborForest
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(users) { user ->
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ArborWhite)
            .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(contentAlignment = Alignment.BottomEnd) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(ArborBlack),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.avatarInitials,
                color = ArborWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (user.status == "En línea") ArborSuccess else ArborTextTertiary)
                .border(1.dp, ArborWhite, CircleShape)
            )
          }

          Column {
            Text(
              text = user.name.split(" ").firstOrNull() ?: user.name,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ArborBlack
            )
            Text(
              text = user.timezone.split(" ").firstOrNull() ?: user.timezone,
              fontSize = 9.sp,
              color = ArborTextSecondary
            )
          }
        }
      }
    }
  }
}

@Composable
fun ChatView(
  messages: List<ChatMessageEntity>,
  chatInputText: String,
  onChatInputChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  onSendVoiceNote: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Canal de Discusión en Tiempo Real • Cifrado de Extremo a Extremo",
            fontSize = 10.sp,
            color = ArborTextTertiary
          )
        }
      }

      items(messages, key = { it.id }) { msg ->
        ChatMessageBubble(message = msg)
      }
    }

    // Input row
    ChatInputRow(
      text = chatInputText,
      onTextChange = onChatInputChange,
      onSend = onSendMessage,
      onVoiceNote = onSendVoiceNote
    )
  }
}

@Composable
fun ChatMessageBubble(message: ChatMessageEntity) {
  val isMine = message.isMine
  val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
  val timeString = timeFormat.format(Date(message.timestamp))

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
  ) {
    if (!isMine) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(ArborBlack),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = message.senderName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
          color = ArborWhite,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Column(
      horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
      modifier = Modifier.widthIn(max = 280.dp)
    ) {
      if (!isMine) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = message.senderName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = message.senderRole,
            fontSize = 9.sp,
            color = ArborTextSecondary
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
      }

      Box(
        modifier = Modifier
          .clip(
            RoundedCornerShape(
              topStart = 16.dp,
              topEnd = 16.dp,
              bottomStart = if (isMine) 16.dp else 4.dp,
              bottomEnd = if (isMine) 4.dp else 16.dp
            )
          )
          .background(if (isMine) ArborBlack else ArborWhiteSubtle)
          .border(
            1.dp,
            if (isMine) ArborBlack else ArborBorder,
            RoundedCornerShape(
              topStart = 16.dp,
              topEnd = 16.dp,
              bottomStart = if (isMine) 16.dp else 4.dp,
              bottomEnd = if (isMine) 4.dp else 16.dp
            )
          )
          .padding(12.dp)
      ) {
        if (message.isVoiceNote) {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.RecordVoiceOver,
                contentDescription = null,
                tint = if (isMine) ArborWhite else ArborForest,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "Mensaje de voz (${message.voiceDurationSec}s)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMine) ArborWhite else ArborBlack
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            VoiceWaveformPlayer(durationSec = message.voiceDurationSec)
          }
        } else {
          Text(
            text = message.message,
            fontSize = 13.sp,
            color = if (isMine) ArborWhite else ArborBlack
          )
        }
      }

      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = timeString,
        fontSize = 9.sp,
        color = ArborTextTertiary
      )
    }
  }
}

@Composable
fun ChatInputRow(
  text: String,
  onTextChange: (String) -> Unit,
  onSend: () -> Unit,
  onVoiceNote: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .border(0.5.dp, ArborBorder),
    color = ArborWhite,
    tonalElevation = 4.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Voice note quick trigger
      IconButton(
        onClick = onVoiceNote,
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(ArborWhiteSubtle)
          .border(1.dp, ArborBorder, CircleShape)
          .testTag("chat_voice_note_button")
      ) {
        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Grabar nota de voz",
          tint = ArborForest,
          modifier = Modifier.size(20.dp)
        )
      }

      OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text("Escribe un mensaje o menciona a @equipo...", fontSize = 12.sp, color = ArborTextTertiary) },
        modifier = Modifier
          .weight(1f)
          .height(50.dp)
          .testTag("chat_input_text"),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = ArborBlack,
          unfocusedBorderColor = ArborBorder,
          focusedContainerColor = ArborWhite,
          unfocusedContainerColor = ArborWhiteSubtle
        ),
        singleLine = true
      )

      IconButton(
        onClick = onSend,
        enabled = text.isNotBlank(),
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(if (text.isNotBlank()) ArborBlack else ArborWhiteSubtle)
          .testTag("chat_send_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = "Enviar mensaje",
          tint = if (text.isNotBlank()) ArborWhite else ArborTextTertiary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun ActivityStreamView(
  activities: List<ActivityLogEntity>,
  modifier: Modifier = Modifier
) {
  val timeFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Historial de Actividad en Vivo",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = "Registro de cambios y eventos remotos",
            fontSize = 11.sp,
            color = ArborTextSecondary
          )
        }
        Icon(
          imageVector = Icons.Default.History,
          contentDescription = null,
          tint = ArborBlack,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    items(activities, key = { it.id }) { log ->
      val (categoryColor, categoryLabel) = when (log.category) {
        "voice" -> Pair(ArborAlert, "Voz")
        "project" -> Pair(ArborForest, "Proyecto")
        "chat" -> Pair(ArborBlack, "Chat")
        else -> Pair(ArborForestLight, "Tarea")
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ArborBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ArborWhite)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(categoryColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = log.actorName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
              color = categoryColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = log.actorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ArborBlack
              )
              Text(
                text = timeFormat.format(Date(log.timestamp)),
                fontSize = 10.sp,
                color = ArborTextTertiary
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = "${log.actionText} ${log.targetItem}",
              fontSize = 12.sp,
              color = ArborTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ArborWhiteSubtle)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = categoryLabel,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = categoryColor
              )
            }
          }
        }
      }
    }
  }
}
