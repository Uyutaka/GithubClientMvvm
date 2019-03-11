package com.github.uyutaka.github_client_mvvm.service.repository

import com.github.uyutaka.github_client_mvvm.service.model.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GithubService {

    // Repository List
    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String, @Query("page") page: Int?, @Query("per_page") perPage: Int?): Call<List<Project>>

    // Repository Description
    @GET("/repos/{user}/{reponame}")
    fun getProjectDetails(@Path("user") user: String, @Path("reponame") projectName: String): Call<Project>

    companion object {
        //Retrofitインターフェース(APIリクエストを管理)
        const val HTTPS_API_GITHUB_URL = "https://api.github.com/"
    }
}