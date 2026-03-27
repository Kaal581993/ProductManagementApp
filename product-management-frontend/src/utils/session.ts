import { AUTH_STORAGE_KEY } from '../constants'
import type { AuthResponse } from '../types'

export const loadSession = (): AuthResponse | null => {
  const raw = window.localStorage.getItem(AUTH_STORAGE_KEY)

  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw) as AuthResponse
  } catch {
    window.localStorage.removeItem(AUTH_STORAGE_KEY)
    return null
  }
}

export const saveSession = (session: AuthResponse) => {
  window.localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(session))
}

export const clearSession = () => {
  window.localStorage.removeItem(AUTH_STORAGE_KEY)
}
