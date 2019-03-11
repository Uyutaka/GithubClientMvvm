package com.github.uyutaka.github_client_mvvm.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.github.uyutaka.github_client_mvvm.service.model.Project
import com.github.uyutaka.github_client_mvvm.service.repository.ProjectRepository

/**
 * List<Project>のrepositoryから送られてくるデータとuiイベントに責務を持つViewModel
 * 引用：実際のケースでは、結果データをObserving Viewに渡す前に変換が必要な場合があります。
 * 変換を行うには、以下のドキュメントに示すTransformationクラスを使用できます。
 * https : //developer.android.com/topic /libraries/architecture/livedata.html#transformations_of_livedata
</Project> */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    //監視対象のLiveData
    //UIが観察できるようにコンストラクタで取得したLiveDataを公開する
    var projectListObservable: MutableLiveData<List<Project>>? = null
        private set

    val googleProjectListObservable: MutableLiveData<List<Project>>?
        get() {
            projectListObservable = ProjectRepository.instance.getProjectList("Google", 2, 10)
            return projectListObservable
        }

    fun getInitialProjectListObservable(page: Int?, perPage: Int?) {
        projectListObservable = ProjectRepository.instance.getProjectList("Google", page, perPage)
    }

    companion object {
        val TAG: String = ProjectListViewModel::class.java.name
    }
}
