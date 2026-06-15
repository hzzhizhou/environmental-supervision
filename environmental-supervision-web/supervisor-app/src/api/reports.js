import instance from "../utils/request"
//表单提交
export const postReports=(data)=>instance.post('/supervisor/reports',data)
//获得省份
export const getProvinces=()=>instance.get('/supervisor/regions/provinces')
//获取城市
export const getCities=(provinceId)=>instance.get('/supervisor/regions/cities',{ params:{ provinceId } })
//上传图片
export const uploadPhoto=(data)=>instance.post('/supervisor/reports/upload-photo',data)
//得到report详情
export const getReportDetail=(reportId)=>instance.get(`/supervisor/reports/${reportId}`)