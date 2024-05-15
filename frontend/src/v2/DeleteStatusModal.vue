<template>
  <div class="modal-mask" v-if="isOpen">
    <div class="itbkk-message">
      <h2 class="text-lg font-semibold mb-4">Delete Status</h2>
      <p class="text-left mb-4">Are you sure you want to delete the status?</p>
      <div class="flex justify-end">
        <button
          type="button"
          class="px-4 py-2 bg-gray-300 text-gray-800 rounded-md mr-2 itbkk-button-cancel"
          @click="closeModal"
        >
          Cancel
        </button>
        <button
          type="button"
          class="px-4 py-2 bg-red-500 text-white hover:bg-red-600 rounded-md itbkk-button-confirm"
          @click="deleteStatus"
        >
          Confirm
        </button>
      </div>
    </div>
  </div>
  <Toast
    :show="showToast"
    :statusCode="statusCode"
    :operationType="operationType"
    @close="showToast = false"
  />
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue'
import fetchUtils from '../lib/fetchUtils'
import Toast from './Toast.vue'

const props = defineProps({
  isOpen: Boolean,
  statusIdToDelete: Number
})

const emit = defineEmits(['closeModal', 'statusDeleted'])

const showToast = ref(false)

const statusCode = ref(0)

const operationType = ref(null)

const closeModal = () => {
  emit('closeModal')
}

const deleteStatus = async () => {
  operationType.value = 'delete'
  console.log('OpeartionType', operationType.value)
  try {
    if (typeof props.statusIdToDelete === 'undefined') {
      throw new Error('Status ID to delete is not defined')
    }

    if (props.statusIdToDelete === 1) {
      alert('The "No Status" status cannot be deleted')
      closeModal()
      throw new Error('The "No Status" status cannot be deleted')
    }

    const response = await fetchUtils.deleteData(
      `statuses/${props.statusIdToDelete}`
    )
    statusCode.value = response.statusCode
    if (response.success) {
      console.log('Status deleted successfully!', statusCode.value)
      emit('statusDeleted')
      showToast.value = true
      closeModal()
    } else {
      throw new Error(response.data.message)
    }
  } catch (error) {
    console.error('Error deleting status:', error.message)
    if (error.message.includes('404')) {
      statusCode.value = 404
      console.log(statusCode.value)
      showToast.value = true
      closeModal()
    }
    if (error.message.includes('500')) {
      statusCode.value = 500
      console.log(statusCode.value)
      showToast.value = true
      closeModal()
    }
  }
}
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.itbkk-message {
  width: 300px;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
}
</style>
