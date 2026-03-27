import {
  Alert,
  Button,
  Link,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useState, type FormEvent } from 'react'
import { Link as RouterLink, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { DEPARTMENTS, REGIONS } from '../constants'
import { AuthShell } from '../layouts/AuthShell'
import { getApiErrorMessage } from '../utils/api'

export const RegisterPage = () => {
  const { isAuthenticated, register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    department: DEPARTMENTS[0],
    region: REGIONS[0],
  })
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  if (isAuthenticated) {
    return <Navigate to="/" replace />
  }

  function updateField(name: string, value: string) {
    setForm((current) => ({
      ...current,
      [name]: value,
    }))
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)
    setIsSubmitting(true)

    try {
      await register(form)
      navigate('/', { replace: true })
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthShell
      title="Register"
      subtitle="Create a business user account. New registrations are assigned the user role by the backend."
    >
      <Stack component="form" spacing={2} onSubmit={handleSubmit}>
        {error ? <Alert severity="error">{error}</Alert> : null}
        <TextField
          label="Full name"
          value={form.name}
          onChange={(event) => updateField('name', event.target.value)}
          required
          fullWidth
        />
        <TextField
          label="Email"
          type="email"
          value={form.email}
          onChange={(event) => updateField('email', event.target.value)}
          required
          fullWidth
        />
        <TextField
          label="Password"
          type="password"
          helperText="Minimum 6 characters."
          value={form.password}
          onChange={(event) => updateField('password', event.target.value)}
          required
          fullWidth
        />
        <TextField
          select
          label="Department"
          value={form.department}
          onChange={(event) => updateField('department', event.target.value)}
          fullWidth
        >
          {DEPARTMENTS.map((department) => (
            <MenuItem key={department} value={department}>
              {department}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          select
          label="Region"
          value={form.region}
          onChange={(event) => updateField('region', event.target.value)}
          fullWidth
        >
          {REGIONS.map((region) => (
            <MenuItem key={region} value={region}>
              {region}
            </MenuItem>
          ))}
        </TextField>
        <Button disabled={isSubmitting} type="submit" variant="contained" size="large">
          {isSubmitting ? 'Creating account...' : 'Create account'}
        </Button>
        <Typography color="text.secondary" textAlign="center">
          Already have an account?{' '}
          <Link component={RouterLink} to="/login" underline="hover">
            Sign in
          </Link>
        </Typography>
      </Stack>
    </AuthShell>
  )
}
