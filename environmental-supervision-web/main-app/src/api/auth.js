import instance from '../untils/request'
export const login=(data)=>instance.post('/auth/login',data)
export const register=(data)=>instance.post('/auth/register',data)