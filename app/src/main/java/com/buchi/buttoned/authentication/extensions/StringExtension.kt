package com.buchi.buttoned.authentication.extensions

inline val String.isValidUserName get() = isNotBlank()
inline val String.isValidPassword get() = isNotBlank()
inline val String.isValidFullName get() = isNotBlank()