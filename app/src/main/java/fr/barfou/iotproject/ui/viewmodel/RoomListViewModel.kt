package fr.barfou.iotproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.barfou.iotproject.data.model.Lighting
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.data.repo.RoomRepository
import kotlinx.coroutines.launch

open class RoomListViewModel (
    private val repository: RoomRepository
): ViewModel() {

    fun retrieveData(onSuccess: onSuccess<List<Room>>) {
        viewModelScope.launch {
            repository.retrieveDataFromFirebase { res ->
                res?.run(onSuccess)
            }
        }
    }

    fun turnOffTheLight(roomId: String, onSuccess: onSuccess<Room>) {
        viewModelScope.launch {
            repository.turnOffTheLight(roomId)?.run(onSuccess)
        }
    }

    fun observePresenceChangesRoom202(onPresenceChanged: onSuccess<Room>) {
        viewModelScope.launch {
            repository.observeChangeOnRoom202 {
                onPresenceChanged(it)
            }
        }
    }

    /*fun observeAlightChangesRoom202(onAlightChange: onSuccess<MutableList<Lighting>>) {
        viewModelScope.launch {
            repository.observeChangeOnNestedDataRoom202 {
                onAlightChange(it)
            }
        }
    }*/

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RoomListViewModel(
                RoomRepository.instance
            ) as T
        }
    }
}