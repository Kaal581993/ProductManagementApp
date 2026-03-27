import { Alert, Button, Link, Stack, TextField, Typography } from '@mui/material'
import { useState, type FormEvent } from 'react'
import {
  Link as RouterLink,
  Navigate,
  useLocation,
  useNavigate,
} from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import  AuthShell  from '../layouts/AuthShell'
import { getApiErrorMessage } from '../utils/api'

export const LoginPage = () => {
  const { isAuthenticated, login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [email, setEmail] = useState('admin@example.com')
  const [password, setPassword] = useState('Admin@123')
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  if (isAuthenticated) {
    return <Navigate to="/" replace />
  }

  const from = (location.state as { from?: { pathname?: string } } | null)?.from
    ?.pathname

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)
    setIsSubmitting(true)

    try {
      await login({ email, password })
      navigate(from ?? '/', { replace: true })
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthShell
      title="Login"
      subtitle="Use the account returned by your backend auth service to continue."
    >
      <Stack component="form" spacing={2} onSubmit={handleSubmit}>
        {error ? <Alert severity="error">{error}</Alert> : null}
        <TextField
          label="Email"
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
          fullWidth
          
        />
        <TextField
          label="Password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          required
          fullWidth
          
        />
        <Button disabled={isSubmitting} type="submit" variant="contained" size="large">
          {isSubmitting ? 'Signing in...' : 'Sign in'}
        </Button>
        <Typography color="text.secondary" textAlign="center">
          New to the platform?{' '}
          <Link component={RouterLink} to="/register" underline="hover">
            Create an account
          </Link>
        </Typography>
      </Stack>
    </AuthShell>
  )
}
