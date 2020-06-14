package com.ananth.githubrepositories.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ananth.githubrepositories.R
import com.ananth.githubrepositories.databinding.ListFragmentBinding

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    /**
     * Lazily initialize the [ListViewModel].
     */
    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, ListViewModel.Factory(requireActivity().application))
            .get(ListViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = ListFragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the ListViewModel
        binding.viewModel = viewModel

        val adapter = RepoListAdapter(RepoClickListener {
            this.findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment(it))
        })
        binding.repositoryList.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
