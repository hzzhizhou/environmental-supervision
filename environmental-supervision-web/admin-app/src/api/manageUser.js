import instance from "../utils/request";
export const getUsers=(params)=>instance.get('/admin/users',{ params })
export const addUser=(data)=>instance.post('/admin/users',data)
export const updateUser=(userId,data)=>instance.put(`/admin/users/${userId}`,data)
export const deleteUser=(userId)=>instance.delete(`/admin/users/${userId}`)
export const listReports=(params)=>instance.get('/admin/reports',{ params })

export const getLocalGridWorkers=(reportId)=>instance.get('/admin/workers/local',{ params: { reportId } })
export const getCrossCityGridWorkers=(reportId)=>instance.get('/admin/workers/cross-city',{ params: { reportId } })
export const checkLocalGridWorkers=(reportId)=>instance.get('/admin/workers/local/check',{ params: { reportId } })
export const assignTask=(data)=>instance.post('/admin/tasks/assign',data)
