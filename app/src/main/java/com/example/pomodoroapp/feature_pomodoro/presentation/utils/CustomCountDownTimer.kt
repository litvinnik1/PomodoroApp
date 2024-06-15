package com.example.pomodoroapp.feature_pomodoro.presentation.utils

import android.os.CountDownTimer

open class CustomCountDownTimer (
    private val millisInFuture: Long,
    private val countDownInterval: Long
) {
    private var millisUntilFinished = millisInFuture
    private var timer = InternalTimer(this,millisInFuture, countDownInterval)
    private var isRunning = false
    var onTick: ((millisUntilFinished: Long)-> Unit)? = null
    var onFinish: (() -> Unit)? = null


    private class InternalTimer(
        private val parent: CustomCountDownTimer,
        millisInFuture: Long,
        countDownInternal: Long
    ): CountDownTimer(millisInFuture, countDownInternal){

        var millisUntilFinished = parent.millisUntilFinished
        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }

    }
    fun pauseTimer(){

    }
    fun resumeTimer(){

    }
    fun startTimer(){

    }
    fun restartTimer(){

    }
    fun destroyTimer(){

    }
}