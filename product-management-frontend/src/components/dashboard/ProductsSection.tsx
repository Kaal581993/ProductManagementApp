import ShoppingCartRoundedIcon from '@mui/icons-material/ShoppingCartRounded'
import TuneRoundedIcon from '@mui/icons-material/TuneRounded'
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  IconButton,
  Stack,
  Switch,
  Typography,
} from '@mui/material'
import type { Product } from '../../types'

interface ProductsSectionProps {
  products: Product[]
  isAdmin: boolean
  isLoading: boolean
  onRefresh: () => void
  onAddToCart: (productId: number) => void
  onEdit: (product: Product) => void
  onToggleStatus: (product: Product) => void
}

const ProductsSection = ({
  products,
  isAdmin,
  isLoading,
  onRefresh,
  onAddToCart,
  onEdit,
  onToggleStatus,
}: ProductsSectionProps) => {
  return (
    <Card className="panel-card">
      <CardContent>
        <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
          <Box>
            <Typography variant="h5" fontWeight={700}>
              Product catalog
            </Typography>
            <Typography color="text.secondary">
              {isLoading
                ? 'Loading current inventory from the product service.'
                : 'Live data from your backend product API.'}
            </Typography>
          </Box>
          <IconButton onClick={onRefresh} color="secondary">
            <TuneRoundedIcon />
          </IconButton>
        </Stack>

        <Box className="product-grid">
          {products.map((product) => (
            <Card key={product.id} className="product-card" variant="outlined">
              <CardContent>
                <Stack spacing={2}>
                  <Stack
                    direction="row"
                    justifyContent="space-between"
                    alignItems="flex-start"
                    spacing={1.5}
                  >
                    <Box>
                      <Typography variant="h6" fontWeight={700}>
                        {product.name}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {product.description || 'No description provided.'}
                      </Typography>
                    </Box>
                    <Chip
                      label={product.enabled ? 'Active' : 'Disabled'}
                      color={product.enabled ? 'success' : 'default'}
                      size="small"
                    />
                  </Stack>

                  <Stack direction="row" spacing={1.5} flexWrap="wrap">
                    <Chip label={`Price: Rs ${product.price}`} variant="outlined" />
                    <Chip label={`Qty: ${product.quantity}`} variant="outlined" />
                    <Chip label={`ID: ${product.id}`} variant="outlined" />
                  </Stack>

                  {isAdmin ? (
                    <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
                      <Button variant="contained" onClick={() => onEdit(product)}>
                        Edit details
                      </Button>
                      <Stack direction="row" alignItems="center" spacing={1}>
                        <Switch
                          checked={product.enabled}
                          onChange={() => onToggleStatus(product)}
                        />
                        <Typography variant="body2">
                          {product.enabled ? 'Enabled' : 'Disabled'}
                        </Typography>
                      </Stack>
                    </Stack>
                  ) : (
                    <Button
                      variant="contained"
                      startIcon={<ShoppingCartRoundedIcon />}
                      onClick={() => onAddToCart(product.id)}
                      disabled={!product.enabled}
                    >
                      Add to cart
                    </Button>
                  )}
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Box>
      </CardContent>
    </Card>
  )
}


export default ProductsSection