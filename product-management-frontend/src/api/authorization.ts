import { http } from './http'
import type {
  AuthorizationRequestPayload,
  AuthorizationResult,
} from '../types'

export const checkAuthorization = async (payload: AuthorizationRequestPayload) => {
  const response = await http.post<AuthorizationResult>(
    '/api/authorization/check',
    payload,
  )
  return response.data
}
