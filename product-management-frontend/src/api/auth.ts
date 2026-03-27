import { http } from './http'
import type {
  AuthResponse,
  LoginPayload,
  RegisterPayload,
  UserProfile,
} from '../types'

const login = async (payload: LoginPayload) => {
  const response = await http.post<AuthResponse>('/api/auth/login', payload)
  return response.data
}

const register = async (payload: RegisterPayload) => {
  const response = await http.post<AuthResponse>('/api/auth/register', payload)
  return response.data
}

const getCurrentUser = async () => {
  const response = await http.get<UserProfile>('/api/users/me')
  return response.data
}

export { login, register, getCurrentUser }