package com.alcorp.aryunas.ui.kamar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.alcorp.aryunas.data.AppRepository
import com.alcorp.aryunas.data.local.entity.KamarEntity

class KamarViewModel(private val appRepository: AppRepository) : ViewModel() {
    var kamarEntity: LiveData<PagedList<KamarEntity>>? = null
        get() {
            if (field == null) {
                field = getKamar()
            }
            return field
        }
        private set

    @JvmName("getKamar1")
    fun getKamar(): LiveData<PagedList<KamarEntity>> = appRepository.getKamar()

    var id = MutableLiveData<Int>()

    fun setSelected(id: Int) {
        this.id.value = id
    }

    var kamarEntityById: LiveData<KamarEntity> = Transformations.switchMap(id) { id_kamar ->
        appRepository.getKamarById(id_kamar)
    }

    fun getKamarBooking() = appRepository.getKamarBooking()
    fun countKamar(id_kamar: Int) = appRepository.countKamar(id_kamar)
    fun insertKamar(kamarEntity: KamarEntity) = appRepository.insertKamar(kamarEntity)
    fun updateKamar(kamarEntity: KamarEntity) = appRepository.updateKamar(kamarEntity)
    fun deleteKamar(id_kamar: Int) = appRepository.deleteKamar(id_kamar)
}