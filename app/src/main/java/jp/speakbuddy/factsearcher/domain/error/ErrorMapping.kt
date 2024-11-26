package jp.speakbuddy.factsearcher.domain.error

fun getErrorMessage(errorType: ErrorType): String {
    return when(errorType){
        ErrorType.NoConnection -> NO_CONNECTION_ERROR_MSG
        ErrorType.ServiceError -> SERVICE_UNAVAILABLE_MSG
        ErrorType.ServiceNotFound -> SERVICE_NOT_FOUND_MSG
        else -> String.format(GENERAL_ERROR_MSG, errorType.code)
    }
}