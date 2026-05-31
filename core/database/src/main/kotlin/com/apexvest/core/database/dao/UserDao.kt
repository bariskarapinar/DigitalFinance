package com.apexvest.core.database.dao

import androidx.room.*
import com.apexvest.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET balance = :newBalance WHERE id = :id")
    suspend fun updateBalance(id: String, newBalance: Double)
}
