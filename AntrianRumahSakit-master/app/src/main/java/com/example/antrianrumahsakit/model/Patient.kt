package com.example.antrianrumahsakit.model

data class Patient(
    val id: Int,
    val name: String,
    val uniqueMedicalId: String? = null, // kode rekam medis, opsional
    val lastVisitTimestamp: Long? = null
) {
    val isRegular: Boolean
        get() = !uniqueMedicalId.isNullOrEmpty()
}
