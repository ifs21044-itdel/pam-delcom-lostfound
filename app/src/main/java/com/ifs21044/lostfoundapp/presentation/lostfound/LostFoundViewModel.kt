package com.ifs21044.lostfoundapp.presentation.lostfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21044.lostfoundapp.data.local.entity.DelcomLostFoundEntity
import com.ifs21044.lostfoundapp.data.remote.MyResult
import com.ifs21044.lostfoundapp.data.remote.response.DataAddLostFoundResponse
import com.ifs21044.lostfoundapp.data.remote.response.DelcomLostFoundResponse
import com.ifs21044.lostfoundapp.data.remote.response.DelcomResponse
import com.ifs21044.lostfoundapp.data.repository.LocalLostFoundRepository
import com.ifs21044.lostfoundapp.data.repository.LostFoundRepository
import com.ifs21044.lostfoundapp.presentation.ViewModelFactory
import okhttp3.MultipartBody

class LostFoundViewModel(
    private val lostFoundRepository: LostFoundRepository,
    private val localLostFoundRepository: LocalLostFoundRepository
) : ViewModel() {

    fun getLostFound(lostfoundId: Int): LiveData<MyResult<DelcomLostFoundResponse>> {
        return lostFoundRepository.getLostFound(lostfoundId).asLiveData()
    }

    fun postLostFound(
        title: String,
        description: String,
        status : String,
    ): LiveData<MyResult<DataAddLostFoundResponse>> {
        return lostFoundRepository.postLostFound(
            title,
            description,
            status
        ).asLiveData()
    }

    fun putLostFound(
        lostfoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return lostFoundRepository.putLostFound(
            lostfoundId,
            title,
            description,
            status,
            isCompleted,
        ).asLiveData()
    }

    fun deleteLostFound(todoId: Int): LiveData<MyResult<DelcomResponse>> {
        return lostFoundRepository.deleteLostFound(todoId).asLiveData()
    }

    fun getLocalLostFounds(): LiveData<List<DelcomLostFoundEntity>?> {
        return localLostFoundRepository.getAllLostFounds()
    }

    fun getLocalLostFound(lostfoundId: Int): LiveData<DelcomLostFoundEntity?> {
        return localLostFoundRepository.get(lostfoundId)
    }
    fun insertLocalLostFound(lostfound: DelcomLostFoundEntity) {
        localLostFoundRepository.insert(lostfound)
    }
    fun deleteLocalLostFound(lostfound: DelcomLostFoundEntity) {
        localLostFoundRepository.delete(lostfound)
    }

    fun addCoverLostFound(
        todoId: Int,
        cover: MultipartBody.Part,
    ): LiveData<MyResult<DelcomResponse>> {
        return lostFoundRepository.addCoverLostFound(todoId, cover).asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: LostFoundViewModel? = null

        fun getInstance(
            lostfoundRepository: LostFoundRepository,
            localLostFoundRepository: LocalLostFoundRepository,
        ): LostFoundViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = LostFoundViewModel(
                    lostfoundRepository,
                    localLostFoundRepository
                )
            }
            return INSTANCE as LostFoundViewModel
        }
    }
}
