import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded'
import ShieldRoundedIcon from '@mui/icons-material/ShieldRounded'
import StorefrontRoundedIcon from '@mui/icons-material/StorefrontRounded'
import {
  Alert,
  AppBar,
  Box,
  Button,
  Chip,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  Stack,
  TextField,
  Toolbar,
  Typography,
} from '@mui/material'
import { useEffect, useState, type FormEvent } from 'react'
import { addCartItem, getCart, removeCartItem, updateCartItem } from '../api/cart'
import { getOrders, placeOrder } from '../api/orders'
import {
  createProduct,
  getProducts,
  updateProduct,
  updateProductStatus,
} from '../api/products'
import { useAuth } from '../auth/AuthContext'
import type { Cart, Order, Product } from '../types'
import { getApiErrorMessage } from '../utils/api'
import { getDisplayName } from '../utils/user'
import CartSection from '../components/dashboard/CartSection'
import DashboardHero from '../components/dashboard/DashboardHero'
import OrdersSection  from '../components/dashboard/OrdersSection'
import ProductsSection  from '../components/dashboard/ProductsSection'

const emptyProductForm = {
  name: '',
  description: '',
  price: '',
  quantity: '',
}

export const DashboardPage = () => {
  const { isAdmin, logout, user } = useAuth()
  const [products, setProducts] = useState<Product[]>([])
  const [cart, setCart] = useState<Cart | null>(null)
  const [orders, setOrders] = useState<Order[]>([])
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [isLoadingProducts, setIsLoadingProducts] = useState(true)
  const [isLoadingCommerce, setIsLoadingCommerce] = useState(false)
  const [isPlacingOrder, setIsPlacingOrder] = useState(false)
  const [isCreateOpen, setIsCreateOpen] = useState(false)
  const [isEditOpen, setIsEditOpen] = useState(false)
  const [form, setForm] = useState(emptyProductForm)
  const [editingProduct, setEditingProduct] = useState<Product | null>(null)

  async function loadProducts() {
    setIsLoadingProducts(true)

    try {
      const nextProducts = await getProducts()
      setProducts(nextProducts)
    } catch (loadError) {
      setError(getApiErrorMessage(loadError))
    } finally {
      setIsLoadingProducts(false)
    }
  }

  async function loadCommerce() {
    setIsLoadingCommerce(true)

    try {
      const [nextCart, nextOrders] = await Promise.all([
        isAdmin ? Promise.resolve(null) : getCart(),
        getOrders(),
      ])
      setCart(nextCart)
      setOrders(nextOrders)
    } catch (loadError) {
      setError(getApiErrorMessage(loadError))
    } finally {
      setIsLoadingCommerce(false)
    }
  }

  useEffect(() => {
    setError(null)
    void loadProducts()
    void loadCommerce()
  }, [isAdmin])

  function updateForm(name: string, value: string) {
    setForm((current) => ({
      ...current,
      [name]: value,
    }))
  }


  function resetMessages() {
    setError(null)
    setSuccess(null)
  }

  async function handleCreateSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    resetMessages()

    try {
      await createProduct({
        name: form.name,
        description: form.description,
        price: Number(form.price),
        quantity: Number(form.quantity),
      })
      setIsCreateOpen(false)
      setForm(emptyProductForm)
      setSuccess('Product created successfully.')
      await loadProducts()
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    }
  }

  async function handleEditSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (!editingProduct) {
      return
    }

    resetMessages()

    try {
      await updateProduct(editingProduct.id, {
        name: form.name,
        description: form.description,
        price: Number(form.price),
        quantity: Number(form.quantity),
        enabled: editingProduct.enabled,
      })
      setIsEditOpen(false)
      setEditingProduct(null)
      setForm(emptyProductForm)
      setSuccess('Product updated successfully.')
      await loadProducts()
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    }
  }

  async function handleStatusToggle(product: Product) {
    resetMessages()

    try {
      await updateProductStatus(product.id, !product.enabled)
      setSuccess(
        `${product.name} is now ${product.enabled ? 'disabled' : 'enabled'}.`,
      )
      await loadProducts()
    } catch (toggleError) {
      setError(getApiErrorMessage(toggleError))
    }
  }

  async function handleAddToCart(productId: number) {
    resetMessages()

    try {
      const nextCart = await addCartItem({
        productId,
        quantity: 1,
      })
      setCart(nextCart)
      setSuccess('Item added to cart.')
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    }
  }

  async function handleCartQuantityChange(cartItemId: number, quantity: number) {
    resetMessages()

    try {
      const nextCart = await updateCartItem(cartItemId, { quantity })
      setCart(nextCart)
      setSuccess('Cart updated successfully.')
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    }
  }

  async function handleRemoveCartItem(cartItemId: number) {
    resetMessages()

    try {
      await removeCartItem(cartItemId)
      setSuccess('Item removed from cart.')
      await loadCommerce()
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    }
  }

  async function handlePlaceOrder() {
    resetMessages()
    setIsPlacingOrder(true)

    try {
      await placeOrder()
      setSuccess('Order placed successfully.')
      await loadCommerce()
    } catch (submissionError) {
      setError(getApiErrorMessage(submissionError))
    } finally {
      setIsPlacingOrder(false)
    }
  }


  function openEditDialog(product: Product) {
    setEditingProduct(product)
    setForm({
      name: product.name,
      description: product.description,
      price: String(product.price),
      quantity: String(product.quantity),
    })
    setIsEditOpen(true)
  }

  return (
    <Box className="dashboard-page">
      <AppBar position="sticky" color="transparent" elevation={0} className="topbar">
        <Toolbar sx={{ gap: 2, justifyContent: 'space-between', py: 1 }}>
          <Stack direction="row" spacing={1.5} alignItems="center">
            <Box className="brand-mark">
              <StorefrontRoundedIcon />
            </Box>
            <Box>
              <Typography variant="h6" fontWeight={700}>
                Product Management UI
              </Typography>

            </Box>
          </Stack>

          <Stack direction="row" spacing={1.5} alignItems="center" flexWrap="wrap">
            <Chip
              icon={<ShieldRoundedIcon />}
              label={user?.role ?? 'Unknown role'}
              color="secondary"
            />
            <Chip
              label={`${user?.department ?? 'NA'} · ${user?.region ?? 'NA'}`}
              variant="outlined"
            />
            <Button
              onClick={logout}
              variant="outlined"
              color="inherit"
              startIcon={<LogoutRoundedIcon />}
            >
              Logout
            </Button>
          </Stack>
        </Toolbar>
      </AppBar>

      <Container maxWidth="xl" sx={{ py: 4 }}>
        <Grid container spacing={3}>
          <Grid size={{ xs: 12, lg: 4 }}>
            <DashboardHero
              isAdmin={isAdmin}
              userName={getDisplayName(user?.name, isAdmin)}
              productsCount={products.length}
              eventsCount={0}
              cartItemsCount={cart?.items.length ?? 0}
              isPlacingOrder={isPlacingOrder}
              onAddProduct={() => setIsCreateOpen(true)}
              onPlaceOrder={() => void handlePlaceOrder()}
            />
          </Grid>

          <Grid size={{ xs: 12, lg: 8 }}>
            <Stack spacing={2}>
              {error ? <Alert severity="error">{error}</Alert> : null}
              {success ? <Alert severity="success">{success}</Alert> : null}

              <ProductsSection
                products={products}
                isAdmin={isAdmin}
                isLoading={isLoadingProducts}
                onRefresh={() => void loadProducts()}
                onAddToCart={(productId) => void handleAddToCart(productId)}
                onEdit={(product) => openEditDialog(product)}
                onToggleStatus={(product) => void handleStatusToggle(product)}
              />

              {isAdmin ? (
                <Grid container spacing={2}>
                  <Grid size={{ xs: 12 }}>
                    <OrdersSection
                      orders={orders}
                      isAdmin
                      onRefresh={() => void loadCommerce()}
                    />
                  </Grid>
                </Grid>
              ) : (
                <Grid container spacing={2}>
                  <Grid size={{ xs: 12, xl: 6 }}>
                    <CartSection
                      cart={cart}
                      isLoading={isLoadingCommerce}
                      isPlacingOrder={isPlacingOrder}
                      onRefresh={() => void loadCommerce()}
                      onQuantityChange={(id, quantity) =>
                        void handleCartQuantityChange(id, quantity)
                      }
                      onRemoveItem={(id) => void handleRemoveCartItem(id)}
                      onPlaceOrder={() => void handlePlaceOrder()}
                    />
                  </Grid>

                  <Grid size={{ xs: 12, xl: 6 }}>
                    <OrdersSection
                      orders={orders}
                      isAdmin={false}
                      onRefresh={() => void loadCommerce()}
                    />
                  </Grid>
                </Grid>
              )}

            </Stack>
          </Grid>
        </Grid>
      </Container>

      <Dialog
        open={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        fullWidth
        maxWidth="sm"
      >
        <Box component="form" onSubmit={handleCreateSubmit}>
          <DialogTitle>Create product</DialogTitle>
          <DialogContent>
            <Stack spacing={2} sx={{ pt: 1 }}>
              <TextField
                label="Name"
                value={form.name}
                onChange={(event) => updateForm('name', event.target.value)}
                required
              />
              <TextField
                label="Description"
                value={form.description}
                onChange={(event) => updateForm('description', event.target.value)}
              />
              <TextField
                label="Price"
                type="number"
                value={form.price}
                onChange={(event) => updateForm('price', event.target.value)}
                required
              />
              <TextField
                label="Quantity"
                type="number"
                value={form.quantity}
                onChange={(event) => updateForm('quantity', event.target.value)}
                required
              />
            </Stack>
          </DialogContent>
          <DialogActions sx={{ px: 3, pb: 3 }}>
            <Button onClick={() => setIsCreateOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained">
              Save
            </Button>
          </DialogActions>
        </Box>
      </Dialog>

      <Dialog
        open={isEditOpen}
        onClose={() => setIsEditOpen(false)}
        fullWidth
        maxWidth="sm"
      >
        <Box component="form" onSubmit={handleEditSubmit}>
          <DialogTitle>Edit product</DialogTitle>
          <DialogContent>
            <Stack spacing={2} sx={{ pt: 1 }}>
              <TextField
                label="Name"
                value={form.name}
                onChange={(event) => updateForm('name', event.target.value)}
                required
              />
              <TextField
                label="Description"
                value={form.description}
                onChange={(event) => updateForm('description', event.target.value)}
              />
              <TextField
                label="Price"
                type="number"
                value={form.price}
                onChange={(event) => updateForm('price', event.target.value)}
                required
              />
              <TextField
                label="Quantity"
                type="number"
                value={form.quantity}
                onChange={(event) => updateForm('quantity', event.target.value)}
                required
              />
            </Stack>
          </DialogContent>
          <DialogActions sx={{ px: 3, pb: 3 }}>
            <Button onClick={() => setIsEditOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained">
              Update
            </Button>
          </DialogActions>
        </Box>
      </Dialog>
    </Box>
  )
}
