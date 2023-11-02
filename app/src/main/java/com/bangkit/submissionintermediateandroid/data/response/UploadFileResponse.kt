package com.bangkit.submissionintermediateandroid.data.response

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
