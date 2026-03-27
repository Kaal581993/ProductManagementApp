import axios from 'axios'
import { API_BASE_URL } from '../constants'

export const http = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const setAuthToken = (token: string | null) => {
  if (token) {
    http.defaults.headers.common.Authorization = `Bearer ${token}`
    return
  }

  delete http.defaults.headers.common.Authorization
}
