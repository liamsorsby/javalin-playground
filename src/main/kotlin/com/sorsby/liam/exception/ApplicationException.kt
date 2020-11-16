package com.sorsby.liam.exception

class ApplicationException(val statusCode: Int, message: String) : Exception(message)
