import instance from "../untils/request";
export const getTasks=(params)=>instance.get('/grid-worker/tasks',{params:params})
export const acceptTask=(taskId)=>instance.put(`/grid-worker/tasks/${taskId}/accept`)
//提交检测数据
export const postTask=(data)=>instance.post('/grid-worker/detect-data',data)
//获取检测数据列表
export const getHasDone=(params)=>instance.get('/grid-worker/detect-data',{params:params})