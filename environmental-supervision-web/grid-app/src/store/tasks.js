import { defineStore } from 'pinia'
import { getTasks } from '../api/tasks'
import { ref } from 'vue'
export const useTasksStore = defineStore('tasks', () => {
    const allTasks = ref(null)
    const fetchTasks=async()=>{
        const {data}=await getTasks()
        
        allTasks.value=data.data
    }
    return {fetchTasks,allTasks}
})