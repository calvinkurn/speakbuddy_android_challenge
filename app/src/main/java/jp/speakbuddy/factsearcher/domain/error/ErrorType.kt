package jp.speakbuddy.factsearcher.domain.error

enum class ErrorType(val code: String) {
    NoConnection("1"),
    ServiceNotFound("404"),
    ServiceError("500"),
    UnknownError("-1")
}