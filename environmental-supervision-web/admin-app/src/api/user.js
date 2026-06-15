import instance from '../utils/request'
export const getUser=(userId)=>instance.get(`/profile/info/${userId}`)