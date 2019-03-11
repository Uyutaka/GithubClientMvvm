package com.github.uyutaka.github_client_mvvm.view.callback

import com.github.uyutaka.github_client_mvvm.service.model.Project

/**
 * クリック操作を伝えるinterface
 * @link onClick(Project project) 詳細画面に移動
 */
interface ProjectClickCallback {
    fun onClick(project: Project)
}
