package com.github.uyutaka.github_client_mvvm.view.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.uyutaka.github_client_mvvm.R
import com.github.uyutaka.github_client_mvvm.service.model.Project

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            //プロジェクト一覧のFragment
            val fragment = ProjectListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, ProjectListFragment.TAG)
                .commit()
        }
    }

    fun show(project: Project) {
        val projectFragment = ProjectFragment.forProject(project.name)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(R.id.fragment_container, projectFragment, null)
            .commit()
    }

companion object {
    val TAG: String = MainActivity::class.java.name

}
}
