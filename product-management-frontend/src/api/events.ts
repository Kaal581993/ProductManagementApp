import { http } from './http'
import type { StoredEvent } from '../types'

export const getEvents = async () => {
  const response = await http.get<StoredEvent[]>('/api/events')
  return response.data
}
