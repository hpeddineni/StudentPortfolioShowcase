package uk.ac.tees.mad.w9643793.screens.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.tees.mad.w9643793.screens.profile.StudentFetchedData

class DashboardViewModel : ViewModel() {
    private val _students = MutableLiveData<List<StudentFetchedData>>()
    val students: LiveData<List<StudentFetchedData>> = _students

    private val databaseReference = FirebaseDatabase.getInstance().getReference("students")

    init {
        fetchData()
    }

    private fun fetchData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<StudentFetchedData>()
                snapshot.children.forEach { it ->
                    val student = it.getValue(StudentFetchedData::class.java)
                    student?.let { list.add(it) }
                }
                _students.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
                Log.d("HomeViewModel", "Error: ${error.message}")
            }
        })
    }
}