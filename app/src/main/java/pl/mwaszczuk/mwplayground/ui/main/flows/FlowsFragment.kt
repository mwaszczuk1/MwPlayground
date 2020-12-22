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
    }

    private fun setupListeners() {
        btnCancelWeather.setOnClickListener {

        }
        btnMultipleCitiesSequential.setOnClickListener {

        }
        btnMultipleCitiesAsync.setOnClickListener {

        }
        btnMultipleCitiesAsyncFromList.setOnClickListener {

        }
        btnMultipleCitiesSequentialFromList.setOnClickListener {

        }
        btnWeatherCancelable.setOnClickListener {

        }
    }
}
