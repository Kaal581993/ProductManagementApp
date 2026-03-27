import AddCircleOutlineRoundedIcon from '@mui/icons-material/AddCircleOutlineRounded'
import ShoppingCartRoundedIcon from '@mui/icons-material/ShoppingCartRounded'
import StorefrontRoundedIcon from '@mui/icons-material/StorefrontRounded'
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Stack,
  Typography,
} from '@mui/material'

interface DashboardHeroProps {
  isAdmin: boolean
  userName: string
  productsCount: number
  eventsCount: number
  cartItemsCount: number
  isPlacingOrder: boolean
  onAddProduct: () => void
  onPlaceOrder: () => void
}


 const DashboardHero = ({
  isAdmin,
  userName,
  productsCount,
  eventsCount,
  cartItemsCount,
  isPlacingOrder,
  onAddProduct,
  onPlaceOrder,
}: DashboardHeroProps) => {
  return (
    <Card className="panel-card panel-card--hero">
      <CardContent>
        <Stack spacing={2.5}>
          <Typography variant="overline" color="secondary.main">
            Welcome back
          </Typography>
          <Typography variant="h3" fontWeight={800}>
            {userName}
          </Typography>
          <Typography color="text.secondary">
            {isAdmin
              ? 'You can manage inventory and inspect platform-wide events.'
              : 'You can browse products, manage your cart, and place orders.'}
          </Typography>
          <Stack direction="row" spacing={1.5} flexWrap="wrap">
            <Chip label={`${productsCount} products synced`} />
            <Chip label={`${eventsCount} events loaded`} />
            {!isAdmin ? <Chip label={`${cartItemsCount} items in cart`} /> : null}
          </Stack>
          {isAdmin ? (
            <Button
              variant="contained"
              startIcon={<AddCircleOutlineRoundedIcon />}
              onClick={onAddProduct}
            >
              Add product
            </Button>
          ) : (
            <Button
              variant="contained"
              startIcon={<ShoppingCartRoundedIcon />}
              disabled={!cartItemsCount || isPlacingOrder}
              onClick={onPlaceOrder}
            >
              {isPlacingOrder ? 'Placing order...' : 'Place current order'}
            </Button>
          )}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <StorefrontRoundedIcon fontSize="small" />
            <Typography variant="body2" color="text.secondary">
              Products, cart, orders, events, and authorization in one flow
            </Typography>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}


export default DashboardHero