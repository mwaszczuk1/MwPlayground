package pl.mwaszczuk.mwplayground.ui.main.flows

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pl.mwaszczuk.mwplayground.data.network.dto.WeatherDto
import pl.mwaszczuk.mwplayground.ui.main.WeatherUseCase

class FlowsViewModel(
    private val useCase: WeatherUseCase
) : ViewModel() {

    val citiesList = listOf(
        "Warsaw", "Bialystok", "Cracow", "Gdansk", "Poznan"
    )

    // Number one rule: FLOWS WILL NOT START WHEN NOTHING COLLECTS THEM! and FLOWS ARE KINDA LIVEDATAS ON STEROIDS
    // Flow can be collected only from one place. If I want to collect a Flow in many places I Should use a StateFlow.
    // Collecting flows dont stop when view goes to a stopped state so I need to cancel a job that is collecting a flow in onStop

    // 1. One time operation flow
    // Creates a flow that I can collect from later e.g. in a Fragment
    // The inside is a coroutine so I can do suspended calls here also.
    // The Flow class cannot be updated from outside - it can only be collected from
    private val weather = flow<WeatherDto> {
        val asyncValue = viewModelScope.async { useCase.getWeather("Warsaw") } // I can do async operations in a flow as well
        useCase.getWeather("Warsaw")
    }

    // 2. Flow as a LiveData
    // I can always transform a flow into a LiveData if I want to
    val flowAsLiveData = flow<WeatherDto> {
        useCase.getWeather("Warsaw")
    }.asLiveData(viewModelScope.coroutineContext)

    // 3. Operations on flow
    // Its really similar to rxJava Single or Observable - I can do many operations on a flow:
    // !! Every block of flow transformation is a corotuine which is cool
    val operationOnFlow = flow<WeatherDto> {
        useCase.getWeather("Warsaw")
    }.map { it }  // I can map it
        .filter { it.name == "Warsaw" } // I can filter it
        .combine(flow<Int> { 1 }, { weather, int -> weather}) // I can combine flows into something
        .onEach {  } // I can do an action on each emmited value
        .onStart {  } // I can do an action on start of the flow
        .onCompletion { } // I can do an action on completion
//        .flatMapLatest {  }.flatMapMerge { }.flatMapConcat { } // I can do different flatMaps
        .catch { throwable ->  } // I can catch exceptions here and do some action on exception. It catches only exceptions that occur higher in the hierarchy so if I want to catch all this should be called last
//        .collect {  } // collect the flow final value and do something with It
//        .drop() // drop like in a list
        .distinctUntilChanged() // the same values are filtered out
        .flowOn(Dispatchers.IO) // specify on which thread this flow should run
        .retry(1) // retry x times
        .retryWhen { cause, attempt -> attempt == 1L } // retry when

    // 3. StateFlow (from coroutines 1.4.0+)
    // This is a LiveData example with a trigger
    // You switchmap the trigger value and emit a liveData with a value.
    private val _stateFlow = MutableStateFlow<WeatherDto?>(null) // have to pass a value here. I can pass a ViewState.Loading here or something for starters
    val stateFlow: StateFlow<WeatherDto?> = _stateFlow

    fun collectAStateFlow() {
        // collecting a flow has to be done in a coroutine
        viewModelScope.launch {
            stateFlow.collect {
                // do sth with that value
            }
            _stateFlow.value = null // I can update a mutableStateFlow like that
        }
    }

    // 4. Trigger async example. Trigger + switchmap with livedata builder but the block is async
    // an example how to make many async calls.
    // The calls are made immediatly, and then when every call responds, the list of results is returned
    private val _asyncCityTrigger = MutableLiveData<List<String>>()
    val asyncCityTriggerLiveData = _asyncCityTrigger.switchMap { trigger ->
        val results = trigger.map {
            viewModelScope.async { useCase.getWeather(it) }
        }
        liveData {
            emit(results.map { it.await() })
        }
    }

    private val _multipleCitiesLiveDataFromMethod = MutableLiveData<List<WeatherDto>>()
    val multipleCitiesLiveDataFromMethod: LiveData<List<WeatherDto>> = _multipleCitiesLiveDataFromMethod

    fun getTriggerWeather(city: String) {
        viewModelScope.launch {
            // In a coroutine i have to use postValue on a LiveData
            _weather.postValue(useCase.getWeather(city))
        }
        // I can specify it like that:
        viewModelScope.launch(Dispatchers.IO) {} // It runs on IO thread. Cant do ".value" here. Have to do .postValue()
    }

    var jobToCancel: Job? = null
    val cancelableWeather = MutableLiveData<WeatherDto>()

    fun getMultipleCitiesAsyncFromList(dispatcher: CoroutineDispatcher, list: List<String>) {
        viewModelScope.launch(dispatcher) {
            val results = list.map {
                async { useCase.getWeather(it) }
            }
            _multipleCitiesLiveDataFromMethod.postValue(results.map { it.await() })
        }
    }

    fun getMultipleCitiesSequentiallyFromList(dispatcher: CoroutineDispatcher, list: List<String>) {
        viewModelScope.launch(dispatcher) {
            val results = list.map {
                useCase.getWeather(it)
            }
            _multipleCitiesLiveDataFromMethod.postValue(results)
        }
    }

    fun getMultipleCitiesAsync(dispatcher: CoroutineDispatcher) {
        viewModelScope.launch(dispatcher) {
            val warsawDeferred = async { useCase.getWeather("Warsaw") }
            val bialystokDeferred = async { useCase.getWeather("Bialystok") }
            val cracowDeferred = async { useCase.getWeather("Cracow") }
            val gdanskDeferred = async { useCase.getWeather("Gdansk") }
            val poznanDeferred = async { useCase.getWeather("Poznan") }

            _multipleCitiesLiveDataFromMethod.postValue(listOf(
                warsawDeferred.await(),
                bialystokDeferred.await(),
                cracowDeferred.await(),
                gdanskDeferred.await(),
                poznanDeferred.await()
            ))
        }
    }

    fun getMultipleCitiesSequentially(dispatcher: CoroutineDispatcher) {
        viewModelScope.launch(dispatcher) {
            val warsaw = useCase.getWeather("Warsaw")
            val bialystok = useCase.getWeather("Bialystok")
            val cracow = useCase.getWeather("Cracow")
            val gdansk = useCase.getWeather("Gdansk")
            val poznan = useCase.getWeather("Poznan")

            _multipleCitiesLiveDataFromMethod.postValue(listOf(
                warsaw, bialystok, cracow, gdansk, poznan
            ))
        }
    }

    fun getWeatherCancellable(dispatcher: CoroutineDispatcher) {
        jobToCancel = viewModelScope.launch(dispatcher) {
            delay(5000)
            cancelableWeather.postValue(useCase.getWeather("bialystok"))
        }
    }
}