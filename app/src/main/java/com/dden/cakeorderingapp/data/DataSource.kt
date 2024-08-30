package com.dden.cakeorderingapp.data

import com.dden.cakeorderingapp.R

object DataSource {
    val flavors = listOf(
        (R.string.vanilla),
        (R.string.chocolate),
        (R.string.red_velvet),
        (R.string.salted_caramel),
        (R.string.coffee)
    )
    val quantityOptions = listOf(
        Pair((R.string.one_cake),1),
        Pair((R.string.two_cake),2),
        Pair((R.string.three_cake),3)
    )
}