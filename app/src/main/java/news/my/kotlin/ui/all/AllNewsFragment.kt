package news.my.kotlin.ui.all

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import news.my.kotlin.adapter.AllNewsAdapter
import news.my.kotlin.databinding.FragmentHomeBinding
import news.my.kotlin.db.EntityMapper
import news.my.kotlin.model.ApiErrorBody
import news.my.kotlin.utils.ApiResult
import news.my.kotlin.utils.ApiStatus
import retrofit2.HttpException

@AndroidEntryPoint
class AllNewsFragment : Fragment() {
    private val TAG = "AllNewsFragment"

    private var _binding: FragmentHomeBinding? = null
    private val allNewViewModel by viewModels<AllNewsViewModel>()
    private val newsAdapter by lazy { AllNewsAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        allNewViewModel.networkBoundNews.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    hideLoadingProgress()

                }

                ApiStatus.ERROR -> {
                    hideLoadingProgress()
                    when (val errorBody = response.error!!) {
                        is HttpException -> {
                            val message = errorBody.response()?.errorBody()?.string()
                            val messageBody = Gson().fromJson(message, ApiErrorBody::class.java)
                            val actualErrorMessage = messageBody.message
                            Snackbar.make(
                                binding.allNewsbaseView,
                                "Error: $actualErrorMessage\nViewing cached data",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        else -> {
                            val message = errorBody.message ?: "Something went wrong"
                            Snackbar.make(
                                binding.allNewsbaseView,
                                "Error: $message\nViewing cached data",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }


                    }
                }

                ApiStatus.LOADING -> {
                    showLoadingProgress()
                }
            }
            newsAdapter.differ.submitList(response.data?.let {
                EntityMapper.fromEntityList(
                    it
                )
            })
            binding.apply {
                topHeadlinesRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = newsAdapter
                }
            }
        }

//        allNewViewModel.articles.observe(viewLifecycleOwner) { result ->
//            when (result?.status) {
//                ApiStatus.SUCCESS -> {
//                    hideLoadingProgress()
//                    newsAdapter.differ.submitList(result.data?.articles)
//                    binding.apply {
//                        topHeadlinesRecyclerView.apply {
//                            layoutManager = LinearLayoutManager(requireContext())
//                            adapter = newsAdapter
//                        }
//                    }
//                }
//
//                ApiStatus.ERROR -> {
//                    hideLoadingProgress()
//                    result.data?.articles?.let {
//                        newsAdapter.differ.submitList(it)
//                        binding.apply {
//                            topHeadlinesRecyclerView.apply {
//                                layoutManager = LinearLayoutManager(requireContext())
//                                adapter = newsAdapter
//                            }
//                        }
//
//                        Snackbar.make(
//                            binding.allNewsbaseView,
//                            "Viewing cached data",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                    result.message?.let { Log.e(TAG, it) }
//                }
//
//                ApiStatus.LOADING -> {
//                    showLoadingProgress()
//                }
//
//                else -> {}
//            }
//        }

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