package com.github.uyutaka.github_client_mvvm.service.repository

import android.arch.lifecycle.MutableLiveData
import com.github.uyutaka.github_client_mvvm.service.model.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * ViewModelに対するデータプロバイダ
 * レスポンスをLiveData Objectにラップする
 */
class ProjectRepository {

    //Retrofitインターフェース
    private val githubService: GithubService

    //コンストラクタでRetrofitインスタンスを生成
    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(GithubService.HTTPS_API_GITHUB_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        githubService = retrofit.create(GithubService::class.java)
    }

    fun getProjectList(userId: String, page: Int?, perPage: Int?): MutableLiveData<List<Project>> {
        val data = MutableLiveData<List<Project>>()

        //Retrofitで非同期リクエスト->Callbackで(自分で実装したModel)型ListのMutableLiveDataにセット
        githubService.getProjectList(userId, page, perPage).enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>?) {
                data.setValue(response!!.body())
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                //TODO: null代入良くない + エラー処理
                data.setValue(null)
            }
        })

        return data
    }

    //APIにリクエストし、レスポンスをLiveDataで返す(詳細)
    //うまくenqueueでのCallbackをOverrideできない場合、Retrofitインターフェースの型指定など間違えて居る可能性あり
    fun getProjectDetails(userID: String, projectName: String): MutableLiveData<Project> {
        val data = MutableLiveData<Project>()

        githubService.getProjectDetails(userID, projectName).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                data.setValue(response.body())
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                //TODO: null代入良くない + エラー処理
                data.setValue(null)
            }
        })
        return data
    }

    companion object {
        val TAG: String = GithubService::class.java.name

        //staticに提供できるRepository
        private var projectRepository: ProjectRepository? = null

        val instance: ProjectRepository
            @Synchronized get() {
                return ProjectRepository()
            }
    }
}