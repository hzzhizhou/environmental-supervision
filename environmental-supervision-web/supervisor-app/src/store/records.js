import { defineStore } from "pinia";
import { getRecords } from "../api/records";
import { ref } from 'vue'
export const useRecordsStore=defineStore('records',()=>{
    const reports=ref(null)
    const assignedReports=ref(null)
    const confirmedReports=ref(null)
    const pendingReports=ref(null)
    const detectedReports=ref(null)
    const fetchRecords=async()=>{
    const {data}=await getRecords({status:'assigned'})
    const {data:confirmed}=await getRecords({status:'confirmed'})
    const {data:pending}=await getRecords({status:'pending'})
    const {data:detected}=await getRecords({status:'detected'})
    const {data:all}=await getRecords()
    pendingReports.value=pending.data
    confirmedReports.value=confirmed.data
    assignedReports.value=data.data
    detectedReports.value=detected.data
    reports.value=all.data

    
    }
    return {fetchRecords,assignedReports,pendingReports,confirmedReports,detectedReports,reports}

})