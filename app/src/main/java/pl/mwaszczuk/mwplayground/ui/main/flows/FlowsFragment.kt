package pl.mwaszczuk.mwplayground.ui.main.flows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_coroutines.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mwaszczuk.mwplayground.R

class FlowsFragment : Fragment() {

    private val viewModel: FlowsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_flows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        btnCancelWeather.setOnClickListener {
            viewModel.jobToCancel?.cancel()
            btnWeatherCancelable.text = "canceled"
        }
        btnMultipleCitiesSequential.setOnClickListener {
            viewModel.getMultipleCitiesSequentially(Dispatchers.IO)
        }
        btnMultipleCitiesAsync.setOnClickListener {
            viewModel.getMultipleCitiesAsync(Dispatchers.IO)
        }
        btnMultipleCitiesAsyncFromList.setOnClickListener {
            viewModel.getMultipleCitiesAsyncFromList(Dispatchers.IO, viewModel.citiesList)
        }
        btnMultipleCitiesSequentialFromList.setOnClickListener {
            viewModel.getMultipleCitiesSequentiallyFromList(Dispatchers.IO, viewModel.citiesList)
        }
        btnWeatherCancelable.setOnClickListener {
            viewModel.getWeatherCancellable(Dispatchers.IO)
        }
    }

    private fun setupObservers() {
        viewModel.cancelableWeather.observe(viewLifecycleOwner, Observer {
            btnWeatherCancelable.text = "done"
        })
        viewModel.cityTriggerLiveData.observe(viewLifecycleOwner, Observer {
            txtTriggerSwitchmapWithLiveDataBuilder.text = it.name.plus(it.main?.temperature.toString())
        })
        viewModel.liveDataBuilderWeather.observe(viewLifecycleOwner, Observer {
            txtLiveDataBuilderData.text = it.name.plus(it.main?.temperature.toString())
        })
        viewModel.weather.observe(viewLifecycleOwner, Observer {
          txtSimplePatternData.text = it.name.plus(it.main?.temperature.toString())
        })
        viewModel.multipleCitiesLiveDataFromMethod.observe(viewLifecycleOwner, Observer {
            txtCity.text = "cities: ".plus(it.size)
            txtTemperature.text = it.sumByDouble { it.main?.temperature?.toDouble() ?: 0.0}.div(it.size).toString()
            txtHumidity.text = it.sumBy { it.main?.humidity ?: 0 }.div(it.size).toString()
        })
    }
}
