package com.android.movies.util

import android.util.Base64

fun decode(args: String): String {
    val decode = Base64.decode(args, Base64.DEFAULT)
    return String(decode, Charsets.UTF_8)
}