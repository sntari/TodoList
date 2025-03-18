package com.example.todolist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.TodoDao
import com.example.todolist.db.TodoEntity

class MainActivity : AppCompatActivity(), OnItemLongClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var db: AppDatabase
    private lateinit var todoDao: TodoDao
    private lateinit var todoList: ArrayList<TodoEntity>
    private lateinit var adapter: TodoRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        getAllTodolist()

        binding.btnAddTodo.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getAllTodolist() {
        Thread {
            todoList = ArrayList(todoDao.getAllTodo())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        runOnUiThread {
            Log.d("MainActivity", "리사이클러뷰 설정 중...")

            adapter = TodoRecyclerViewAdapter(todoList, this)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(this)

            Log.d("MainActivity", "리사이클러뷰 설정 완료, 아이템 수: ${todoList.size}")

        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllTodolist()
    }

    override fun onLongClick(position: Int) {
        Log.d("TodoList", "롱 클릭 감지됨! 위치: $position")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_title))
        builder.setMessage(getString(R.string.alert_message))
        builder.setNegativeButton(getString(R.string.alert_no), null)
        builder.setPositiveButton(
            getString(R.string.alert_yes),
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTodo(position)
                }
            }
        )
        builder.show()
    }

    private fun deleteTodo(position: Int) {
        Thread {
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}