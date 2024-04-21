package com.ifs21044.lostfoundapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ifs21044.lostfoundapp.data.pref.UserModel
import com.ifs21044.lostfoundapp.data.remote.MyResult
import com.ifs21044.lostfoundapp.data.remote.response.DelcomLostFoundsResponse
import com.ifs21044.lostfoundapp.data.repository.AuthRepository
import com.ifs21044.lostfoundapp.data.repository.LostFoundRepository
import com.ifs21044.lostfoundapp.presentation.ViewModelFactory
import com.ifs21044.lostfoundappo.data.remote.response.DelcomResponse
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

    fun getLostFound(): LiveData<MyResult<DelcomLostFoundsResponse>> {
        return lostfoundRepository.getLostFounds(null, 1, null).asLiveData()
    }

    fun getLostFounds(): LiveData<MyResult<DelcomLostFoundsResponse>> {
        return lostfoundRepository.getLostFounds(null,0,null).asLiveData()
    }

    fun putLostFound(
        lostandfoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return lostfoundRepository.putLostFound(
            lostandfoundId,
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
            lostfoundRepository: LostFoundRepository
        ): MainViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = MainViewModel(
                    authRepository,
                    lostfoundRepository
                )
            }
            return INSTANCE as MainViewModel
        }
    }
}