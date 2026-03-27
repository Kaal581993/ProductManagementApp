import { CssBaseline, ThemeProvider, createTheme } from '@mui/material'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import { AuthProvider } from './auth/AuthContext'
import { ProtectedRoute } from './components/ProtectedRoute'
import { DashboardPage } from './pages/DashboardPage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#c2410cff',
    },
    secondary: {
      main: '#c2410c',
    },
    background: {
      default: '#f7f4ed',
      paper: '#fffdf8',
    },
  },
  shape: {
    borderRadius: 20,
  },
  typography: {
    fontFamily:
      '"Poppins", "Segoe UI", "Helvetica Neue", Arial, sans-serif',
    h2: {
      fontWeight: 800,
      lineHeight: 1,
    },
  },
})

const App = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<DashboardPage />} />
            </Route>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  )
}

export default App
