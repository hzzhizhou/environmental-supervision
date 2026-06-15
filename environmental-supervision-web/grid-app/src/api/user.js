import instance from "../untils/request";
export const getUser=(userId)=>instance.get(`/profile/info/${userId}`)
export const updateUser=(data)=>instance.post('/profile/update',data)
export const updatePassword=(data)=>instance.post('/profile/change-password',data)
export const getRank=(params)=>instance.get('/grid-worker/ranking',{params:params})
export const updateAvatar=(params)=>instance.post('/profile/upload-avatar',{params:params})
