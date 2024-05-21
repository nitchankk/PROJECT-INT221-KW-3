<template>
  <div v-if="isOpen" class="fixed inset-0 flex items-center justify-center">
    <div class="fixed inset-0 bg-black opacity-50"></div>
    <div class="bg-white rounded-lg p-6 max-w-md w-full relative z-10">
      <h2 class="text-lg font-semibold mb-4">Edit Status</h2>
      <div v-if="editedStatus">
        <form @submit.prevent="saveChanges" class="itbkk-modal-status">
          <div class="mb-4">
            <label for="name" class="block font-semibold mb-1 text-left"
              >Name:</label
            >
            <input
              v-model="editedStatus.name"
              type="text"
              id="name"
              name="name"
              maxlength="50"
              class="w-full border rounded-md p-2 font-medium itbkk-status-name"
            />
            <small
              v-if="editedStatus.name.length > 50"
              class="text-red-500"
              >Name must be at most 50 characters long.</small
            >
          </div>
          <div class="mb-4">
            <label
              for="description"
              class="block font-semibold mb-1 text-left itbkk-status-description"
              >Description:</label
            >
            <textarea
              v-model="editedStatus.description"
              id="description"
              name="description"
              rows="4"
              maxlength="200"
              class="w-full border rounded-md p-2 font-medium"
            ></textarea>
            <small
              v-if="editedStatus.description.length > 200"
              class="text-red-500"
              >Description must be at most 200 characters long.</small
            >
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

const editedStatus = ref({ name: '', description: '' })
const initialStatus = ref({ name: '', description: '' })

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
      editedStatus.value = { name: '', description: '' }
      initialStatus.value = { name: '', description: '' }
    }
  }
)

const isSaveDisabled = computed(() => {
  return (
    JSON.stringify(editedStatus.value) ===
      JSON.stringify(initialStatus.value) ||
    editedStatus.value.name.length > 50 ||
    editedStatus.value.description.length > 200
  )
})

const saveChanges = async () => {
  operationType.value = 'edit'
  try {
    if (props.selectedStatusIdToEdit === 1) {
      alert('The "No Status" status cannot be edited')
      emit('closeModal')
      throw new Error('The "No Status" status cannot be edited')
    }

    const existingStatuses = await fetchUtils.fetchData('statuses')
    const existingStatusNames = existingStatuses.map(
      (status) => status.name
    )

    if (editedStatus.value.name !== initialStatus.value.name) {
      if (existingStatusNames.includes(editedStatus.value.name)) {
        alert('Status name must be unique. Please enter a different name.')
        return
      }
    }

    const response = await fetchUtils.putData(
      `statuses/${props.selectedStatusIdToEdit}`,
      editedStatus.value
    )
    statusCode.value = response.statusCode
    if (response.success) {
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
