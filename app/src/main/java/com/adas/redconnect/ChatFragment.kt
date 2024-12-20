package com.adas.redconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>

    private lateinit var chatId: String
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        // Retrieve chatId from arguments
        chatId = arguments?.getString(ARG_CHAT_ID) ?: throw IllegalArgumentException("Chat ID not provided")

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        messageList = mutableListOf()
        messageAdapter = MessageAdapter(messageList, auth.currentUser?.uid ?: "")
        recyclerView.adapter = messageAdapter

        // Initialize input field and send button
        editTextMessage = view.findViewById(R.id.editTextMessage)
        buttonSend = view.findViewById(R.id.buttonSend)

        // Load existing messages
        loadMessages()

        // Handle send button click
        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                editTextMessage.text.clear()
            }
        }

        return view
    }

    // Function to load messages from Firebase
    private fun loadMessages() {
        val chatRef = database.getReference("chats/$chatId/messages")

        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let { messageList.add(it) }
                }
                messageAdapter.notifyDataSetChanged()
                if (messageList.isNotEmpty()) {
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    // Function to send a message
    private fun sendMessage(messageText: String) {
        val chatRef = database.getReference("chats/$chatId/messages")
        val messageId = chatRef.push().key ?: return
        val message = Message(
            senderId = auth.currentUser?.uid ?: "",
            message = messageText,
            timestamp = System.currentTimeMillis().toString()
        )

        // Save message to Firebase
        chatRef.child(messageId).setValue(message)
    }

    companion object {
        private const val ARG_CHAT_ID = "chatId"

        fun newInstance(chatId: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putString(ARG_CHAT_ID, chatId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}


