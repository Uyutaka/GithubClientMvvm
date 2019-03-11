package com.github.uyutaka.github_client_mvvm.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.uyutaka.github_client_mvvm.R
import com.github.uyutaka.github_client_mvvm.service.model.Project
import com.github.uyutaka.github_client_mvvm.viewmodel.ProjectViewModel
import com.github.uyutaka.github_client_mvvm.databinding.FragmentProjectDetailsBinding


class ProjectFragment : Fragment() {
    private var binding: FragmentProjectDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // DataBinding対象のレイアウトをinflateする
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_details, container, false)

        // Create and set the adapter for the RecyclerView.
        return binding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //DI
        val factory = ProjectViewModel.Factory(
            (activity as MainActivity).application, arguments?.getString(KEY_PROJECT_ID)?:""
        )

        //project_idをキーに注入してViewModelインスタンスを取得
        val viewModel = ViewModelProviders.of(this, factory).get(ProjectViewModel::class.java)

        //ViewにViewModelをセット
        binding!!.projectViewModel = viewModel
        //app:visibleGone="@{isLoading}"をtrueに
        binding!!.isLoading = true

        //データ監視を開始 -> 差分を監視して、ViewModelに伝える
        observeViewModel(viewModel)

    }

    //Modelのデータを監視するメソッド
    fun observeViewModel(viewModel: ProjectViewModel) {
        viewModel.observableProject.observe(this, object : Observer<Project> {
            override fun onChanged(project: Project?) {
                if (project != null) {

                    binding!!.setIsLoading(false)

                    viewModel.setProject(project)
                }
            }
        })
    }

    companion object {
        val TAG: String = ProjectFragment::class.java.name

        private const val KEY_PROJECT_ID = "project_id"

        //fragmentTofragmentでidを渡す
        fun forProject(projectID: String): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()

            args.putString(KEY_PROJECT_ID, projectID)
            fragment.setArguments(args)

            return fragment
        }
    }

}
