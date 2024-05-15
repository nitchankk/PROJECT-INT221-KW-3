<template>
  <div class="modal-wrapper">
    <div class="modal">
      <div class="modal-content">
        <h2 class="modal-title">Add Task</h2>

        <form @submit.prevent="handleSaveTask">
          <div class="form-group">
            <label for="title">Title:</label>
            <input
              type="text"
              id="title"
              v-model="taskDetails.title"
              class="itbkk-title"
              required
            />
          </div>
          <div class="form-group">
            <label for="description">Description:</label>
            <textarea
              id="description"
              v-model="taskDetails.description"
              class="itbkk-description"
            ></textarea>
          </div>
          <div class="form-group">
            <label for="assignees">Assignees:</label>
            <input
              type="text"
              id="assignees"
              v-model="taskDetails.assignees"
              class="itbkk-assignees"
            />
          </div>
          <div class="form-group">
            <label for="status">Status:</label>
            <select
              id="status"
              v-model="taskDetails.statusName"
              class="itbkk-status"
            >
              <option v-if="statuses.length === 0" value="" disabled>
                Loading...
              </option>
              <option
                v-else
                v-for="status in statuses"
                :key="status.statusId"
                :value="status.statusName"
              >
                {{ status.statusName }}
              </option>
            </select>
          </div>

          <div class="modal-buttons">
            <button
              class="itbkk-button-confirm itbkk-button"
              type="submit"
              :class="{ disabled: isSaveDisabled }"
              :disabled="isSaveDisabled"
            >
              Save
            </button>
            <button
              class="itbkk-button-cancel itbkk-button"
              type="button"
              @click="cancelModal"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import FetchUtils from '../lib/fetchUtils'

export default {
  props: {
    closeModal: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      taskDetails: {
        title: '',
        description: '',
        assignees: '',
        statusName: 'NO STATUS'
      },
      statuses: []
    }
  },
  computed: {
    isSaveDisabled() {
      return !this.taskDetails.title.trim()
    }
  },
  methods: {
    async handleSaveTask() {
      try {
        const { success, data, statusCode } = await FetchUtils.postData(
          'tasks',
          this.taskDetails
        )
        if (success && statusCode === 200) {
          console.log('The task has been successfully added', statusCode)
          this.$emit('taskSaved', data)
          this.$emit('showStatusModal', statusCode)
          this.taskDetails = {
            title: '',
            description: '',
            assignees: '',
            status: 'No Status'
          }
          this.closeModal()
        } else {
          console.error('Something went wrong while adding the task')
        }
      } catch (error) {
        console.error('Error saving task:', error)
      }
    },
    cancelModal() {
      this.closeModal()
    }
  },
  async created() {
    try {
      const data = await FetchUtils.fetchData('statuses')
      this.statuses = data
    } catch (error) {
      console.error('Error fetching statuses:', error)
    }
  }
}
</script>

<!-- Existing styles -->
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
.disabled {
  background-color: gray; /* เปลี่ยนสีตามความเหมาะสม */
  cursor: not-allowed;
}
</style>
