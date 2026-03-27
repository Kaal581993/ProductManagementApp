import { http } from './http'
import type { Order } from '../types'

export const placeOrder = async () => {
  const response = await http.post<Order>('/api/orders')
  return response.data
}

export const getOrders = async () => {
  const response = await http.get<Order[]>('/api/orders')
  return response.data
}
