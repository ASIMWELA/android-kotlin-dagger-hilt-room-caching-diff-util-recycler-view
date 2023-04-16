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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import news.my.kotlin.adapter.AllNewsAdapter
import news.my.kotlin.databinding.FragmentHomeBinding
import news.my.kotlin.utils.ApiStatus

@AndroidEntryPoint
class AllNewsFragment : Fragment() {
    private val TAG = "AllNewsFragment"

    private var _binding: FragmentHomeBinding? = null
    private val allNewViewModel by viewModels<AllNewsViewModel>()
    private val newsAdapter by lazy { AllNewsAdapter() }

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

        allNewViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result?.status) {
                ApiStatus.SUCCESS -> {
                    hideLoadingProgress()
                    newsAdapter.differ.submitList(result.data?.articles)
                    binding.apply {
                        topHeadlinesRecyclerView.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = newsAdapter
                        }
                    }
                }

                ApiStatus.ERROR -> {
                    hideLoadingProgress()
                    result.data?.articles?.let{
                        newsAdapter.differ.submitList(it)
                        binding.apply {
                            topHeadlinesRecyclerView.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = newsAdapter
                            }
                        }
                    }
                    result.message?.let { Log.e(TAG, it) }
                }

                ApiStatus.LOADING -> {
                    showLoadingProgress()
                }

                else -> {}
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoadingProgress() {
        binding.loadingAllNewsProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        binding.loadingAllNewsProgressBar.visibility = View.GONE
    }
}