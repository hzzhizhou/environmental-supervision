import { defineStore } from 'pinia'
import { getUser,userPoint,pointLog,rankData } from '../api/user'
import { ref } from 'vue'

export  const useAlertsStore = defineStore('users',()=>{
    const userId=ref(localStorage.getItem('userId')||'')
    const userInfo=ref(null)
    const point=ref(null)
    const pointL=ref(null)
    const rankD=ref(null)
    const avatar=ref(null)
    const fetchUser=async()=>{
        const {data}=await getUser(userId.value)
        const {data:userP}=await userPoint(userId.value)
        const {data:mypointLog}=await pointLog(userId.value)
        const {data:rank}=await rankData()
        userInfo.value=data.data
        point.value=userP.data.totalPoints
        pointL.value=mypointLog
        rankD.value=rank.data 
        avatar.value=window.$img(data.data.avatarUrl)
    }
       return {fetchUser,userInfo,userId,point,pointL,rankD,avatar}
})

