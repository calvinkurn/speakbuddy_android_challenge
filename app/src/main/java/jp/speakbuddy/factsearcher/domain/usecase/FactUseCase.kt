package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.domain.error.ErrorType
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.domain.usecase.utils.FactResult
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class FactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun getRandomCatFact(): FactResult<FactUiModel> {
        return try {
            factRepository.getRandomFact().let {
                FactResult.Success(
                    FactUiModel(
                        factId = System.currentTimeMillis(),
                        fact = it.fact,
                        length = it.length,
                        isContainsCat = it.fact.lowercase(Locale.US).contains("cats")
                    )
                )
            }
        } catch (e: HttpException) {
            val error = when(e.code()) {
                500 -> ErrorType.ServiceError
                404 -> ErrorType.ServiceNotFound
                else -> ErrorType.UnknownError
            }
            FactResult.Failure(error)
        } catch (e: IOException) {
            FactResult.Failure(ErrorType.NoConnection)
        } catch (e: Exception) {
            // we can add logger here to track unspecific error
            FactResult.Failure(ErrorType.UnknownError)
        }
    }
}