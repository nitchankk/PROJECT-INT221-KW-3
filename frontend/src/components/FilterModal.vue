<template>
  <div class="modal">
    <div class="modal-content p-6 bg-white rounded-lg">
      <h2 class="text-xl font-semibold mb-4">Select Statuses to Filter</h2>
      <div class="status-checkboxes">
        <!-- Select All Checkbox -->
        <div class="flex items-center mb-2">
          <input
            type="checkbox"
            v-model="selectAll"
            @change="selectAllChanged"
            class="form-checkbox mr-2 h-4 w-4 text-indigo-600 transition duration-150 ease-in-out"
          />
          <label class="text-sm">All</label>
        </div>
        <!-- Render checkboxes for statuses -->
        <div
          v-for="status in statuses"
          :key="status.statusName"
          class="flex items-center mb-2"
        >
          <input
            type="checkbox"
            :value="status.statusName"
            v-model="localSelectedStatuses"
            @change="checkboxChanged"
            class="form-checkbox mr-2 h-4 w-4 text-indigo-600 transition duration-150 ease-in-out"
          />
          <label class="text-sm">{{ status.statusName }}</label>
        </div>
      </div>
      <!-- Display message if no checkbox is checked -->
      <div
        v-if="localSelectedStatuses.length === 0 && !selectAll"
        class="text-red-600 mb-2"
      >
        You must select at least one.
      </div>
      <div class="modal-buttons flex justify-end mt-4">
        <button
          @click="applyFilter"
          :disabled="localSelectedStatuses.length === 0 && !selectAll"
          :class="{
            'bg-gray-400 cursor-not-allowed':
              localSelectedStatuses.length === 0 && !selectAll,
            'bg-green-500 cursor-pointer':
              localSelectedStatuses.length > 0 || selectAll
          }"
          class="apply-button px-4 py-2 text-white rounded-md"
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
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  statuses: Array,
  selectedStatuses: Array
})

const emit = defineEmits(['applyFilter', 'close'])

const localSelectedStatuses = ref([...props.selectedStatuses])
const selectAll = ref(false)

const applyFilter = () => {
  emit('applyFilter', localSelectedStatuses.value)
}

// Method to handle click on "Select All" checkbox
const selectAllChanged = () => {
  if (selectAll.value) {
    localSelectedStatuses.value = props.statuses.map(
      (status) => status.statusName
    )
  } else {
    localSelectedStatuses.value = []
  }
}

// Method to handle click on individual checkboxes
const checkboxChanged = () => {
  if (localSelectedStatuses.value.length === props.statuses.length) {
    selectAll.value = true
  } else {
    selectAll.value = false
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

.apply-button,
.cancel-button {
  cursor: pointer;
}
</style>
