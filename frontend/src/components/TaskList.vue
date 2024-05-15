<template>
  <div>
    <h1 class="text-3xl font-bold">IT Bangmod Kradan Kanban by kw-3</h1>
    <h2 class="text-2xl">Task List</h2>
    <div id="app">
      <div class="manage-status">
        <button @click="goToStatusManagement" class="itbkk-manage-status">
          Manage Status
        </button>
      </div>
      <div class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th
                class="itbkk-button-add"
                style="width: 50px; text-align: center"
              >
                <button
                  @click="handleAddTask"
                  style="border: none; background: none; padding: 0"
                >
                  <img
                    src="../assets/add.png"
                    alt="Add Icon"
                    style="width: 30px; height: 30px"
                  />
                </button>
              </th>
              <th style="width: 600px">Title</th>
              <th style="width: 200px">Assignees</th>
              <th style="width: 100px">Status</th>
              <th style="width: 70px">
                <img
                  src="../assets/menu-bar.png"
                  alt="Action Icon"
                  style="
                    width: 25px;
                    height: 25px;
                    display: block;
                    margin: 0 auto;
                  "
                />
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(task, index) in sortedTasks"
              :key="task.taskId"
              class="itbkk-item"
            >
              <td class="border px-4 py-2" style="text-align: center">
                {{ index + 1 }}
              </td>
              <td class="itbkk-title" @click="handleTaskClick(task.taskId)">
                {{ task.title }}
              </td>
              <td
                class="border px-4 py-2 itbkk-assignees"
                :style="{ fontStyle: task.assignees ? 'normal' : 'italic' }"
              >
                {{ task.assignees || 'Unassigned' }}
              </td>
              <td
                class="border px-4 py-2 itbkk-status"
                :data-status="task.statusName"
              >
                {{ getStatusLabel(task.statusName, statuses) }}
              </td>
              <td class="border px-4 py-2" style="width: 100px">
                <div class="action-buttons">
                  <button
                    style="border: none; background: none; padding: 0"
                    class="itbkk-button-action"
                  >
                    <button
                      @click="openEditModal(task.taskId)"
                      style="
                        border: none;
                        background: none;
                        padding: 0;
                        margin-right: 10px;
                      "
                      class="itbkk-button-edit"
                    >
                      <img
                        src="../assets/edit.png"
                        alt="Edit Icon"
                        style="width: 30px; height: 30px"
                      />
                    </button>

                    <button
                      @click="openDeleteModal(task.taskId)"
                      style="border: none; background: none; padding: 0"
                      class="itbkk-button-delete"
                    >
                      <img
                        src="../assets/delete2.png"
                        alt="Delete Icon"
                        style="width: 30px; height: 30px"
                      />
                    </button>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <task-modal
      v-if="selectedTask"
      :task="selectedTask"
      :timezone="timezone"
      :createdDate="formatLocalDate(selectedTask.createdOn)"
      :updatedDate="formatLocalDate(selectedTask.updatedOn)"
      :closeModal="closeModal"
    />

    <add-modal
      v-if="showAddModal"
      @taskSaved="handleTaskSaved"
      @showStatusModal="handleShowStatusModal"
      :closeModal="cancelAdd"
    />

    <status-modal
      :showModal="showSuccessModal"
      :statusCode="statusCode"
      :closeModal="closeSuccessModal"
      :operationType="operationType"
    />

    <delete-modal
      v-if="showDeleteModal"
      :closeModal="closeDeleteModal"
      :taskId="taskIdToDelete"
      :taskTitle="taskTitleToDelete"
      :taskIndex="taskIndexToDelete"
      @deleted="handleTaskDeleted"
    />
    <edit-modal
      v-if="showEditModal"
      :task="taskToEdit"
      :closeModal="closeEditModal"
      :onTaskUpdated="onTaskUpdated"
      @editSuccess="handleEditSuccess"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import TaskModal from './TaskModal.vue'
import AddModal from './AddModal.vue'
import DeleteModal from './DeleteModal.vue'
import StatusModal from './StatusModal.vue'
import EditModal from './EditModal.vue'
import FetchUtils from '../lib/fetchUtils'

const tasks = ref([])
const statuses = ref([])
const selectedTask = ref(null)
const showAddModal = ref(false)
const showDeleteModal = ref(false)
const showSuccessModal = ref(false)
const statusCode = ref(null)
const taskIdToDelete = ref(null)
const showEditModal = ref(false)
const taskToEdit = ref(null)
const operationType = ref('')
const taskTitleToDelete = ref(null)
const taskIndexToDelete = ref(null)

const route = useRoute()

const formatLocalDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('en-GB')
}

const timezone = computed(
  () => Intl.DateTimeFormat().resolvedOptions().timeZone
)

const fetchTasks = async () => {
  try {
    const data = await FetchUtils.fetchData('tasks')
    tasks.value = data
  } catch (error) {
    console.error('Error fetching tasks:', error)
  }
}

const fetchStatuses = async () => {
  try {
    const data = await FetchUtils.fetchData('statuses')
    statuses.value = data
  } catch (error) {
    console.error('Error fetching statuses:', error)
  }
}

const sortedTasks = computed(() => {
  return tasks.value.sort(
    (a, b) => new Date(a.createdOn) - new Date(b.createdOn)
  )
})

