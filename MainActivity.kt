package com.meulauncher.tv

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var grid: RecyclerView
    private val adapter = AppAdapter { app -> openApp(app) }
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grid = findViewById(R.id.app_grid)
        grid.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.grid_columns))
        grid.adapter = adapter
        grid.setHasFixedSize(true)

        // Um launcher não deve "fechar" com o botão Voltar
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* ignora */ }
        })
    }

    override fun onResume() {
        super.onResume()
        reloadApps() // atualiza se algum app foi instalado ou removido
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        // Botão Home pressionado: volta ao topo da grade
        grid.scrollToPosition(0)
    }

    private fun reloadApps() {
        val pm = packageManager
        val self = packageName
        executor.execute {
            val apps = AppLoader.load(pm, self)
            runOnUiThread { adapter.submitList(apps) }
        }
    }

    private fun openApp(app: AppInfo) {
        try {
            startActivity(app.intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.erro_abrir, app.label), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
