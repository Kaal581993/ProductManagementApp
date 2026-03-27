type UserRole = 'ROLE_ADMIN' | 'ROLE_USER' | string

interface AuthResponse {
  token: string
  tokenType: string
  userId: number
  name: string
  email: string
  role: UserRole
  department: string
  region: string
}

interface UserProfile {
  id: number
  name: string
  email: string
  role: UserRole
  department: string
  region: string
}

interface LoginPayload {
  email: string
  password: string
}

interface RegisterPayload {
  name: string
  email: string
  password: string
  department: string
  region: string
}

interface Product {
  id: number
  name: string
  description: string
  price: number
  quantity: number
  enabled: boolean
}

interface ProductCreatePayload {
  name: string
  description: string
  price: number
  quantity: number
}

interface ProductUpdatePayload extends ProductCreatePayload {
  enabled: boolean
}

interface CartItem {
  cartItemId: number
  productId: number
  productName: string
  quantity: number
  unitPrice: number
  lineTotal: number
}

interface Cart {
  cartId: number
  items: CartItem[]
  totalAmount: number
}

interface AddCartItemPayload {
  productId: number
  quantity: number
}

interface UpdateCartItemPayload {
  quantity: number
}

interface OrderItem {
  orderItemId: number
  productId: number
  productName: string
  quantity: number
  unitPrice: number
  lineTotal: number
}

interface Order {
  orderId: number
  userId: number
  customerName?: string | null
  status: string
  totalAmount: number
  createdAt: string
  items: OrderItem[]
}

interface StoredEvent {
  id: number
  eventKey: string
  eventType: string
  payload: string
  receivedAt: string
}

interface AuthorizationRequestPayload {
  resourceOwnerId: number
  resourceDepartment: string
  resourceRegion: string
  action: string
}

interface AuthorizationResult {
  allowed: boolean
  reason: string
}

interface ApiErrorResponse {
  timestamp?: string
  status?: number
  error?: string
  message?: string
  validationErrors?: Record<string, string>
}

export type {
  AuthResponse,
  UserProfile,
  LoginPayload,
  RegisterPayload,
  Product,
  ProductCreatePayload,
  ProductUpdatePayload,
  CartItem,
  Cart,
  AddCartItemPayload,
  UpdateCartItemPayload,
  OrderItem,
  Order,
  StoredEvent,
  AuthorizationRequestPayload,
  AuthorizationResult,
  ApiErrorResponse,
}