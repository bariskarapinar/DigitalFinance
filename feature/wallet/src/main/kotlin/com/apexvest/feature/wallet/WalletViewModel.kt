package com.apexvest.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexvest.core.database.dao.UserDao
import com.apexvest.core.database.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    val user: StateFlow<UserEntity?> = userDao.getUser("me")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // Initialize dummy user if not exists
        viewModelScope.launch {
            userDao.insertUser(UserEntity("me", "Baris Karapinar", "baris@example.com", 1250000.0))
        }
    }

    fun transfer(amount: Double) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            userDao.updateBalance(currentUser.id, currentUser.balance - amount)
        }
    }
}
