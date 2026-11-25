package com.nightowl.library.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedBookDao {

    @Query("SELECT * FROM saved_books ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<SavedBookEntity>>

    @Query("SELECT workKey FROM saved_books")
    fun observeSavedKeys(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: SavedBookEntity): Long

    @Update
    suspend fun update(book: SavedBookEntity)

    @Delete
    suspend fun delete(book: SavedBookEntity)
}