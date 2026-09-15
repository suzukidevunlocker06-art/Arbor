package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VideoLibrary

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.ArborAppHeader
import com.example.ui.screens.CollaborationScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TeamPermissionsScreen
import com.example.ui.screens.YouTubeShowcaseScreen
import com.example.ui.theme.ArborBlack
import com.example.ui.theme.ArborBorder
import com.example.ui.theme.ArborForest
import com.example.ui.theme.ArborSage
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle
import com.example.viewmodel.ArborViewModel
import com.example.viewmodel.NavigationTab

@Composable
fun MainAppLayout(
  viewModel: ArborViewModel,
  modifier: Modifier = Modifier
) {
  val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
  val projects by viewModel.projects.collectAsStateWithLifecycle()
  val filteredTasks by viewModel.filteredTasks.collectAsStateWithLifecycle()
  val messages by viewModel.messages.collectAsStateWithLifecycle()
  val activityLogs by viewModel.activityLogs.collectAsStateWithLifecycle()
  val notifications by viewModel.notifications.collectAsStateWithLifecycle()
  val users by viewModel.users.collectAsStateWithLifecycle()
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedProjectFilter by viewModel.selectedProjectFilter.collectAsStateWithLifecycle()
  val selectedTaskStatusFilter by viewModel.selectedTaskStatusFilter.collectAsStateWithLifecycle()

  val isRecordingVoice by viewModel.isRecordingVoice.collectAsStateWithLifecycle()
  val voiceSeconds by viewModel.voiceSeconds.collectAsStateWithLifecycle()
  val chatInputText by viewModel.chatInputText.collectAsStateWithLifecycle()

  val showCreateTaskDialog by viewModel.showCreateTaskDialog.collectAsStateWithLifecycle()
  val showCreateProjectDialog by viewModel.showCreateProjectDialog.collectAsStateWithLifecycle()

  val unreadCount = notifications.count { !it.isRead }

  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .statusBarsPadding()
  ) {
    val isExpandedScreen = maxWidth >= 680.dp // Adaptive tablet / desktop screen check

    if (isExpandedScreen) {
      // DESKTOP / TABLET EXPANDED LAYOUT WITH NAVIGATION RAIL
      Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
          modifier = Modifier
            .fillMaxHeight()
            .width(84.dp)
            .border(1.dp, ArborBorder),
          containerColor = ArborWhite,
          contentColor = ArborBlack,
          header = {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
                  .background(ArborWhite),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_arbor_logo),
                  contentDescription = "Logo Arbor",
                  modifier = Modifier.size(36.dp)
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "ARBOR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = ArborBlack
              )
            }
          }
        ) {
          Spacer(modifier = Modifier.weight(1f))

          NavigationTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationRailItem(
              selected = isSelected,
              onClick = { viewModel.selectTab(tab) },
              icon = {
                if (tab == NavigationTab.NOTIFICATIONS) {
                  BadgedBox(
                    badge = {
                      if (unreadCount > 0) {
                        Badge(containerColor = ArborBlack, contentColor = ArborWhite) {
                          Text("$unreadCount", fontSize = 9.sp)
                        }
                      }
                    }
                  ) {
                    Icon(
                      imageVector = getTabIcon(tab, isSelected),
                      contentDescription = tab.label
                    )
                  }
                } else {
                  Icon(
                    imageVector = getTabIcon(tab, isSelected),
                    contentDescription = tab.label
                  )
                }
              },
              label = {
                Text(
                  text = tab.label.split(" ").firstOrNull() ?: tab.label,
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = NavigationRailItemDefaults.colors(
                selectedIconColor = ArborWhite,
                selectedTextColor = ArborBlack,
                indicatorColor = ArborBlack,
                unselectedIconColor = ArborTextSecondary,
                unselectedTextColor = ArborTextSecondary
              ),
              modifier = Modifier.testTag("nav_rail_${tab.name}")
            )
          }

          Spacer(modifier = Modifier.weight(1f))
        }

        // Expanded content area
        Column(modifier = Modifier.fillMaxSize()) {
          ArborAppHeader(
            currentUserInitials = currentUser?.avatarInitials ?: "SN",
            currentUserName = currentUser?.name ?: "Sofia Navarro",
            unreadNotificationsCount = unreadCount,
            onNotificationClick = { viewModel.selectTab(NavigationTab.NOTIFICATIONS) },
            onProfileClick = { viewModel.selectTab(NavigationTab.TEAM_PERMISSIONS) }
          )

          ScreenContent(
            selectedTab = selectedTab,
            viewModel = viewModel,
            projects = projects,
            filteredTasks = filteredTasks,
            messages = messages,
            activityLogs = activityLogs,
            notifications = notifications,
            users = users,
            currentUser = currentUser,
            searchQuery = searchQuery,
            selectedProjectFilter = selectedProjectFilter,
            selectedTaskStatusFilter = selectedTaskStatusFilter,
            isRecordingVoice = isRecordingVoice,
            voiceSeconds = voiceSeconds,
            chatInputText = chatInputText,
            showCreateTaskDialog = showCreateTaskDialog,
            showCreateProjectDialog = showCreateProjectDialog
          )
        }
      }
    } else {
      // MOBILE COMPACT LAYOUT WITH BOTTOM NAVIGATION BAR
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ArborWhite,
        topBar = {
          ArborAppHeader(
            currentUserInitials = currentUser?.avatarInitials ?: "SN",
            currentUserName = currentUser?.name ?: "Sofia Navarro",
            unreadNotificationsCount = unreadCount,
            onNotificationClick = { viewModel.selectTab(NavigationTab.NOTIFICATIONS) },
            onProfileClick = { viewModel.selectTab(NavigationTab.TEAM_PERMISSIONS) }
          )
        },
        bottomBar = {
          NavigationBar(
            modifier = Modifier
              .fillMaxWidth()
              .border(0.5.dp, ArborBorder)
              .navigationBarsPadding(),
            containerColor = ArborWhite,
            contentColor = ArborBlack,
            tonalElevation = 6.dp
          ) {
            val mobileTabs = listOf(
              NavigationTab.PROJECTS,
              NavigationTab.TASKS,
              NavigationTab.SHOWCASE,
              NavigationTab.COLLABORATION,
              NavigationTab.TEAM_PERMISSIONS
            )
            mobileTabs.forEach { tab ->
              val isSelected = selectedTab == tab
              NavigationBarItem(
                selected = isSelected,
                onClick = { viewModel.selectTab(tab) },
                icon = {
                  if (tab == NavigationTab.NOTIFICATIONS) {
                    BadgedBox(
                      badge = {
                        if (unreadCount > 0) {
                          Badge(containerColor = ArborBlack, contentColor = ArborWhite) {
                            Text("$unreadCount", fontSize = 9.sp)
                          }
                        }
                      }
                    ) {
                      Icon(
                        imageVector = getTabIcon(tab, isSelected),
                        contentDescription = tab.label
                      )
                    }
                  } else {
                    Icon(
                      imageVector = getTabIcon(tab, isSelected),
                      contentDescription = tab.label
                    )
                  }
                },
                label = {
                  Text(
                    text = when (tab) {
                      NavigationTab.PROJECTS -> "Proyectos"
                      NavigationTab.TASKS -> "Tareas"
                      NavigationTab.SHOWCASE -> "YouTube"
                      NavigationTab.COLLABORATION -> "Discusión"
                      NavigationTab.TEAM_PERMISSIONS -> "Equipo"
                      NavigationTab.NOTIFICATIONS -> "Alertas"
                    },
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = ArborWhite,
                  selectedTextColor = ArborBlack,
                  indicatorColor = ArborBlack,
                  unselectedIconColor = ArborTextSecondary,
                  unselectedTextColor = ArborTextSecondary
                ),
                modifier = Modifier.testTag("nav_bottom_${tab.name}")
              )
            }
          }
        }
      ) { innerPadding ->
        ScreenContent(
          selectedTab = selectedTab,
          viewModel = viewModel,
          projects = projects,
          filteredTasks = filteredTasks,
          messages = messages,
          activityLogs = activityLogs,
          notifications = notifications,
          users = users,
          currentUser = currentUser,
          searchQuery = searchQuery,
          selectedProjectFilter = selectedProjectFilter,
          selectedTaskStatusFilter = selectedTaskStatusFilter,
          isRecordingVoice = isRecordingVoice,
          voiceSeconds = voiceSeconds,
          chatInputText = chatInputText,
          showCreateTaskDialog = showCreateTaskDialog,
          showCreateProjectDialog = showCreateProjectDialog,
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}

@Composable
fun ScreenContent(
  selectedTab: NavigationTab,
  viewModel: ArborViewModel,
  projects: List<com.example.data.model.ProjectEntity>,
  filteredTasks: List<com.example.data.model.TaskEntity>,
  messages: List<com.example.data.model.ChatMessageEntity>,
  activityLogs: List<com.example.data.model.ActivityLogEntity>,
  notifications: List<com.example.data.model.NotificationEntity>,
  users: List<com.example.data.model.UserEntity>,
  currentUser: com.example.data.model.UserEntity?,
  searchQuery: String,
  selectedProjectFilter: String?,
  selectedTaskStatusFilter: String,
  isRecordingVoice: Boolean,
  voiceSeconds: Int,
  chatInputText: String,
  showCreateTaskDialog: Boolean,
  showCreateProjectDialog: Boolean,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    when (selectedTab) {
      NavigationTab.PROJECTS -> {
        ProjectsScreen(
          projects = projects,
          searchQuery = searchQuery,
          onSearchQueryChange = { viewModel.setSearchQuery(it) },
          onSelectProjectForTasks = { projId ->
            viewModel.selectProjectFilter(projId)
            viewModel.selectTab(NavigationTab.TASKS)
          },
          onOpenCreateProject = { viewModel.setShowCreateProjectDialog(true) },
          showCreateDialog = showCreateProjectDialog,
          onDismissCreateDialog = { viewModel.setShowCreateProjectDialog(false) },
          onCreateProjectConfirm = { title, desc, category ->
            viewModel.createProject(title, desc, category)
          }
        )
      }

      NavigationTab.TASKS -> {
        TasksScreen(
          tasks = filteredTasks,
          projects = projects,
          selectedProjectFilter = selectedProjectFilter,
          onSelectProjectFilter = { viewModel.selectProjectFilter(it) },
          selectedStatusFilter = selectedTaskStatusFilter,
          onSelectStatusFilter = { viewModel.setTaskStatusFilter(it) },
          searchQuery = searchQuery,
          onSearchQueryChange = { viewModel.setSearchQuery(it) },
          isRecordingVoice = isRecordingVoice,
          recordingSeconds = voiceSeconds,
          onStartVoiceRecord = { viewModel.startVoiceRecording() },
          onStopVoiceRecord = { viewModel.stopVoiceRecording() },
          onSaveVoiceTask = { viewModel.createVoiceTask() },
          onUpdateTaskStatus = { id, title, nextStatus, projId ->
            viewModel.updateTaskStatus(id, title, nextStatus, projId)
          },
          onDeleteTask = { viewModel.deleteTask(it) },
          onOpenCreateTask = { viewModel.setShowCreateTaskDialog(true) },
          showCreateDialog = showCreateTaskDialog,
          onDismissCreateDialog = { viewModel.setShowCreateTaskDialog(false) },
          onCreateManualTaskConfirm = { projId, title, desc, prio, date, tag, assignee ->
            viewModel.createManualTask(projId, title, desc, prio, date, tag, assignee)
          },
          onUpdateTaskDetails = { viewModel.updateTaskDetails(it) }
        )
      }

      NavigationTab.SHOWCASE -> {
        YouTubeShowcaseScreen(
          onNavigateToTab = { viewModel.selectTab(it) }
        )
      }

      NavigationTab.COLLABORATION -> {
        CollaborationScreen(
          messages = messages,
          activities = activityLogs,
          users = users,
          chatInputText = chatInputText,
          onChatInputChange = { viewModel.setChatInputText(it) },
          onSendMessage = { viewModel.sendChatMessage() },
          onSendVoiceNote = { viewModel.sendVoiceMessage() }
        )
      }

      NavigationTab.TEAM_PERMISSIONS -> {
        TeamPermissionsScreen(
          users = users,
          currentUser = currentUser,
          onSwitchUser = { viewModel.switchUser(it) },
          onAudioPermissionGranted = { viewModel.setAudioPermissionGranted(it) },
          onNotificationPermissionGranted = { viewModel.setNotificationPermissionGranted(it) }
        )
      }

      NavigationTab.NOTIFICATIONS -> {
        NotificationsScreen(
          notifications = notifications,
          onMarkAsRead = { viewModel.markNotificationAsRead(it) },
          onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() }
        )
      }
    }
  }
}

fun getTabIcon(tab: NavigationTab, isSelected: Boolean): ImageVector {
  return when (tab) {
    NavigationTab.PROJECTS -> if (isSelected) Icons.Filled.FolderOpen else Icons.Outlined.FolderOpen
    NavigationTab.TASKS -> if (isSelected) Icons.Filled.Assignment else Icons.Outlined.Assignment
    NavigationTab.SHOWCASE -> if (isSelected) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary
    NavigationTab.COLLABORATION -> if (isSelected) Icons.Filled.ChatBubbleOutline else Icons.Outlined.ChatBubbleOutline
    NavigationTab.TEAM_PERMISSIONS -> if (isSelected) Icons.Filled.Security else Icons.Outlined.Security
    NavigationTab.NOTIFICATIONS -> if (isSelected) Icons.Filled.Notifications else Icons.Outlined.Notifications
  }
}
