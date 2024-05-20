import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
import TaskList from './components/TaskList.vue';
import StatusList from './v2/StatusList.vue';

const routes = [
  {
    path: '/task',
    name: "taskView",
    component: TaskList,
  },
  {
    path: '/task/:taskId',
    name: "taskDetail",
    component: TaskList,
  },
  {
    path: '/status',
    name: "statusView",
    component: StatusList,
  },
  {
    path: '/status/:statusId',
    name: "statusDetail",
    component: StatusList,
  },
  {
    path: '/',
    redirect: '/task',
    component: App,
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/task', 
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;