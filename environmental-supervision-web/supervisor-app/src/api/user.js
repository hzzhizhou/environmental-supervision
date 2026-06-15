import instance from "../utils/request";

export const getUser=(userId)=>instance.get(`/profile/info/${userId}`)
export const updateUser=(data)=>instance.post('/profile/update',data)
export const updatePassword=(password)=>instance.post('/profile/change-password',password)
export const userPoint=(userId)=>instance.get(`/supervisor/points/${userId}`)
export const pointLog=(userId)=>instance.get(`supervisor/points/${userId}/logs`)
export const rankData=()=>instance.get(`/supervisor/ranking`)
export const updateAvatar=(formDate)=>instance.post(`/profile/upload-avatar`,formDate,{ 
    headers: {
        'Content-Type': 'multipart/form-data'
    }})