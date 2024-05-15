<template>
  <div v-if="isOpen" class="fixed inset-0 flex items-center justify-center">
    <div class="fixed inset-0 bg-black opacity-50"></div>
    <div class="bg-white rounded-lg p-6 max-w-md w-full relative z-10">
      <h2 class="text-lg font-semibold mb-4">Edit Status</h2>
      <div v-if="editedStatus">
        <form @submit.prevent="saveChanges" class="itbkk-modal-status">
          <div class="mb-4">
            <label for="statusName" class="block font-semibold mb-1 text-left"
              >Name:</label
            >
            <input
              v-model="editedStatus.statusName"
              type="text"
              id="statusName"
              name="statusName"
              class="w-full border rounded-md p-2 font-medium itbkk-status-name"
            />
          </div>
          <div class="mb-4">
            <label
              for="statusDescription"
              class="block font-semibold mb-1 text-left itbkk-status-description"
              >Description:</label
            >
            <textarea
              v-model="editedStatus.statusDescription"
              id="statusDescription"
              name="statusDescription"
              rows="4"
              class="w-full border rounded-md p-2 font-medium"
            ></textarea>
          </div>
          <div class="flex justify-end">
            <button
              type="button"
              @click="emit('closeModal')"
              class="px-4 py-2 bg-gray-300 text-gray-800 rounded-md mr-2 itbkk-button-cancel"
            >
              Cancel
            </button>
            <button
              :disabled="isSaveDisabled"
              type="submit"
              class="px-4 py-2 rounded-md itbkk-button-confirm"
              :class="{
                'bg-gray-400 text-gray-600 cursor-not-allowed': isSaveDisabled,
                'bg-blue-500 text-white hover:bg-blue-600': !isSaveDisabled
              }"
            >
              Save
            </button>
          </div>
        </form>
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
import { defineProps, defineEmits, ref, watch, computed } from 'vue'
import fetchUtils from '../lib/fetchUtils'
import Toast from './Toast.vue'

const props = defineProps({
  isOpen: Boolean,
  statusData: Object,
  selectedStatusIdToEdit: Number
})

const emit = defineEmits(['closeModal', 'statusEdited'])

const editedStatus = ref({ statusName: '', statusDescription: '' })
const initialStatus = ref({ statusName: '', statusDescription: '' })

const showToast = ref(false)
const statusCode = ref(0)
const operationType = ref(null)

watch(
  () => props.statusData,
  (newValue) => {
    if (newValue) {
      editedStatus.value = { ...newValue }
      initialStatus.value = { ...newValue }
    } else {
      editedStatus.value = { statusName: '', statusDescription: '' }
      initialStatus.value = { statusName: '', statusDescription: '' }
    }
  }
)

const isSaveDisabled = computed(() => {
  return (
    JSON.stringify(editedStatus.value) === JSON.stringify(initialStatus.value)
  )
})

const saveChanges = async () => {
  operationType.value = 'edit'
  console.log('OpeartionType', operationType.value)
  try {
    if (props.selectedStatusIdToEdit === 1) {
      alert('The "No Status" status cannot be edited')
      emit('closeModal')
      throw new Error('The "No Status" status cannot be edited')
    }

    const response = await fetchUtils.putData(
      `statuses/${props.selectedStatusIdToEdit}`,
      editedStatus.value
    )
    statusCode.value = response.statusCode
    if (response.success) {
      console.log(
        'Status updated successfully:',
        response.data,
        statusCode.value
      )
      emit('closeModal')
      emit('statusEdited')
      showToast.value = true
    } else {
      console.error('Failed to update status:', response.data)
    }
  } catch (error) {
    console.error('Error updating status:', error.message)
    if (error.message.includes('404')) {
      statusCode.value = 404
      console.log(statusCode.value)
      showToast.value = true
      emit('closeModal')
    }
  }
}
</script>

<style scoped>
.modal-container {
  max-width: 80%;
}
</style>
