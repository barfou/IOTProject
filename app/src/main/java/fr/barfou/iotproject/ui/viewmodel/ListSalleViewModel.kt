package fr.barfou.iotproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.barfou.iotproject.data.model.Salle
import fr.barfou.iotproject.data.repo.SalleRepository
import kotlinx.coroutines.launch

open class ListSalleViewModel (
    private val repository: SalleRepository
): ViewModel() {

    fun retrieveData(onSuccess: onSuccess<List<Salle>>) {
        viewModelScope.launch {
            repository.retrieveDataFromFirebase { res ->
                res?.run(onSuccess)
            }
        }
    }

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListSalleViewModel(
                SalleRepository.instance
            ) as T
        }
    }
}