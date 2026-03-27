export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export const AUTH_STORAGE_KEY = 'pm-auth-session'

export const DEPARTMENTS = [
  'SALES',
  'MARKETING',
  'OPERATIONS',
  'FINANCE',
  'HR',
  'ENGINEERING',
] as const

export const REGIONS = ['APAC', 'EMEA', 'NA', 'LATAM'] as const
