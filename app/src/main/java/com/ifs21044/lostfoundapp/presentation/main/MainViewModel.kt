package com.ifs21044.lostfoundapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ifs21044.lostfoundapp.data.pref.UserModel
import com.ifs21044.lostfoundapp.data.remote.MyResult
import com.ifs21044.lostfoundapp.data.remote.response.DelcomLostFoundsResponse
import com.ifs21044.lostfoundapp.data.remote.response.DelcomResponse
import com.ifs21044.lostfoundapp.data.repository.AuthRepository
import com.ifs21044.lostfoundapp.data.repository.LostFoundRepository
import com.ifs21044.lostfoundapp.presentation.ViewModelFactory
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val lostfoundRepository: LostFoundRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getLostFounds(filter: String = ""): LiveData<MyResult<DelcomLostFoundsResponse>> {
        if (filter.isNotEmpty()) {
        }
        return lostfoundRepository.getLostFounds(null, 0, null).asLiveData()

    }

    fun getLostFound(): LiveData<MyResult<DelcomLostFoundsResponse>> {
        return lostfoundRepository.getLostFounds(null, 1, null).asLiveData()
    }

    fun putLostFound(
        lostfoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return lostfoundRepository.putLostFound(
            lostfoundId,
            title,
            description,
            status,
            isCompleted,
        ).asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModel? = null

        fun getInstance(
            authRepository: AuthRepository,
            todoRepository: LostFoundRepository
        ): MainViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = MainViewModel(
                    authRepository,
                    todoRepository
                )
            }
            return INSTANCE as MainViewModel
        }
    }
}

