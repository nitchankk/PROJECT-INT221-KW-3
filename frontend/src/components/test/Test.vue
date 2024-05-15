<script setup>
import { ref } from 'vue'
import ModalComponent from './ModalComponent.vue'

const isModalOpened = ref(false)

const openModal = () => {
  isModalOpened.value = true
}
const closeModal = () => {
  isModalOpened.value = false
}

</script>

<template>
  <div>
    <button @click="openModal">Open generic modal</button>
  </div>
  <ModalComponent
    :isOpen="isModalOpened"
    @modal-close="closeModal"
    name="first-modal"
  >
    <template #header>Custom header</template>
    <template #content>Custom content</template>
    <template #footer>Custom content</template>
  </ModalComponent>
</template>
