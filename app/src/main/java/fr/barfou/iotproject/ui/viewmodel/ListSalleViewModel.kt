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

    fun getAllSalles(etablissement_id: Int, onSuccess: onSuccess<Map<String, Salle>>) {
        viewModelScope.launch {
            repository.retrieveSallesFromFirebase(etablissement_id)?.run(onSuccess)
        }
    }

    fun initFirebase(onSuccess: onSuccess<Boolean>) {
        viewModelScope.launch {
            repository.initFirebase().run(onSuccess)
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