const getStatusLabel = (statusName, statuses) => {
  const status = statuses.find((s) => s.statusName === statusName)
  return status ? status.statusName : 'No Status'
}

const openModal = async (taskId) => {
  if (!taskId) {
    console.error('Task ID is invalid or missing.')
    return
  }
  try {
    const data = await FetchUtils.fetchData(`tasks/${taskId}`)
    selectedTask.value = data
  } catch (error) {
    console.error('Error fetching task details:', error)
  }
}
const handleTaskClick = (taskId) => {
  if (taskId) {
    openModal(taskId)
  } else {
    console.error('Invalid taskId:', taskId)
  }
}
const handleAddTask = () => {
  showAddModal.value = true
  operationType.value = 'add'
}
const handleTaskSaved = (savedTask) => {
  tasks.value.push(savedTask)
  showAddModal.value = false
  tasks.value.sort((a, b) => new Date(a.createdOn) - new Date(b.createdOn))
}
const cancelAdd = () => {
  showAddModal.value = false
}
const closeModal = () => {
  selectedTask.value = null
}
const openDeleteModal = (taskId) => {
  const task = tasks.value.find((task) => task.taskId === taskId)
  if (task) {
    taskIdToDelete.value = taskId
    taskTitleToDelete.value = task.title
    taskIndexToDelete.value = tasks.value.indexOf(task) + 1
    operationType.value = 'delete'
    showDeleteModal.value = true
  }
}
const handleTaskDeleted = (deletedTaskId, receivedStatusCode) => {
  console.log('Received deletion status code:', receivedStatusCode)
  statusCode.value = receivedStatusCode // Set statusCode here
  tasks.value = tasks.value.filter((task) => task.taskId !== deletedTaskId)
  closeDeleteModal()
  // Show success modal after deletion
  showSuccessModal.value = true
}
const closeDeleteModal = () => {
  showDeleteModal.value = false
}
const closeSuccessModal = () => {
  showSuccessModal.value = false
}
const handleShowStatusModal = (status) => {
  if (status === 201 || status === 200) {
    // If the status is 201 or 200, show the success modal
    showSuccessModal.value = true
    statusCode.value = status
  }
}
const openEditModal = async (taskId) => {
  try {
    const data = await FetchUtils.fetchData('tasks', taskId)
    taskToEdit.value = data
    if (taskToEdit.value) {
      operationType.value = 'edit' // Set operationType to 'edit' when opening edit modal
      showEditModal.value = true
    }
  } catch (error) {
    console.error('Error fetching task details:', error)
    alert('Failed to edit task. Please try again.')
  }
}
const closeEditModal = () => {
  showEditModal.value = false
}
const onTaskUpdated = (updatedTask) => {
  const taskIndex = tasks.value.findIndex(
    (task) => task.taskId === updatedTask.taskId
  )
  if (taskIndex !== -1) {
    tasks.value[taskIndex] = updatedTask
  }
}
const handleEditSuccess = (status) => {
  console.log('Received status code after edit:', status)
  statusCode.value = status
  showSuccessModal.value = true
}
const goToStatusManagement = () => {
  window.location.href = '/status'
}
onMounted(() => {
  fetchTasks()
  fetchStatuses()
})
onMounted(() => {
  const taskId = route.params.taskId
  if (taskId) {
    openModal(taskId)
  }
})
</script>
<style scoped>
#app {
  width: 1200px;
  margin: 0 auto;
}

table-container {
  margin: 0 auto;
  width: 80%;
  max-width: 1200px;
  overflow-x: auto;
}

.table {
  border-collapse: collapse;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.table th,
.table td {
  border: 1px solid #e2e8f0;
  padding: 12px;
  text-align: left;
  height: 40px;
}

.table th {
  background-color: #d3e7fc;
  font-weight: bold;
}

tbody tr:nth-child(even) {
  background-color: #ebebeb;
}

tbody tr:hover {
  background-color: #acc1ca;
}

.itbkk-status {
  padding: 8px 12px;
  border-radius: 4px;
  text-transform: uppercase;
}

.itbkk-status[data-status='TO DO'] {
  background-color: #f67c5e;
  color: #333;
}

.itbkk-status[data-status='DOING'] {
  background-color: #f6e05e;
  color: #333;
}

.itbkk-status[data-status='DONE'] {
  background-color: #68d391;
  color: #333;
}

.itbkk-status[data-status='NO STATUS'] {
  background-color: #f5f5f5;
  color: #888;
  font-style: italic;
}

.action-buttons {
  text-align: center;
}

.action-button {
  display: flex;
  border: none;
  background: none;
  padding: 0;
  margin-right: 10px;
}

.manage-status {
  text-align: right;
  /* Align to the right */
  margin: 10px;
}

.manage-status button {
  background-color: #aebac4;
  color: #fff;
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
  transition: background-color 0.3s ease;
  text-align: center;
}

.manage-status button:hover {
  background-color: #276fad;
}

.manage-status button:focus {
  outline: none;
}

.manage-status button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.itbkk-button-add button:hover {
  transform: translateY(1px);
}

.itbkk-button-add button:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.itbkk-button-action button:hover {
  transform: translateY(1px);
}

.itbkk-button-action button:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>