package com.github.uyutaka.github_client_mvvm.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.uyutaka.github_client_mvvm.R
import com.github.uyutaka.github_client_mvvm.service.model.Project
import com.github.uyutaka.github_client_mvvm.view.callback.ProjectClickCallback
import com.github.uyutaka.github_client_mvvm.databinding.ProjectListItemBinding
class ProjectAdapter(private val projectClickCallback: ProjectClickCallback?) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    internal var projectList: MutableList<Project>? = null

    //現状との差分をListとしてRecyclerViewにセットする
    fun setProjectList(projectList: MutableList<Project>) {

        if (this.projectList == null) {
            this.projectList = projectList

            //positionStartの位置からitemCountの範囲において、データの変更があったことを登録されているすべてのobserverに通知する。
            notifyItemRangeInserted(0, projectList.size)
        } else {
            //2つのListの差分を計算するユーティリティー。Support Library 24.2.0で追加された。
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@ProjectAdapter.projectList!!.size
                }

                override fun getNewListSize(): Int {
                    return projectList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return this@ProjectAdapter.projectList!![oldItemPosition].id === projectList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = projectList[newItemPosition]
                    val old = projectList[oldItemPosition]

                    return project.id === old.id && project.git_url == old.git_url
                }
            })
            this.projectList = projectList

            //DiffUtilのメソッド=>差分を元にRecyclerViewAderpterのnotify系が呼ばれ、いい感じにアニメーションなどをやってくれます。
            result.dispatchUpdatesTo(this)
        }
    }

    fun updateProjectList(newProjectList: List<Project>) {

        for (item in newProjectList) {
            this.projectList!!.add(item)
        }
        notifyDataSetChanged()
    }


    //継承したインナークラスのViewholderをレイアウトとともに生成
    //bindするビューにコールバックを設定 -> ビューホルダーを返す
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ProjectViewHolder {
        val binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.project_list_item,
                parent,
                false) as ProjectListItemBinding

        binding.setCallback(projectClickCallback)

        return ProjectViewHolder(binding)
    }

    //ViewHolderをDataBindする
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.binding.setProject(projectList!![position])
        holder.binding.executePendingBindings()
    }

    //リストのサイズを返す
    override fun getItemCount(): Int {
        return if (projectList == null) 0 else projectList!!.size
    }

    //インナークラスにViewHolderを継承し、project_list_item.xml に対する Bindingを設定
    open class ProjectViewHolder(val binding: ProjectListItemBinding) : RecyclerView.ViewHolder(binding.getRoot())
}
