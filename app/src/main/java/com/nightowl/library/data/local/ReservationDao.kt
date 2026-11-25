package com.nightowl.library.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservations ORDER BY startHour ASC, zoneName ASC")
    fun observeAll(): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE id = :id")
    suspend fun getById(id: Int): ReservationEntity?

    @Query(
        "SELECT COUNT(*) FROM reservations " +
                "WHERE zoneId = :zoneId AND seat = :seat AND id != :excludeId " +
                "AND :startHour < endHour AND :endHour > startHour"
    )
    suspend fun countOverlapping(
        zoneId: Int,
        seat: String,
        startHour: Int,
        endHour: Int,
        excludeId: Int
    ): Int

    @Insert
    suspend fun insert(reservation: ReservationEntity): Long

    @Update
    suspend fun update(reservation: ReservationEntity)

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun deleteById(id: Int)
}