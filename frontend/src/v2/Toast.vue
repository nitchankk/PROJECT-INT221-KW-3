<template>
  <transition name="slide-fade">
    <div
      v-if="show"
      class="toast fixed top-10 left-10 px-6 py-3 rounded-md flex items-center"
      :class="{
        'bg-green-400 text-white': operationType === 'add',
        'bg-blue-400 text-white':
          statusCode === 200 && operationType === 'edit',
        'bg-red-500 text-white':
          statusCode === 200 && operationType === 'delete',
        'bg-yellow-500 text-white':
          statusCode === 200 && operationType === 'transfer',

        'bg-orange-500 text-white': statusCode === 404 || statusCode === 500
      }"
    >
      <span v-if="operationType === 'add'">The status has been added</span>
      <span v-if="statusCode === 200 && operationType === 'edit'"
        >The status has been updated</span
      >
      <span v-if="statusCode === 200 && operationType === 'delete'"
        >The status has been deleted</span
      >
      <span v-if="statusCode === 200 && operationType === 'transfer'"
        >The status is transfered</span
      >
      <span v-if="statusCode === 500 && operationType === 'delete'"
        >The status is being use by task</span
      >
      <span v-else-if="statusCode === 404">The status does not exist</span>
      <button
        class="ml-4 bg-transparent text-white cursor-pointer"
        @click="closeToast"
      >
        &times;
      </button>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    show: {
      type: Boolean,
      required: true
    },
    statusCode: {
      type: Number,
      required: true
    },
    operationType: {
      type: String,
      default: null
    }
  },
  methods: {
    closeToast() {
      this.$emit('close')
    }
  },
  mounted() {
    if (this.show) {
      setTimeout(() => {
        this.closeToast()
      }, 3000)
    }
  },
  watch: {
    show(newVal) {
      if (newVal) {
        setTimeout(() => {
          this.closeToast()
        }, 3000)
      }
    }
  }
}
</script>

<style scoped>
.slide-fade-enter-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
}
.slide-fade-enter-from {
  transform: translateY(-100%);
  opacity: 0;
}
.slide-fade-enter-to {
  transform: translateY(0);
  opacity: 1;
}

.slide-fade-leave-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
}
.slide-fade-leave-from {
  transform: translateY(0);
  opacity: 1;
}
.slide-fade-leave-to {
  transform: translateY(-100%);
  opacity: 0;
}
</style>
