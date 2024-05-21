<template>
  <div class="modal-wrapper">
    <div class="modal">
      <div class="modal-content">
        <h2 class="modal-title">Edit Task</h2>

        <form @submit.prevent="handleEditTask">
          <!-- Title Input -->
          <div class="form-group">
            <label for="title">Title:</label>
            <input
              type="text"
              id="title"
              v-model="editedTask.title"
              class="itbkk-title"
              required
              maxlength="100"
            />
            <small v-if="editedTask.title.length > 100" class="error">
              Title must be at most 100 characters long.
            </small>
          </div>

          <!-- Description Input -->
          <div class="form-group">
            <label for="description">Description:</label>
            <textarea
              id="description"
              v-model="editedTask.description"
              class="itbkk-description"
              :placeholder="
                editedTask.description ? '' : 'No Description Provided'
              "
              maxlength="500"
            ></textarea>
            <small v-if="editedTask.description.length > 500" class="error">
              Description must be at most 500 characters long.
            </small>
          </div>

          <!-- Assignees Input -->
          <div class="form-group">
            <label for="assignees">Assignees:</label>
            <input
              type="text"
              id="assignees"
              v-model="editedTask.assignees"
              class="itbkk-assignees"
              :placeholder="editedTask.assignees ? '' : 'Unassigned'"
              maxlength="30"
            />
            <small v-if="editedTask.assignees.length > 30" class="error">
              Assignees must be at most 30 characters long.
            </small>
          </div>

          <!-- Status Select -->
          <div class="form-group">
            <label for="status">Status:</label>
            <select
              id="status"
              v-model="editedTask.statusName"
              class="itbkk-status"
            >
              <option v-if="statuses.length === 0" value="" disabled>
                Loading...
              </option>
              <option
                v-else
                v-for="status in statuses"
                :key="status.id"
                :value="status.name"
              >
                {{ status.name }}
              </option>
            </select>
          </div>

          <!-- Display timezone, created date, and updated date -->
          <div class="modal-buttons">
            <button
              class="itbkk-button itbkk-button-confirm"
              type="submit"
              :class="{ disabled: isSaveDisabled }"
              :disabled="isSaveDisabled"
            >
              Save
            </button>
            <button
              class="itbkk-button itbkk-button-cancel"
              type="button"
              @click="closeModal"
            >
              Cancel
            </button>
          </div>

          <!-- Details Container -->
          <div class="details-container">
            <div class="details-group">
              <strong>Timezone:</strong> {{ timezone }}
              <p>
                <strong>Created Date:</strong>
                {{ formatLocalDate(task.createdOn) }}
              </p>
              <p>
                <strong>Updated Date:</strong>
                {{ formatLocalDate(task.updatedOn) }}
              </p>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits, onMounted } from 'vue'
import FetchUtils from '../lib/fetchUtils'

const props = defineProps({
  task: {
    type: Object,
    required: true
  },
  closeModal: {
    type: Function,
    required: true
  },
  onTaskUpdated: {
    type: Function,
    required: true
  }
})
const emit = defineEmits(['editSuccess'])
const editedTask = ref({
  title: '',
  description: '',
  assignees: '',
  statusName: '' 
})
const statuses = ref([])

if (props.task) {
  editedTask.value = { ...props.task }
}

const initialTask = JSON.parse(JSON.stringify(props.task))

const isSaveDisabled = computed(() => {
  return (
    JSON.stringify(editedTask.value) === JSON.stringify(initialTask) ||
    editedTask.value.title.length > 100 ||
    editedTask.value.description.length > 500 ||
    editedTask.value.assignees.length > 30
  )
})

const handleEditTask = async () => {
  try {
    const updatedTask = {
      title: editedTask.value.title,
      description: editedTask.value.description,
      assignees: editedTask.value.assignees,
      statusName: editedTask.value.statusName, 
      updatedOn: new Date().toISOString()
    }

    const response = await FetchUtils.putData(
      `tasks/${props.task.id}`,
      updatedTask
    )

    if (response && response.success) {
      props.onTaskUpdated(response.data)
      props.closeModal()
      if (response.statusCode === 200) {
        emit('editSuccess', response.statusCode, 'edit')
        console.log('Task updated successfully.')
      }
    } else {
      console.error('Failed to update task')
      console.error('Failed to update task. Please try again.')
      alert('Failed to edit task. Please try again.')
    }
  } catch (error) {
    console.error('Error updating task:', error)
    console.error('An error occurred while updating the task.')
    alert('Error to updating task. Please try again.')
  }
}
const formatLocalDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('en-GB')
}

const timezone = computed(
  () => Intl.DateTimeFormat().resolvedOptions().timeZone
)

const fetchStatuses = async () => {
  try {
    const data = await FetchUtils.fetchData('statuses')
    statuses.value = data
  } catch (error) {
    console.error('Error fetching statuses:', error)
  }
}

onMounted(() => {
  fetchStatuses()
})
</script>

<style scoped>
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
  width: 700px;
}

.modal-content {
  padding: 30px;
}

.modal-title {
  margin-top: 0;
  font-size: 24px;
  font-weight: bold;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  text-align: left;
}

input[type='text'],
textarea,
select {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

input[type='text'] {
  width: 500px;
}

textarea {
  width: 500px;
  height: 200px;
  resize: none;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
}

.itbkk-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.itbkk-button-confirm {
  background-color: #68d391;
  color: #fff;
}

.itbkk-button-cancel {
  background-color: #f67c5e;
  color: #fff;
}

.itbkk-button-confirm:hover,
.itbkk-button-cancel:hover {
  opacity: 0.8;
}

.details-container {
  position: absolute;
  top: 30px;
  right: 30px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 250px;
  height: 90px;
  text-align: right;
  background-color: #ffffffd2;
}

.details-group {
  margin-bottom: 10px;
  font-size: 13px;
  padding: 10px;
}

.disabled {
  background-color: gray;
  cursor: not-allowed;
}

.error {
  color: red;
  font-size: 12px;
}

.itbkk-description::placeholder,
.itbkk-assignees::placeholder {
  font-style: italic;
}
</style>
