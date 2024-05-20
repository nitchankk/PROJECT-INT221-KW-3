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
              <th style="width: 600px">
                Title
                <button @click="openFilterModal" class="itbkk-filter-status">
                  <img
                    src="../assets/filter.png"
                    alt="filter Icon"
                    style="width: 25px; height: 25px"
                  />
                </button>
              </th>
              <th style="width: 200px">Assignees</th>
              <th style="width: 120px; position: relative">
                Status
                <button
                  @click="sortTasksByStatus"
                  class="itbkk-button-sort"
                  style="
                    position: absolute;
                    right: 10px;
                    top: 50%;
                    transform: translateY(-50%);
                    border: none;
                    background: none;
                    padding: 0;
                  "
                >
                  <img
                    v-if="sortOrder === 0"
                    src="../assets/sort.png"
                    alt="Sort Icon"
                  />
                  <img
                    v-else-if="sortOrder === 1"
                    src="../assets/aesc.png"
                    alt="Sort Ascending Icon"
                  />
                  <img
                    v-else
                    src="../assets/desc.png"
                    alt="Sort Descending Icon"
                  />
                </button>
              </th>

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
                  <button class="itbkk-button-action">
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
    <filter-modal
      v-if="showFilterModal"
      :statuses="statuses"
      :selectedStatuses="selectedStatuses"
      @applyFilter="applyFilter"
      @close="closeFilterModal"
    ></filter-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TaskModal from './TaskModal.vue'
import AddModal from './AddModal.vue'
import DeleteModal from './DeleteModal.vue'
import StatusModal from './StatusModal.vue'
import EditModal from './EditModal.vue'
import FilterModal from './FilterModal.vue'
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
const selectedStatuses = ref([])

const sortOrder = ref(0)

const route = useRoute()
const router = useRouter()

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

    const taskId = route.params.taskId
    if (taskId && !tasks.value.some((task) => task.taskId === taskId)) {
      router.push('/task')
    }
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
  let sorted = [...tasks.value]
  if (sortOrder.value === 1) {
    sorted.sort((a, b) => a.statusName.localeCompare(b.statusName))
  } else if (sortOrder.value === 2) {
    sorted.sort((a, b) => b.statusName.localeCompare(a.statusName))
  } else {
    sorted.sort((a, b) => new Date(a.createdOn) - new Date(b.createdOn))
  }
  return sorted.filter(
    (task) =>
      selectedStatuses.value.length === 0 ||
      selectedStatuses.value.includes(task.statusName)
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
    if (data) {
      selectedTask.value = data
    }
  } catch (error) {
    console.error('Error fetching task details:', error)
    if (error.status === 404) {
    } else {
      alert('The Request Task does not exist')
    }
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
  statusCode.value = receivedStatusCode
  tasks.value = tasks.value.filter((task) => task.taskId !== deletedTaskId)
  closeDeleteModal()
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
    showSuccessModal.value = true
    statusCode.value = status
  }
}
const openEditModal = async (taskId) => {
  try {
    const data = await FetchUtils.fetchData('tasks', taskId)
    taskToEdit.value = data
    if (taskToEdit.value) {
      operationType.value = 'edit'
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
  router.push({ name: 'statusView' })
}

const sortTasksByStatus = () => {
  sortOrder.value = (sortOrder.value + 1) % 3
}

const showFilterModal = ref(false)

const openFilterModal = () => {
  showFilterModal.value = true
}

const closeFilterModal = () => {
  showFilterModal.value = false
}

const applyFilter = (selectedStatusesValue) => {
  selectedStatuses.value = selectedStatusesValue
  closeFilterModal()
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
  width: 1500px;
  margin: 0 auto;
}

.table-container {
  margin: 0 auto;
  width: 100%;
  max-width: 1700px;
  overflow-x: none;
  border-radius: 8px;
  font-size: 19px;
}

.table {
  border-collapse: collapse;
  width: 100%;
  border-radius: 8px;
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
  margin: 10px;
}

.itbkk-manage-status {
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
  width: 120px;
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

.itbkk-button-sort {
  width: 35px;
  height: 35px;
}

.itbkk-button-sort button:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.itbkk-button-sort img {
  width: 100%;
  height: 100%;
}

.itbkk-button-sort img:hover {
  transform: translateY(1px);
}

.itbkk-button-sort img:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.itbkk-filter-status button:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.itbkk-filter-status img {
  width: 100%;
  height: 100%;
}

.itbkk-filter-status img:hover {
  transform: translateY(1px);
}

.itbkk-filter-status img:active {
  transform: translateY(2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
