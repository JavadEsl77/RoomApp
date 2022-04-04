package com.example.myroom.main.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.*
import com.example.myroom.data.database.Note
import com.example.myroom.databinding.ActivityMainBinding
import com.example.myroom.main.viewmodel.NoteViewModal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickInterface {
    private lateinit var databinding: ActivityMainBinding

    lateinit var viewModal: NoteViewModal
    lateinit var notesRV: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notesRV = findViewById(R.id.notesRV)
        notesRV.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteRVAdapter(this, this, this)
        notesRV.adapter = noteRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        // on below line we are calling all notes method
        // from our view modal class to observer the changes on list.
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                // on below line we are updating our list.
                noteRVAdapter.updateList(it)
                if (list.isEmpty()) {
                    notesRV.visibility = View.GONE
                    databinding.txtEmpty.visibility = View.VISIBLE
                } else {
                    notesRV.visibility = View.VISIBLE
                    databinding.txtEmpty.visibility = View.GONE
                }
            }
        })

        databinding.btnSave.setOnClickListener {

            val formatted: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm")
                formatted = current.format(formatter)
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            var note = Note(
                databinding.edtTitle.text.toString().trim(),
                databinding.edtDescription.text.toString(),
                formatted.toString()
            )
            viewModal.addNote(note)

            viewModal.allNotes.observe(this, Observer { list ->
                list?.let {
                    // on below line we are updating our list.
                    noteRVAdapter.updateList(it)
                    if (list.isEmpty()) {
                        notesRV.visibility = View.GONE
                        databinding.txtEmpty.visibility = View.VISIBLE
                    } else {
                        notesRV.visibility = View.VISIBLE
                        databinding.txtEmpty.visibility = View.GONE
                    }
                }
            })

            databinding.edtTitle.text.clear()
            databinding.edtDescription.text.clear()

        }


    }

    override fun onDeleteIconClick(note: Note) {
        viewModal.deleteNote(note)

    }

    override fun onNoteClick(note: Note) {
        TODO("Not yet implemented")
    }
}