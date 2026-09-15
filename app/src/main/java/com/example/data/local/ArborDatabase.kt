package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity

@Database(
  entities = [
    ProjectEntity::class,
    TaskEntity::class,
    ChatMessageEntity::class,
    ActivityLogEntity::class,
    NotificationEntity::class,
    UserEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class ArborDatabase : RoomDatabase() {
  abstract fun arborDao(): ArborDao

  companion object {
    @Volatile
    private var INSTANCE: ArborDatabase? = null

    fun getDatabase(context: Context): ArborDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          ArborDatabase::class.java,
          "arbor_workspace.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
