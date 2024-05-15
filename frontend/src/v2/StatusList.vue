<template>
  <div>
    <h1 class="text-3xl font-bold">IT Bangmod Kradan Kanban by kw-3</h1>
    <div id="app">
      <h2 class="text-2xl">Status Manager</h2>
      <div class="back-home">
        <button @click="backToHomePage" class="itbkk-button-home">HOME</button>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th style="width: 30px; text-align: center" class="add-icon">
                <button
                  @click="openAddModal"
                  style="border: none; background: none; padding: 0"
                  class="itbkk-button-add"
                >
                  <img
                    src="../assets/add-status.png"
                    alt="Add Icon"
                    style="width: 25px; height: 25px"
                  />
                </button>
              </th>
              <th style="width: 150px">Name</th>
              <th style="width: 400px">Description</th>
              <th style="width: 80px">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(status, index) in statuses"
              :key="status.id"
              class="itbkk-item"
            >
              <td class="itbkk-status" style="text-align: center">
                {{ index + 1 }}
              </td>
              <td class="itbkk-status-name">{{ status.statusName }}</td>
              <td class="itbkk-status-description" style="text-align: left">
                <span
                  v-if="status.statusDescription"
                  v-html="status.statusDescription"
                ></span>
                <span
                  v-else
                  class="no-description"
                  style="font-style: italic; color: #808080"
                  >No description provided</span
                >
              </td>
              <td>
                <div class="action-buttons">
                  <button class="itbkk-button-action">
                    <button
                      style="margin-right: 15px"
                      class="itbkk-button-edit"
                      @click="openEditModal(status)"
                    >
                      <img
                        src="../assets/pencil.png"
                        alt="EditStatus"
                        class="action-icon"
                      />
                    </button>
                    <button
                      class="itbkk-button-delete"
                      @click="checkTasksBeforeDelete(status)"
                    >
                      <img
                        src="../assets/delete-status.png"
                        alt="DeleteStatus"
                        class="action-icon"
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
    <AddStatusModal
      :isAddOpen="isAddOpen"
      @closeModal="closeModal"
      @statusAdded="handleStatusAdded"
    >
    </AddStatusModal>
    <EditStatusModal
      :isOpen="isEditOpen"
      @closeModal="closeModal"
      @statusEdited="handleStatusEdited"
      :statusData="selectedStatus"
      :selectedStatusIdToEdit="selectedStatusIdToEdit"
    >
    </EditStatusModal>
    <DeleteStatusModal
      :isOpen="isDeleteOpen"
      @closeModal="closeModal"
      @statusDeleted="handleDelete"
      :statusIdToDelete="selectedStatusIdToDelete"
    >
    </DeleteStatusModal>
    <TransferStatusModal
      :isOpen="isTransferOpen"
      @closeModal="closeModal"
      @statusTransfered="handleTransfer"
      :statusIdToTransfer="selectedStatusIdToTransfer"
    >
    </TransferStatusModal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import fetchUtils from '../lib/fetchUtils'
import { useRoute, useRouter } from 'vue-router'

const statuses = ref([])
const tasks = ref([])
const route = useRoute()
const router = useRouter()

async function fetchData() {
  try {
    const statusData = await fetchUtils.fetchData('statuses')
    statuses.value = statusData

    const taskData = await fetchUtils.fetchData('tasks')
    tasks.value = taskData

    // Check if route has a status ID and validate it
    const statusId = route.params.statusId
    if (
      statusId &&
      !statuses.value.some((status) => status.statusId === statusId)
    ) {
      alert('404 Not Found: Status ID does not exist')
      router.push('/status')
    }
  } catch (error) {
    console.error('Error fetching status data:', error)
  }
}

const backToHomePage = () => {
  router.push({name: "taskView"})
}

// Add ----------------------------------------------------------
import AddStatusModal from './AddStatusModal.vue'

let isAddOpen = ref(false)

const openAddModal = () => {
  isAddOpen.value = true
}

const closeModal = () => {
  isAddOpen.value = false
  isEditOpen.value = false
  isDeleteOpen.value = false
  isTransferOpen.value = false
}

const handleStatusAdded = () => {
  fetchData()
}

// Edit ----------------------------------------------------------

import EditStatusModal from './EditStatusModal.vue'

const isEditOpen = ref(false)
const selectedStatus = ref(null)
const selectedStatusIdToEdit = ref(null)

const openEditModal = (status) => {
  selectedStatus.value = { ...status }
  console.log('Object to edit', selectedStatus.value)
  selectedStatusIdToEdit.value = status.statusId
  console.log('Id to edit', selectedStatusIdToEdit.value)
  isEditOpen.value = true
}
const handleStatusEdited = () => {
  fetchData()
}

// Delete --------------------------------------------------------
import DeleteStatusModal from './DeleteStatusModal.vue'

const isDeleteOpen = ref(false)

const selectedStatusIdToDelete = ref(null)

const openDeleteModal = (status) => {
  console.log('Status ID to delete:', status.statusId)
  selectedStatusIdToDelete.value = status.statusId
  isDeleteOpen.value = true
}

const handleDelete = () => {
  fetchData()
}

// Transfer --------------------------------------------------------
import TransferStatusModal from './TransferStatusModal.vue'
const isTransferOpen = ref(false)

const selectedStatusIdToTransfer = ref(null)

const openTransferModal = (status) => {
  console.log('Status ID to transfer:', status.statusId)
  selectedStatusIdToTransfer.value = status.statusId
  isTransferOpen.value = true
}

const handleTransfer = () => {
  fetchData()
}

// Check if status is in use --------------------------------------
const checkTasksBeforeDelete = (status) => {
  const statusInUse = tasks.value.some(
    (task) => task.statusName === status.statusName
  )
  if (statusInUse) {
    openTransferModal(status)
  } else {
    openDeleteModal(status)
  }
}

onMounted(fetchData)
</script>

<style scoped>
#app {
  width: 1200px;
  margin: 0 auto;
}

.table-container {
  margin: 0 auto;
  width: 80%;
  max-width: 1200px;
  overflow-x: auto;
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 8px;
  overflow: hidden;
}

thead {
  background-color: #d3e7fc;
}

th,
td {
  padding: 12px;
  border: 1px solid #e2e8f0;
  text-align: left;
  height: 40px;
}

th {
  font-weight: bold;
}

tbody tr:nth-child(even) {
  background-color: #ebebeb;
}

tbody tr:hover {
  background-color: #acc1ca;
}

.action-buttons {
  text-align: center;
}

.itbkk-button-action {
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
}

.action-icon {
  width: 25px;
  height: 25px;
  display: block;
  margin: 0 auto;
}

.back-home {
  text-align: right;
  margin: 10px;
}

.back-home button {
  background-color: #276fad;
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

.back-home button:hover {
  background-color: #aebac4;
}

.back-home button:focus {
  outline: none;
}

.back-home button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-icon button:hover {
  transform: translateY(1px);
}

.add-icon button:active {
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
