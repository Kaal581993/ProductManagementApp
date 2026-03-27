import {
  Box,
  Chip,
  Container,
  Paper,
  Stack,
  Typography,
} from '@mui/material'
import type { ReactNode } from 'react'

export const AuthShell = ({
  title,
  subtitle,
  children,
}: {
  title: string
  subtitle: string
  children: ReactNode
}) => {
  return (
    <Box className="auth-page">
      <Container maxWidth="lg">
        <Box className="auth-grid">
          <Box className="auth-copy">
            <Chip label="Buy and Sell your products online" color="secondary" sx={
              {
                margin:'20px',
                padding:'10px 20px',
                fontWeight: 'bold',
                fontSize: '1.1rem',
              }
            } />


            <Stack direction={{ xs: 'column', sm: 'column' }} spacing={1.5}>
              <Chip label="Your one solution for online ordering system" variant="outlined" sx={{margin:'20px', width: '50%'}}/>
              <Chip label="Buy today, pay tomorrow" variant="outlined" sx={{margin:'20px', width: '50%'}}/>
              <Chip label="Secure payment processing" variant="outlined" sx={{margin:'20px', width: '50%'}}/>
            </Stack>
          </Box>

          <Paper elevation={0} className="auth-card">
            <Stack spacing={1.5} sx={{ mb: 3 }}>
              <Typography variant="h4" fontWeight={700}>
                {title}
              </Typography>
              <Typography color="text.secondary">{subtitle}</Typography>
            </Stack>
            {children}
          </Paper>
        </Box>
      </Container>
    </Box>
  )
}

export default AuthShell
