import { defineStore } from "pinia";
import { getUsers } from "../api/manageUser";
import { ref } from 'vue'
export const useUsersStore=defineStore('users',()=>{
    const users=ref(null)
    const fetchUsers=async()=>{
        const {data} =await getUsers()
        users.value=data.data
        console.log(data);
    }
    return { fetchUsers,users }
})