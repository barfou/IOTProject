package fr.barfou.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.barfou.data.model.Salle
import fr.barfou.data.repo.SalleRepository
import kotlinx.coroutines.launch

open class ListSalleViewModel (
    private val repository: SalleRepository
): ViewModel() {

    fun getAllSalles(etablissement_id: Int, onSuccess: onSuccess<List<Salle>>) {
        viewModelScope.launch {
            repository.getAllSalles(etablissement_id).run(onSuccess)
        }
    }

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListSalleViewModel(SalleRepository.instance) as T
        }
    }
}