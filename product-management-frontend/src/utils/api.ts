import axios from 'axios'
import type { ApiErrorResponse } from '../types'

export const getApiErrorMessage = (error: unknown): string => {
  if (axios.isAxiosError<ApiErrorResponse>(error)) {
    const data = error.response?.data

    if (data?.validationErrors) {
      const messages = Object.values(data.validationErrors)

      if (messages.length > 0) {
        return messages.join(', ')
      }
    }

    return data?.message ?? error.message ?? 'Request failed'
  }

  if (error instanceof Error) {
    return error.message
  }

  return 'Something went wrong'
}
