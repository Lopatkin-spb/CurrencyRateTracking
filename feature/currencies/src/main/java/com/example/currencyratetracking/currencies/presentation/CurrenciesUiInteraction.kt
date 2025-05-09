package com.example.currencyratetracking.currencies.presentation

import com.example.currencyratetracking.core.presentation.UiEvent
import com.example.currencyratetracking.model.CurrencyUi
import com.example.currencyratetracking.model.Sorting


sealed class CurrenciesUiState {
    open val showedBaseCurrency: String = ""
    open val listBaseCurrencies: List<String> = emptyList()
    open val isSortingApply: Boolean = false
    open val sorting: Sorting = Sorting.CodeAZ
    open val listActualCurrencyRates: List<ActualCurrencyRateUi> = emptyList()
    open val isFiltersLifecycle: Boolean? = null


    data object Empty : CurrenciesUiState()
    data class Loading(
        override val showedBaseCurrency: String = "",
        override val listBaseCurrencies: List<String> = emptyList(),
        override val isSortingApply: Boolean = false,
        override val sorting: Sorting = Sorting.CodeAZ,
    ) : CurrenciesUiState()

    data class Error(val text: String) : CurrenciesUiState()
    data class Success(
        override val showedBaseCurrency: String = "",
        override val listBaseCurrencies: List<String> = emptyList(),
        override val listActualCurrencyRates: List<ActualCurrencyRateUi> = emptyList(),
        override val isFiltersLifecycle: Boolean? = null,
        override val sorting: Sorting = Sorting.CodeAZ,
        override val isSortingApply: Boolean = false,
    ) : CurrenciesUiState()


    fun CurrenciesUiState.toSuccess(): Success {
        return Success(
            listActualCurrencyRates = this.listActualCurrencyRates,
            showedBaseCurrency = this.showedBaseCurrency,
            listBaseCurrencies = this.listBaseCurrencies,
            isSortingApply = this.isSortingApply,
            sorting = this.sorting,
            isFiltersLifecycle = this.isFiltersLifecycle,
        )
    }

    fun CurrenciesUiState.toLoading(): Loading {
        return Loading(
            showedBaseCurrency = this.showedBaseCurrency,
            listBaseCurrencies = this.listBaseCurrencies,
            isSortingApply = this.isSortingApply,
            sorting = this.sorting,
        )
    }

}


data class ActualCurrencyRateUi(
    override val id: Long,
    override val text: String,
    override val quotation: String,
    override val isFavorite: Boolean,
) : CurrencyUi(id = id, text = text, quotation = quotation, isFavorite = isFavorite)


sealed interface CurrenciesUserEvent : UiEvent {
    data object OnScreenOpen : CurrenciesUserEvent
    data object OnScreenClose : CurrenciesUserEvent
    data class OnChangeBaseCurrency(val name: String) : CurrenciesUserEvent
    data class OnChangeFavoriteState(val currency: CurrencyUi) : CurrenciesUserEvent
    data object OnOpenFilters : CurrenciesUserEvent
    data object OnCloseFilters : CurrenciesUserEvent
    data object OnResetFiltersState : CurrenciesUserEvent
    data object OnApplyFilters : CurrenciesUserEvent
    data class OnSortingSelect(val select: Sorting) : CurrenciesUserEvent
}