package com.blinkslabs.blinkist.android.challenge.data.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


@Database(entities = [Book::class], version = 1)
@TypeConverters(CustomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object CustomConverters {

    @JvmStatic
    @TypeConverter
    fun convertDateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return formatter.format(date)
    }

    @JvmStatic
    @TypeConverter
    fun convertStringToDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}


