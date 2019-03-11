package com.github.uyutaka.github_client_mvvm.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import com.github.uyutaka.github_client_mvvm.service.model.Project
import com.github.uyutaka.github_client_mvvm.service.repository.ProjectRepository

class ProjectViewModel(application: Application, private val projectID: String) : AndroidViewModel(application) {
    //ゲッター
    var observableProject: MutableLiveData<Project>

    var project = ObservableField<Project>()

    init {
        observableProject = ProjectRepository.instance.getProjectDetails("google", projectID)
    }

    //セッター
    fun setProject(project: Project) {
        this.project.set(project)
    }

    /**
     * IDの(DI)依存性注入クラス
     * Architecture ComponentsとDagger2の合わせ技(参考記事:https://qiita.com/satorufujiwara/items/f42b176404287690f1d0)
     */
    class Factory(private val application: Application, private val projectID: String) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProjectViewModel(application, projectID) as T
        }
    }
}
