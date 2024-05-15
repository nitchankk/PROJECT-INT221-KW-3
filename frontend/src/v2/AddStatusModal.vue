<template>
  <div class="fixed inset-0 flex items-center justify-center" v-if="isAddOpen">
    <div class="fixed inset-0 bg-black opacity-50"></div>
    <div class="bg-white rounded-lg p-6 max-w-md w-full relative z-10">
      <h2 class="text-lg font-semibold mb-4">Add Status</h2>
      <form @submit.prevent="addStatus" class="itbkk-modal-status">
        <div class="mb-4">
          <label for="statusName" class="block font-semibold mb-1 text-left"
            >Name</label
          >
          <input
            type="text"
            id="statusName"
            v-model.trim="statusName"
            class="w-full border rounded-md p-2 font-medium"
          />
        </div>
        <div class="mb-4">
          <label
            for="statusDescription"
            class="block font-semibold mb-1 text-left"
            >Description</label
          >
          <textarea
            id="statusDescription"
            v-model.trim="statusDescription"
            class="w-full border rounded-md p-2 font-medium"
            rows="4"
          ></textarea>
        </div>
        <div class="flex justify-end">
          <button
            type="button"
            class="px-4 py-2 bg-gray-300 text-gray-800 rounded-md mr-2 itbkk-button-cancel"
            @click="closeModal"
          >
            Cancel
          </button>
          <button
            type="submit"
            :class="[
              'px-4 py-2 rounded-md itbkk-button-confirm',
              !statusName.trim()
                ? 'bg-gray-400 text-gray-600 cursor-not-allowed'
                : 'bg-blue-500 text-white hover:bg-blue-600'
            ]"
            :disabled="!statusName.trim()"
          >
            Add Status
          </button>
        </div>
      </form>
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
  isAddOpen: Boolean
})

const emit = defineEmits(['closeModal', 'statusAdded'])

const statusName = ref('')
const statusDescription = ref('')
const statusCode = ref(0)
const operationType = ref(null)
const showToast = ref(false)

const closeModal = () => {
  statusName.value = ''
  statusDescription.value = ''
  emit('closeModal')
}

const addStatus = async () => {
  operationType.value = 'add'
  console.log('OpeartionType', operationType.value)
  try {
    const newStatus = {
      statusName: statusName.value,
      statusDescription: statusDescription.value
    }
    const response = await fetchUtils.postData('statuses', newStatus)
    statusCode.value = response.statusCode
    console.log('Response status code:', statusCode.value)
    if (response.success) {
      closeModal()
      emit('statusAdded')
      showToast.value = true
    }
  } catch (error) {
    console.error('Error adding status:', error)
    alert('An error has occurred, the status could not be added.')
    closeModal()
  }
}
</script>