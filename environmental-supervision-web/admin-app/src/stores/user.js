import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUser } from '../api/user'
export const useUserStore = defineStore('user',()=> {
    const userMessage=ref(null)
    const userId=localStorage.getItem('userId')
    const fetchUser=async()=>{
      const {data} =await getUser(userId)
      userMessage.value=data.data
    }
    return {fetchUser,userMessage}
})
