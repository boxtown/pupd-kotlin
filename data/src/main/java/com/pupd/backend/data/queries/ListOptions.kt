package com.pupd.backend.data.queries

/**
 * Data class representing possible list query options
 *
 * Created by maxiaojun on 9/3/16.
 */
data class ListOptions(var offset: Int = 0,
                       var limit: Int = 10,
                       var sort: String = "id",
                       var desc: Boolean = false)