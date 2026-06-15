import { defineStore } from "pinia";
import { getProvinces,getCities } from "../api/reports";
import { ref } from 'vue'
export const useReportStore=defineStore('report',()=>{
    const provinces=ref(null)
    const cities=ref(null)
    const refresh=async()=>{
        const {data}=await getProvinces()
        provinces.value=data.data
    }
    const refreshCity=async(provinceName)=>{
        const province=provinces.value.find(item=>item.provinceName===provinceName)
        const {data}=await getCities(province.provinceId)
        cities.value=data.data
        
    }
    return {refresh,provinces,refreshCity,cities}
})