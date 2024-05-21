<template>
  <div class="modal-wrapper">
    <div class="modal">
      <div class="modal-content">
        <h2 class="modal-title">DELETE TASK</h2>
        <p class="itbkk-message">
          Do you want to delete the task number "{{ taskIndex }}" <br />
          "{{ title }}" ?
        </p>
        <div class="modal-buttons">
          <button
            class="itbkk-button itbkk-button-confirm"
            @click="confirmDelete"
          >
            Confirm
          </button>
          <button class="itbkk-button itbkk-button-cancel" @click="cancelModal">
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import FetchUtils from '../lib/fetchUtils'
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  closeModal: {
    type: Function,
    required: true
  },
  id: {
    type: Number,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  taskIndex: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['deleted', 'showSuccessModal']) // Add 'showSuccessModal' to emitted events

const confirmDelete = async () => {
  try {
    const response = await FetchUtils.deleteData(`tasks/${props.id}`)
    const statusCode = response.statusCode
    console.log('Deletion status code:', statusCode)
    emit('deleted', props.id, statusCode, 'delete')
    if (statusCode === 200) {
      emit('showSuccessModal') // Emit 'showSuccessModal' event
    }
    props.closeModal()
  } catch (error) {
    console.error('Error deleting task:', error)
    alert('Failed to delete task. Please try again.')
  }
}

const cancelModal = () => {
  props.closeModal()
}
</script>

<style scoped>
/* Styles for the modal */
.modal-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background-color: white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  width: 500px;
}

.modal-content {
  padding: 30px;
}

.modal-title {
  margin-top: 0;
  font-size: 24px;
  font-weight: bold;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.itbkk-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.itbkk-button-confirm {
  background-color: #f67c5e;
  color: #fff;
}

.itbkk-button-cancel {
  background-color: #cccccc;
  color: #fff;
}

.itbkk-button-confirm:hover,
.itbkk-button-cancel:hover {
  opacity: 0.8;
}
</style>
