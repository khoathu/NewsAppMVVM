package com.example.newsappmvvm.presentation.ui

import android.content.Context
import retrofit2.HttpException
import java.io.IOException

class BaseHandlerError {

    companion object {
        const val ACTION_GO_TO_LOGIN = 1
        const val ACTION_SHOW_ERROR_MESSAGE = 2
    }


    fun handleBaseErrorAlert(context: Context, throwable: Throwable?, code: Int) {
        var errorCode = 0
        when (code) {
            1 -> {
                // handle case 1
            }
            2 -> {
                // handle case 2
            }
            else -> {
                throwable?.let {
                    when (it) {
                        is IOException -> {
                            //A network or conversion error happened show handle here
                        }
                        is HttpException -> {
                            //we will handle non-2xx http code here : example 401 , 500...etc
                            errorCode = (throwable as HttpException).code()
                            when (errorCode) {
                                //UNAUTHORIZED
                                //SERVER ERROR...
                            }
                        }
                        else -> {
                            //We don't know what happened. We need to simply convert to an unknown error
                        }
                    }
                }
            }
        }
    }
}

