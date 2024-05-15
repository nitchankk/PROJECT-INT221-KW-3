import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
import TaskList from './components/TaskList.vue';
import StatusList from './v2/StatusList.vue';

const routes = [
  {
    path: '/task',
    component: TaskList,
  },
  {
    path: '/task/:taskId',
    component: TaskList,
  },
  {
    path: '/status',
    component: StatusList,
  },
  {
    path: '/status/:statusId',
    component: StatusList,
  },
  {
    path: '/',
    redirect: '/task',
    component: App,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
