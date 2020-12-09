package io.realworld.app.utils

import com.auth0.jwt.algorithms.Algorithm

object Cipher {
    val algorithm = Algorithm.HMAC256("dUqTPcVqhrvsC3XDwP3AvYt3G7mPLAqMLaDpJsazWqKdh7mffsHz8vJhYGfXU6MmqYwUUYjt2vE9RBshB9qqbhmLdyMTF7s48yV9KU8LCvceV2V9naw4VrrtDpAUKX2cM7tnvzr3pLFY46GnPQ5mfff6qsSU72ewKzdPjRDNubdSqP7uWeNYe6HmvM4gr8nBQ3YZ8LNFv3yNP3q4QUHY9jjGzHmmJwkakxPEgzV3XAPb2LBaej5VbMNFhX66Edj5")

    fun encrypt(data: String?): ByteArray =
            algorithm.sign(data?.toByteArray())

}