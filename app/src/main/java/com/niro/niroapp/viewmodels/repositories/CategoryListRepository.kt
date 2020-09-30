package com.niro.niroapp.viewmodels.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.niro.niroapp.models.APIError
import com.niro.niroapp.models.APILoader
import com.niro.niroapp.models.APIResponse
import com.niro.niroapp.models.Success
import com.niro.niroapp.models.responsemodels.CategoryResponse
import com.niro.niroapp.models.responsemodels.GenericAPIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListRepository<T>(context : Context?) : Repository(context) {


    override fun getResponse(): MutableLiveData<APIResponse> {
        val responseData = MutableLiveData<APIResponse>()

        val loader = APILoader(true)

        getAPIInterface()?.categories?.enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                val error = APIError(404,getDefaultErrorMessage())
                responseData.value = error
            }

            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if(response.body()?.success != true) {
                    try {
                        val errorResponse = Gson().fromJson<GenericAPIResponse<Any>>(
                            response.errorBody()?.charStream(),
                            GenericAPIResponse::class.java
                        )
                        responseData.value = APIError(
                            response.code(),
                            if (!errorResponse.message.isNullOrEmpty()) errorResponse.message else getDefaultErrorMessage()
                        )
                        return
                    } catch (exception : Exception) {
                        responseData.value = APIError(response.code(), getDefaultErrorMessage())

                    }
                }

                val success = Success<T>(response.body()?.data as? T)
                responseData.value = success
            }

        })

        responseData.value = loader
        return responseData
    }
}