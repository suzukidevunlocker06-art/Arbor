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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.ui.theme.ArborAlert
import com.example.ui.theme.ArborBlack
import com.example.ui.theme.ArborBorder
import com.example.ui.theme.ArborForest
import com.example.ui.theme.ArborForestLight
import com.example.ui.theme.ArborSage
import com.example.ui.theme.ArborSuccess
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle
import com.example.ui.theme.ArborWhiteSurface
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
  notifications: List<NotificationEntity>,
  onMarkAsRead: (String) -> Unit,
  onMarkAllAsRead: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf("Todas") }
  val filters = listOf("Todas", "No Leídas", "Tareas", "Menciones")

  val filteredNotifications = notifications.filter { notif ->
    when (selectedFilter) {
      "No Leídas" -> !notif.isRead
      "Tareas" -> notif.type == "task"
      "Menciones" -> notif.type == "mention"
      else -> true
    }
  }

  val unreadCount = notifications.count { !it.isRead }
  val timeFormat = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
  ) {
    // Header & mark all as read action
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Bandeja de Notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = "$unreadCount pendientes de revisión",
            fontSize = 12.sp,
            color = ArborTextSecondary
          )
        }

        if (unreadCount > 0) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ArborWhiteSubtle)
              .border(1.dp, ArborBorder, RoundedCornerShape(8.dp))
              .clickable { onMarkAllAsRead() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("mark_all_read_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                tint = ArborBlack,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "Marcar leídas",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArborBlack
              )
            }
          }
        }
      }
    }

    // Filter chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filters) { filter ->
          val isSelected = filter == selectedFilter
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(18.dp))
              .background(if (isSelected) ArborBlack else ArborWhiteSubtle)
              .border(1.dp, if (isSelected) ArborBlack else ArborBorder, RoundedCornerShape(18.dp))
              .clickable { selectedFilter = filter }
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = filter,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) ArborWhite else ArborTextSecondary
            )
          }
        }
      }
    }

    if (filteredNotifications.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ArborWhiteSubtle)
            .border(1.dp, ArborBorder, RoundedCornerShape(16.dp))
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsNone,
              contentDescription = null,
              tint = ArborTextTertiary,
              modifier = Modifier.size(36.dp)
            )
            Text(
              text = "Bandeja al día",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = ArborBlack
            )
            Text(
              text = "No tienes notificaciones pendientes en esta categoría.",
              fontSize = 12.sp,
              color = ArborTextSecondary
            )
          }
        }
      }
    } else {
      items(filteredNotifications, key = { it.id }) { notif ->
        val icon = when (notif.type) {
          "mention" -> Icons.Default.AlternateEmail
          "task" -> Icons.Default.Assignment
          "deadline" -> Icons.Default.Timer
          else -> Icons.Default.Groups
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (notif.isRead) ArborBorder else ArborBlack, RoundedCornerShape(14.dp))
            .clickable { onMarkAsRead(notif.id) },
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (notif.isRead) ArborWhite else ArborWhiteSurface
          )
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
                .background(if (notif.isRead) ArborWhiteSubtle else ArborSage),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (notif.isRead) ArborTextSecondary else ArborForest,
                modifier = Modifier.size(18.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = notif.title,
                  fontWeight = if (notif.isRead) FontWeight.SemiBold else FontWeight.Bold,
                  fontSize = 13.sp,
                  color = ArborBlack
                )

                if (!notif.isRead) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(ArborForest)
                  )
                }
              }

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = notif.body,
                fontSize = 12.sp,
                color = ArborTextSecondary
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = timeFormat.format(Date(notif.timestamp)),
                fontSize = 10.sp,
                color = ArborTextTertiary
              )
            }
          }
        }
      }
    }
  }
}
