package com.programmingz.miniclip.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.programmingz.miniclip.R
import com.programmingz.miniclip.databinding.VideoItemRowBinding
import com.programmingz.miniclip.model.UserModel
import com.programmingz.miniclip.model.VideoModel

class VideoListAdapter(
    options: FirestoreRecyclerOptions<VideoModel>
) : FirestoreRecyclerAdapter<VideoModel, VideoListAdapter.VideoViewHolder>(options){

    inner class VideoViewHolder(private val binding: VideoItemRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindVideo(videoModel: VideoModel){
            //bind user data
            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener {
                    val userModel = it?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username
                        //binding profile picture
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_person)
                                )
                            .into(binding.profileIcon)
                    }
                }
            binding.captionView.text = videoModel.title
            binding.progressBar.visibility = View.VISIBLE

            //bindVideo
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true
                }

                setOnClickListener {
                    if (isPlaying){
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                    } else {
                        start()
                        binding.pauseIcon.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        holder.bindVideo(model)
    }


}