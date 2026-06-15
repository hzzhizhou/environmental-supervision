import { defineStore } from 'pinia'
import { getUser,getRank } from '../api/user'
import { ref } from 'vue'

export const useUserStore=defineStore('user',()=>{
    const userId=localStorage.getItem('userId')
    const user=ref(null)
    const rank=ref(null)
    const avatar=ref(null)
    const getUserMessage=async()=>{
        const {data}=await getUser(userId)        
        user.value=data.data
        avatar.value=window.$img(data.data.avatarUrl)
        const {allRank}=await getRank()
        rank.value=allRank.data
    }
    return {user,getUserMessage,avatar,rank,userId}
})