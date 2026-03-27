import ShoppingCartRoundedIcon from '@mui/icons-material/ShoppingCartRounded'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Divider,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { Cart } from '../../types'

interface CartSectionProps {
  cart: Cart | null
  isLoading: boolean
  isPlacingOrder: boolean
  onRefresh: () => void
  onQuantityChange: (cartItemId: number, quantity: number) => void
  onRemoveItem: (cartItemId: number) => void
  onPlaceOrder: () => void
}

 const CartSection = ({
  cart,
  isLoading,
  isPlacingOrder,
  onRefresh,
  onQuantityChange,
  onRemoveItem,
  onPlaceOrder,
}: CartSectionProps) => {
  return (
    <Card className="panel-card">
      <CardContent>
        <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
          <Stack direction="row" spacing={1} alignItems="center">
            <ShoppingCartRoundedIcon color="secondary" />
            <Typography variant="h5" fontWeight={700}>
              Cart
            </Typography>
          </Stack>
          <Button onClick={onRefresh}>Refresh</Button>
        </Stack>
        <Typography color="text.secondary" mb={2}>
          {isLoading
            ? 'Loading your cart and order history.'
            : 'Adjust quantities, remove items, or place an order.'}
        </Typography>
        <Stack spacing={1.5}>
          {cart?.items.length ? (
            cart.items.map((item) => (
              <Card key={item.cartItemId} variant="outlined">
                <CardContent>
                  <Stack spacing={1.5}>
                    <Typography fontWeight={700}>{item.productName}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      Unit price: Rs {item.unitPrice} · Line total: Rs {item.lineTotal}
                    </Typography>
                    <Stack
                      direction={{ xs: 'column', sm: 'row' }}
                      spacing={1.5}
                      alignItems={{ sm: 'center' }}
                    >
                      <TextField
                        label="Quantity"
                        type="number"
                        size="small"
                        value={item.quantity}
                        slotProps={{ htmlInput: { min: 1 } }}
                        onChange={(event) =>
                          onQuantityChange(item.cartItemId, Number(event.target.value))
                        }
                        sx={{ maxWidth: 140 }}
                      />
                      <Button
                        color="error"
                        variant="outlined"
                        onClick={() => onRemoveItem(item.cartItemId)}
                      >
                        Remove
                      </Button>
                    </Stack>
                  </Stack>
                </CardContent>
              </Card>
            ))
          ) : (
            <Alert severity="info">Your cart is empty. Add an active product to start.</Alert>
          )}
          <Divider />
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography fontWeight={700}>Total: Rs {cart?.totalAmount ?? 0}</Typography>
            <Button
              variant="contained"
              disabled={!cart?.items.length || isPlacingOrder}
              onClick={onPlaceOrder}
            >
              {isPlacingOrder ? 'Placing...' : 'Place order'}
            </Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}


export default CartSection