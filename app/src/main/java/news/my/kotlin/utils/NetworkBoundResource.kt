package news.my.kotlin.utils

import kotlinx.coroutines.flow.*
import retrofit2.HttpException


/**
 * GENERIC TYPES
 * This is a generic function and that means it can work with any type of data,
 * @param ResultType is the data type loaded the local cache. Can be any thing, a list or any object.
 * @param RequestType is the data type loaded from the network. Can be any thing, a list or any object.
 * @see networkBoundResource
 *
 * ARGUMENT PARAMETERS
 * This function takes in four argument parameters which are functions
 *
 * NOTE!!! -> all the paramEters are function implementations of the following pieces of logic
 *
 * @param query
 * @return Flow<ResultType>
 * pass in a function that loads data from your local cache and returns a flow of your specified data type <ResultType>
 *
 * @param fetch
 * @return <RequestType>
 * pass in a function, a suspend function, that loads data from your rest api and returns an object of <RequestType>
 *
 * @param saveFetchResult
 * @return Unit
 * pass in a function that just takes in <RequestType> (The data type got from the network) and saves it in the local cache.
 *
 * @param
 * @return Boolean
 * pass in a function that has the logic to whether the algorithm should make a networking call or not.
 * In this case, this function takes in data loaded from @param query and determines whether to make a networking call or not.
 * This can vary with your implementation however, say fetch depending on the last time you made a networking call....e.t.c.
 *
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    //First step, fetch data from the local cache
    val data = query().first()

    //If shouldFetch returns true,
    val resource = if (shouldFetch(data)) {

        //Dispatch a message to the UI that you're doing some background work
        emit(ApiResult.Loading(true))

        try {

            //make a networking call
            val resultType = fetch()

            //save it to the database
            saveFetchResult(resultType)

            //Now fetch data again from the database and Dispatch it to the UI
            query().map { ApiResult.Success(it) }

        } catch (throwable: Throwable) {

            when(throwable){
                is HttpException-> {
                    val message = throwable.response()?.errorBody()?.string()
                    val errorMessage = message ?: "Something went wrong"
                    query().map { ApiResult.Error(errorMessage, it) }
                }
                else -> {
                    val errorMessage = throwable.message ?: "Something went wrong"
                    query().map { ApiResult.Error(errorMessage, it) }
                }
            }

            //Dispatch any error emitted to the UI, plus data emmited from the Database


        }

        //If should fetch returned false
    } else {
        //Make a query to the database and Dispatch it to the UI.
        query().map { ApiResult.Success(it) }
    }

    //Emit the resource variable
    emitAll(resource)
}