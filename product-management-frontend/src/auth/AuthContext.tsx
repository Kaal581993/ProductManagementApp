import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import * as authApi from '../api/auth'
import { setAuthToken } from '../api/http'
import type {
  AuthResponse,
  LoginPayload,
  RegisterPayload,
  UserProfile,
} from '../types'
import { clearSession, loadSession, saveSession } from '../utils/session'

interface AuthContextValue {
  session: AuthResponse | null
  user: UserProfile | null
  isAuthenticated: boolean
  isAdmin: boolean
  isBootstrapping: boolean
  login: (payload: LoginPayload) => Promise<void>
  register: (payload: RegisterPayload) => Promise<void>
  logout: () => void
  refreshUser: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

function toUserProfile(session: AuthResponse): UserProfile {
  return {
    id: session.userId,
    name: session.name,
    email: session.email,
    role: session.role,
    department: session.department,
    region: session.region,
  }
}

 const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [session, setSession] = useState<AuthResponse | null>(() => loadSession())
  const [user, setUser] = useState<UserProfile | null>(() => {
    const savedSession = loadSession()
    return savedSession ? toUserProfile(savedSession) : null
  })
  const [isBootstrapping, setIsBootstrapping] = useState(true)

  useEffect(() => {
    if (!session?.token) {
      setAuthToken(null)
      setIsBootstrapping(false)
      return
    }

    setAuthToken(session.token)
    void authApi
      .getCurrentUser()
      .then((profile) => {
        setUser(profile)
      })
      .catch(() => {
        clearSession()
        setSession(null)
        setUser(null)
        setAuthToken(null)
      })
      .finally(() => {
        setIsBootstrapping(false)
      })
  }, [session])

  const handleAuthSuccess = (nextSession: AuthResponse) => {
    saveSession(nextSession)
    setSession(nextSession)
    setUser(toUserProfile(nextSession))
    setAuthToken(nextSession.token)
  }

  const value = useMemo<AuthContextValue>(
    () => ({
      session,
      user,
      isAuthenticated: Boolean(session?.token),
      isAdmin: user?.role === 'ROLE_ADMIN' || session?.role === 'ROLE_ADMIN',
      isBootstrapping,
      async login(payload) {
        const nextSession = await authApi.login(payload)
        handleAuthSuccess(nextSession)
      },
      async register(payload) {
        const nextSession = await authApi.register(payload)
        handleAuthSuccess(nextSession)
      },
      logout() {
        clearSession()
        setSession(null)
        setUser(null)
        setAuthToken(null)
      },
      async refreshUser() {
        const profile = await authApi.getCurrentUser()
        setUser(profile)
      },
    }),
    [isBootstrapping, session, user],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

 const useAuth = () => {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider')
  }

  return context
}

export { AuthProvider, useAuth }
