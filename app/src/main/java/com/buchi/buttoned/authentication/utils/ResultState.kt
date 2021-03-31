package com.buchi.buttoned.authentication.utils

data class ResultState<T>(
    var message: Event<String>? = null,
    var error: Event<Throwable>? = null,
    var loading: Boolean = false,
    var data: Event<T>? = null
) {
    companion object {
        fun <T> error(
            message: String,
            error: Throwable
        ): ResultState<T> {
            return ResultState(
                message = Event(message),
                error = Event(error),
                loading = false,
                data = null
            )
        }

        fun <T> loading(
            isLoading: Boolean
        ): ResultState<T> {
            return ResultState(
                message = null,
                error = null,
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(
            message: String? = null,
            data: T? = null
        ): ResultState<T> {
            return ResultState(
                message = Event.messageEvent(message),
                error = null,
                loading = false,
                data = Event.dataEvent(data)
            )
        }
    }

    override fun toString(): String {
        return "com.ajocard.core.utils.DataState(loading=$loading,data=$data)"
    }
}
