package news.my.kotlin.ui.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import news.my.kotlin.databinding.FragmentHomeBinding
import news.my.kotlin.utils.ApiStatus

@AndroidEntryPoint
class AllNewsFragment : Fragment() {
    private val TAG = "AllNewsFragment"

    private var _binding: FragmentHomeBinding? = null
    private val allNewViewModel by viewModels<AllNewsViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        allNewViewModel.newsAsLiveData.observe(viewLifecycleOwner) { result ->
            when (result?.status) {
                ApiStatus.SUCCESS -> {
                    result.data?.let { Log.e(TAG, it.toString()) }
                }

                ApiStatus.ERROR -> {
                    result.message?.let { Log.e(TAG, it) }
                }

                ApiStatus.LOADING -> {

                }
                else -> {

                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}