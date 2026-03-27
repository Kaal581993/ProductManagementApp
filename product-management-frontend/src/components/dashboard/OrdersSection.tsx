import ReceiptLongRoundedIcon from '@mui/icons-material/ReceiptLongRounded'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Chip,
  Stack,
  Typography,
} from '@mui/material'
import type { Order } from '../../types'

interface OrdersSectionProps {
  orders: Order[]
  isAdmin: boolean
  onRefresh: () => void
}

 const OrdersSection = ({
  orders,
  isAdmin,
  onRefresh,
}: OrdersSectionProps) => {
  return (
    <Card className="panel-card">
      <CardContent>
        <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
          <Stack direction="row" spacing={1} alignItems="center">
            <ReceiptLongRoundedIcon color="secondary" />
            <Typography variant="h5" fontWeight={700}>
              {isAdmin ? 'View orders (admin)' : 'My orders'}
            </Typography>
          </Stack>
          <Button onClick={onRefresh}>Refresh</Button>
        </Stack>
        <Stack spacing={1.5}>
          {orders.length ? (
            orders.map((order) => (
              <Card key={order.orderId} variant="outlined">
                <CardContent>
                  <Stack spacing={1.5}>
                    <Stack
                      direction="row"
                      justifyContent="space-between"
                      alignItems="center"
                      flexWrap="wrap"
                    >
                      <Typography fontWeight={700}>Order #{order.orderId}</Typography>
                      <Chip label={order.status} color="secondary" />
                    </Stack>
                    <Typography variant="body2" color="text.secondary">
                      {new Date(order.createdAt).toLocaleString()} · Total: Rs {order.totalAmount}
                    </Typography>
                    {isAdmin ? (
                      <Typography variant="body2" color="text.secondary">
                        Customer: {order.customerName ?? 'Unknown'} · ID: {order.userId}
                      </Typography>
                    ) : null}
                    <Stack spacing={0.5}>
                      {order.items.map((item) => (
                        <Typography key={item.orderItemId} variant="body2">
                          {item.productName} x {item.quantity} = Rs {item.lineTotal}
                        </Typography>
                      ))}
                    </Stack>
                  </Stack>
                </CardContent>
              </Card>
            ))
          ) : (
            <Alert severity="info">
              {isAdmin
                ? 'No placed orders are available yet.'
                : 'No orders yet. Place your first order from the cart.'}
            </Alert>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}


export default OrdersSection