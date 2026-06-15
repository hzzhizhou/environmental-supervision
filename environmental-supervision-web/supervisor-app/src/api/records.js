import instance from "../utils/request";
export const getRecords=(params)=>instance.get('/supervisor/reports',{ params })