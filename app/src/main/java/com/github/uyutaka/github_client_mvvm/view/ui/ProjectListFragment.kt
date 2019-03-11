package com.github.uyutaka.github_client_mvvm.view.ui

import android.arch.lifecycle.Lifecycle
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
import com.github.uyutaka.github_client_mvvm.view.adapter.ProjectAdapter
import com.github.uyutaka.github_client_mvvm.view.callback.ProjectClickCallback
import com.github.uyutaka.github_client_mvvm.viewmodel.ProjectListViewModel
import com.github.uyutaka.github_client_mvvm.databinding.FragmentProjectListBinding
/**
 *
 */
class ProjectListFragment : Fragment() {
    private var projectAdapter: ProjectAdapter? = null
    private var binding: FragmentProjectListBinding? = null

    //callbackに操作イベントを設定
    private val projectClickCallback = object : ProjectClickCallback {
        override fun onClick(project: Project) {
            if (getLifecycle().currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (getActivity() as MainActivity).show(project)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dataBinding用のレイアウトリソースをセット
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        //イベントのcallbackをadapterに伝達
        projectAdapter = ProjectAdapter(projectClickCallback)
        //上記adapterをreclclerViewに適用
        binding!!.projectList.setAdapter(projectAdapter)
        //Loading開始
        binding!!.setIsLoading(true)
        //rootViewを取得
        return binding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ProjectListViewModel::class.java)

        observeViewModel(viewModel)
    }

    //observe開始
    private fun observeViewModel(viewModel: ProjectListViewModel) {

        //データが更新されたらアップデートするように、LifecycleOwnerを紐付け、ライフサイクル内にオブザーバを追加
        //オブザーバーは、STARTED かRESUMED状態である場合にのみ、イベントを受信する
        viewModel.getInitialProjectListObservable(1, 2)
        viewModel.projectListObservable?.observe(this, object : Observer<List<Project>> {
            override fun onChanged(projects: List<Project>?) {
                if (projects != null) {
                    binding!!.setIsLoading(false)
                    projectAdapter?.setProjectList(projects.toMutableList())
                    nextProjectList(viewModel)
                }
            }
        })
    }

    private fun nextProjectList(viewModel: ProjectListViewModel) {
        viewModel.getInitialProjectListObservable(2, 2)
        viewModel.projectListObservable?.observe(this, object : Observer<List<Project>> {
            override fun onChanged(projects: List<Project>?) {
                if (projects != null) {
                    binding!!.setIsLoading(false)
                    projectAdapter!!.updateProjectList(projects)
                }
            }
        })
    }

    companion object {
        val TAG: String = ProjectListFragment::class.java.name
    }

}
