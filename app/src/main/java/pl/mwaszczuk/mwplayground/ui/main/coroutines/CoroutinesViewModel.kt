package pl.mwaszczuk.mwplayground.ui.main.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import pl.mwaszczuk.mwplayground.data.network.dto.WeatherDto

class CoroutinesViewModel(
    private val useCase: CoroutinesUseCase
) : ViewModel() {

    // Simple pattern with a private mutable live data and public liveData
    // And i populate it by providing the data from a method. Here it is getTriggerWeather(city: String)
    private val _weather = MutableLiveData<WeatherDto>()
    val weather: LiveData<WeatherDto> = _weather

    // Same can be achieved with this.
    // you can call suspended things inside because it is a coroutine inside
    // emit sets this live data value.
    val liveDataBuilderWeather = liveData {
        emit(useCase.getWeather("Białystok"))
    }

    // This is a LiveData example with a trigger
    // You switchmap the trigger value and emit a liveData with a value.
    private val _cityTrigger = MutableLiveData<String>()
    val cityTriggerLiveData = _cityTrigger.switchMap { trigger ->
        liveData {
            emit(useCase.getWeather(trigger))
        }
    }

    // an example how to make many async calls.
    // The calls are made immediatly, and then when every call responds, the list of results is returned
    private val _multipleCitiesTrigger = MutableLiveData<List<String>>()
    val multipleCitiesLiveDataFromTrigger = _multipleCitiesTrigger.switchMap { trigger ->
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
            // I can do ".value" because by default the launch creates a coroutine on the MAIN thread
            _weather.value = useCase.getWeather(city)
        }
        // I can specify it like that:
        viewModelScope.launch(Dispatchers.IO) {} // It runs on IO thread. Cant do ".value" here. Have to do .postValue()
    }

    private var jobToCancel: Job? = null
    val cancelableWeather = MutableLiveData<WeatherDto>()

    fun getMultipleCitiesAsyncFromList(dispatcher: CoroutineDispatcher, list: List<String>) {
        viewModelScope.launch(dispatcher) {
            val results = list.map {
                async { useCase.getWeather(it) }
            }
            _multipleCitiesLiveDataFromMethod.postValue(results.map { it.await() }) // idk what dispatcher will it be so use postValue for safety
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