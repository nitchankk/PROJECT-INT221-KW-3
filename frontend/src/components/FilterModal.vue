<template>
  <div class="modal">
    <div class="modal-content p-6 bg-white rounded-lg">
      <h2 class="text-xl font-semibold mb-4">Select Statuses to Filter</h2>
      <div class="status-checkboxes">
        <!-- Add "All" checkbox -->
        <div class="flex items-center mb-2">
          <input
            type="checkbox"
            v-model="selectAll"
            class="form-checkbox mr-2 h-4 w-4 text-indigo-600 transition duration-150 ease-in-out"
          />
          <label class="text-sm">All</label>
        </div>
        <!-- Render other checkboxes -->
        <div
          v-for="status in statuses"
          :key="status.statusName"
          class="flex items-center mb-2"
        >
          <input
            type="checkbox"
            :value="status.statusName"
            v-model="localSelectedStatuses"
            class="form-checkbox mr-2 h-4 w-4 text-indigo-600 transition duration-150 ease-in-out"
          />
          <label class="text-sm">{{ status.statusName }}</label>
        </div>
      </div>
      <div class="modal-buttons flex justify-end mt-4">
        <button
          @click="applyFilter"
          class="apply-button px-4 py-2 bg-green-500 text-white rounded-md"
        >
          Apply Filter
        </button>
        <button
          @click="$emit('close')"
          class="cancel-button px-4 py-2 bg-red-500 text-white rounded-md ml-2"
        >
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue'

const props = defineProps({
  statuses: Array,
  selectedStatuses: Array
})

const emit = defineEmits(['applyFilter', 'close'])

const localSelectedStatuses = ref([...props.selectedStatuses])
const selectAll = ref(false)

// Watch for changes in the selectAll checkbox value
watch(selectAll, (newValue) => {
  // If "All" checkbox is checked, select all statuses
  if (newValue) {
    localSelectedStatuses.value = props.statuses.map(
      (status) => status.statusName
    )
  } else {
    localSelectedStatuses.value = [] // If "All" checkbox is unchecked, clear the selection
  }
})

const applyFilter = () => {
  if (selectAll.value) {
    // If "All" checkbox is checked, select all statuses
    emit(
      'applyFilter',
      props.statuses.map((status) => status.statusName)
    )
  } else {
    emit('applyFilter', localSelectedStatuses.value)
  }
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  max-width: 400px;
}

.status-checkbox {
  margin-bottom: 0.5rem;
}

.apply-button,
.cancel-button {
  cursor: pointer;
}
</style>
