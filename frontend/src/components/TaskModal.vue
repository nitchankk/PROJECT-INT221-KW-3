<template>
  <div class="modal-wrapper">
    <div class="modal">
      <div class="modal-content">
        <!-- Modal content -->

        <h2 class="modal-title">{{ task.title }}</h2>

        <div class="grid-container">
          <!-- Description box -->
          <div class="description-box grid-item">
            <strong>Description</strong>
            <p class="itbkk-description">
              <span v-if="!task.description" class="no-data"
                >No Description Provided</span
              >
              <span v-else>{{ task.description }}</span>
            </p>
          </div>

          <!-- Timezone, Created Date, and Updated Date -->
          <div class="timezone-created-updated-box grid-item">
            <div class="timezone-box">
              <strong>Timezone</strong>
              <p class="timezone itbkk-timezone">{{ timezone }}</p>
            </div>

            <div class="created-date-box">
              <strong>Created Date</strong>
              <p class="itbkk-created-on">{{ createdDate }}</p>
            </div>

            <div class="updated-date-box">
              <strong>Updated Date</strong>
              <p class="itbkk-updated-on">{{ updatedDate }}</p>
            </div>
          </div>
        </div>

        <!-- Assignees and Status box -->
        <div class="assignees-status-container">
          <div class="assignees-box">
            <strong>Assignees</strong>
            <p class="itbkk-assignees">
              <template v-if="task.assignees">{{ task.assignees }}</template>
              <template v-else><i class="no-data">Unassigned</i></template>
            </p>
          </div>

          <div class="status-box">
            <strong>Status</strong>
            <p class="itbkk-status">
              <template v-if="task.status.name">{{ task.status.name }}</template>
              <template v-else><i class="no-data">Unassigned</i></template>
            </p>
          </div>
        </div>

        <div class="modal-buttons">
          <button class="itbkk-button button-done" @click="closeModal">
            Done
          </button>
          <button class="itbkk-button button-close" @click="closeModal">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    task: {
      type: Object,
      required: true
    },
    timezone: {
      type: String,
      required: true
    },
    createdDate: {
      type: String,
      required: true
    },
    updatedDate: {
      type: String,
      required: true
    },
    closeModal: {
      type: Function,
      required: true
    }
  }
}
</script>

<style scoped>
.modal-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
  z-index: 1000;
  backdrop-filter: blur(8px); /* Apply blur effect */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  position: relative;
  background-color: #fff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  width: 900px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content {
  padding: 30px;
}

.close {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  color: #333;
  cursor: pointer;
}

h2 {
  margin-top: 0;
  font-size: 24px;
  font-weight: bold;
}

p {
  margin-bottom: 15px;
  font-size: 16px;
}

strong {
  font-weight: bold;
}

i {
  font-style: italic;
}

.no-data {
  font-style: italic;
  color: #888;
}

.timezone {
  font-size: 14px;
  color: #888;
  margin-bottom: 15px;
}

.description-box {
  width: 500px;
  height: 200px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  word-wrap: break-word;
  text-align: left;
}

.timezone-created-updated-box {
  text-align: left;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 200px;
  padding: 15px;
}

.assignees-box,
.status-box {
  
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  word-wrap: break-word;
  text-align: left;
}

.assignees-box {
  margin-right: 10px;
  width: calc(50% - 10px);
}

.status-box {
  margin-left: 10px;
  width: calc(50% - 10px);
}
.assignees-status-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.modal-title {
  max-width: 800px;
  word-wrap: break-word;
  text-align: left;
}

.grid-container {
  display: flex;
  justify-content: space-between;
}

.grid-item {
  margin-bottom: 20px;
}

.modal-buttons {
  position: absolute;
  bottom: 10px;
  right: 30px;
}

.itbkk-button {
  padding: 10px 20px;
  margin-left: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.button-done {
  background-color: #68d391;
  color: #fff;
}

.button-close {
  background-color: #f67c5e;
  color: #fff;
}

.button-done:hover,
.button-close:hover {
  opacity: 0.8;
}
</style>
